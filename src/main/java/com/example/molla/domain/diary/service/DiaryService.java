package com.example.molla.domain.diary.service;

import com.example.molla.common.DeleteResponse;
import com.example.molla.common.PageResponse;
import com.example.molla.common.UpdateResponse;
import com.example.molla.domain.common.Emotion;
import com.example.molla.domain.diary.domain.Diary;
import com.example.molla.domain.diary.dto.DiaryCreateDTO;
import com.example.molla.domain.diary.dto.DiaryImageResponseDTO;
import com.example.molla.domain.diary.dto.DiaryListResponseDTO;
import com.example.molla.domain.diary.repository.DiaryRepository;
import com.example.molla.domain.diary.domain.DiaryImage;
import com.example.molla.domain.diary.repository.DiaryImageRepository;
import com.example.molla.domain.user.domain.User;
import com.example.molla.domain.user.repository.UserRepository;
import com.example.molla.exception.BusinessException;
import com.example.molla.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DiaryService {

    private final DiaryRepository diaryRepository;
    private final UserRepository userRepository;
    private final DiaryImageRepository diaryImageRepository;

    @Value("${image.upload.dir}")
    private String imageUploadDir;

    @Transactional
    public Long save(DiaryCreateDTO diaryCreateDTO, List<MultipartFile> images) {

        User user = userRepository.findById(diaryCreateDTO.getUserId())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Diary diary = diaryCreateDTO.toEntity(user, LocalDateTime.now());
        Diary savedDiary = diaryRepository.save(diary);

        if (images != null && !images.isEmpty()) {
            images.forEach(image -> {
                try {
                    saveImage(image, savedDiary);
                } catch (IOException e) {
                    throw new RuntimeException("이미지 저장에 실패했습니다. : " + image.getOriginalFilename(), e);
                }
            });
        }

        return savedDiary.getId();
    }

    public PageResponse<DiaryListResponseDTO> getDiaryList(Long userId, int pageNumber, int pageSize) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // 페이징 처리
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("createDate").descending());
        Page<Diary> diaryPage = diaryRepository.findByUserId(user.getId(), pageable);

        // 다이어리 id 값 분류
        List<Long> diaryIds = diaryPage.getContent().stream().map(Diary::getId).toList();

        // 다이어리 id로 이미지 조회
        List<DiaryImage> diaryImages = diaryImageRepository.findByDiaryIds(diaryIds);

        // 다이어리 id 로 된 이미지들 그룹화
        Map<Long, List<DiaryImage>> diaryImageMap = diaryImages.stream()
                .collect(Collectors.groupingBy(diaryImage -> diaryImage.getDiary().getId()));

        // 다이어리, 다이어리 이미지로 응답 DTO 형식에 맞춘 객체 만들기
        List<DiaryListResponseDTO> content = diaryPage.getContent().stream()
                .map(diary -> convertToDiaryListResponseDTO(diary, diaryImageMap.getOrDefault(diary.getId(), Collections.emptyList())))
                .toList();

        return PageResponse.<DiaryListResponseDTO>builder()
                .content(content)
                .totalPage(diaryPage.getTotalPages())
                .totalElement(diaryPage.getTotalElements())
                .currentPage(diaryPage.getNumber())
                .isLast(diaryPage.isLast())
                .build();
    }

    @Transactional
    public UpdateResponse updateDiary(Long diaryId, DiaryCreateDTO diaryCreateDTO, List<MultipartFile> newImages, List<Long> deleteImages) {

        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new BusinessException(ErrorCode.DIARY_NOT_FOUND));

        // 변경 감지
        diary.updateDiary(diaryCreateDTO.getTitle(), diaryCreateDTO.getContent(), diaryCreateDTO.getDiaryEmotion());

        if (deleteImages != null && !deleteImages.isEmpty()) {
            deleteDiaryImagesById(deleteImages);  // 삭제할 이미지 처리
        }

        if (newImages != null && !newImages.isEmpty()) {
            newImages.forEach(image -> {
                try {
                    saveImage(image, diary);
                } catch (IOException e) {
                    throw new BusinessException(ErrorCode.IMAGE_SAVE_FAILED);
                }
            });
        }

        return new UpdateResponse(diary.getId(), "일기");
    }

    @Transactional
    public void updateDiaryEmotion(Long diaryId, Emotion emotion) {

        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new BusinessException(ErrorCode.DIARY_NOT_FOUND));

        diary.updateDiary(emotion);
    }

    @Transactional
    public DeleteResponse deleteDiary(Long diaryId) {

        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new BusinessException(ErrorCode.DIARY_NOT_FOUND));

        deleteDiaryImagesByDiaryId(diary.getId());

        diaryRepository.delete(diary);
        return new DeleteResponse(diary.getId(), "일기");
    }

    // 이미지가 존재하지 않거나 여러 개일 때도 대응
    private DiaryListResponseDTO convertToDiaryListResponseDTO(Diary diary, List<DiaryImage> images) {
        List<DiaryImageResponseDTO> base64Images = images.stream()
                .map(image -> {
                    File file = new File(imageUploadDir + File.separator + image.getImagePath());
                    if (file.exists()) {
                        try {
                            byte[] fileContent = Files.readAllBytes(file.toPath());
                            String base64EncodedImage = Base64.getEncoder().encodeToString(fileContent);
                            return DiaryImageResponseDTO.builder()
                                    .imageId(image.getId())
                                    .base64EncodedImage(base64EncodedImage)
                                    .build();
                        } catch (IOException e) {
                            throw new BusinessException(ErrorCode.IMAGE_READ_FAILED);
                        }
                    } else {
                        throw new BusinessException(ErrorCode.IMAGE_NOT_FOUND);
                    }
                })
                .collect(Collectors.toList());

        return DiaryListResponseDTO.builder()
                .diaryId(diary.getId())
                .title(diary.getTitle())
                .content(diary.getContent())
                .diaryEmotion(diary.getDiaryEmotion())
                .createDate(diary.getCreateDate())
                .images(base64Images)
                .build();
    }

    private void saveImage(MultipartFile file, Diary diary) throws IOException {

        String originalFilename = file.getOriginalFilename();
        String extension = (originalFilename != null) ? originalFilename.substring(originalFilename.lastIndexOf(".")) : "";

        String savedFileName = UUID.randomUUID() + extension;

        File destinationFile = new File(imageUploadDir + File.separator + savedFileName);
        file.transferTo(destinationFile);

        DiaryImage diaryImage = DiaryImage.builder()
                .imagePath(savedFileName)
                .diary(diary)
                .build();
        diaryImageRepository.save(diaryImage);
    }

    private void deleteDiaryImagesByDiaryId(Long diaryId) {

        List<DiaryImage> diaryImages = diaryImageRepository.findByDiaryId(diaryId);

        diaryImages.stream()
                .map(diaryImage -> imageUploadDir + File.separator + diaryImage.getImagePath())
                .map(File::new)
                .filter(File::exists)
                .forEach(File::delete);

        diaryImageRepository.deleteAll(diaryImages);
    }

    private void deleteDiaryImagesById(List<Long> imageIds) {

        List<DiaryImage> diaryImages = diaryImageRepository.findAllById(imageIds);

        diaryImages.stream()
                .map(diaryImage -> imageUploadDir + File.separator + diaryImage.getImagePath())
                .map(File::new)
                .filter(File::exists)
                .forEach(File::delete);

        diaryImageRepository.deleteAll(diaryImages);
    }
}
