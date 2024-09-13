package com.example.molla.domain.diary.service;

import com.example.molla.common.DeleteResponse;
import com.example.molla.common.PageResponse;
import com.example.molla.common.UpdateResponse;
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
import java.util.Base64;
import java.util.List;
import java.util.UUID;
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

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("createDate").descending());
        Page<Diary> diaryPage = diaryRepository.findByUserId(user.getId(), pageable);

        List<DiaryListResponseDTO> content = diaryPage.getContent().stream()
                .map(this::convertToDiaryListResponseDTO)
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
    public DeleteResponse deleteDiary(Long diaryId) {

        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new BusinessException(ErrorCode.DIARY_NOT_FOUND));

        deleteDiaryImagesByDiaryId(diary.getId());

        diaryRepository.delete(diary);
        return new DeleteResponse(diary.getId(), "일기");
    }

    private DiaryListResponseDTO convertToDiaryListResponseDTO(Diary diary) {

        List<DiaryImageResponseDTO> base64Images = getImageBase64Strings(diary.getId());

        return DiaryListResponseDTO.builder()
                .diaryId(diary.getId())
                .title(diary.getTitle())
                .content(diary.getContent())
                .diaryEmotion(diary.getDiaryEmotion())
                .createDate(diary.getCreateDate())
                .images(base64Images)
                .build();
    }

    private List<DiaryImageResponseDTO> getImageBase64Strings(Long diaryId) {

        List<DiaryImage> diaryImages = diaryImageRepository.findByDiaryId(diaryId);

        // DiaryImage 목록을 DiaryImageResponseDTO로 변환
        return diaryImages.stream()
                .map(image -> {
                    File file = new File(imageUploadDir + File.separator + image.getImagePath());
                    if (file.exists()) {
                        try {
                            // 이미지 파일을 Base64로 변환
                            byte[] fileContent = Files.readAllBytes(file.toPath());
                            String base64EncodedImage = Base64.getEncoder().encodeToString(fileContent);

                            // DiaryImageResponseDTO로 변환하여 반환
                            return DiaryImageResponseDTO.builder()
                                    .imageId(image.getId())  // 이미지 ID 설정
                                    .base64EncodedImage(base64EncodedImage)  // Base64로 인코딩된 이미지 설정
                                    .build();

                        } catch (IOException e) {
                            throw new BusinessException(ErrorCode.IMAGE_READ_FAILED);
                        }
                    } else {
                        throw new BusinessException(ErrorCode.IMAGE_NOT_FOUND);
                    }
                })
                .collect(Collectors.toList());
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
