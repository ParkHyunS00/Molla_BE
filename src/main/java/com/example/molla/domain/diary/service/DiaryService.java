package com.example.molla.domain.diary.service;

import com.example.molla.domain.diary.domain.Diary;
import com.example.molla.domain.diary.dto.DiaryCreateDTO;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
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

    // TODO : 필요하다면 페이징 처리
    public List<DiaryListResponseDTO> getDiaryList(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        List<Diary> diaries = diaryRepository.findByUserId(user.getId());

        return diaries.stream()
                .map(diary -> getDiary(diary.getId()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateDiary(Long diaryId, DiaryCreateDTO diaryCreateDTO, List<MultipartFile> newImages, List<Long> deleteImages) {

        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new BusinessException(ErrorCode.DIARY_NOT_FOUND));

        diary.updateDiary(diaryCreateDTO.getTitle(), diaryCreateDTO.getContent(), diaryCreateDTO.getDiaryEmotion());

        if (deleteImages != null && !deleteImages.isEmpty()) {
            deleteDiaryImagesById(deleteImages);  // 삭제할 이미지 처리
        }

        if (newImages != null && !newImages.isEmpty()) {
            newImages.forEach(image -> {
                try {
                    saveImage(image, diary);
                } catch (IOException e) {
                   throw new RuntimeException("이미지 저장에 실패했습니다. : " + image.getOriginalFilename(), e);
                }
            });
        }
    }

    @Transactional
    public void deleteDiary(Long diaryId) {

        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new BusinessException(ErrorCode.DIARY_NOT_FOUND));

        deleteDiaryImagesByDiaryId(diary.getId());

        diaryRepository.delete(diary);
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

    private DiaryListResponseDTO getDiary(Long diaryId) {

        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new BusinessException(ErrorCode.DIARY_NOT_FOUND));

        List<File> imageFiles = getImageFiles(diary.getId());

        return DiaryListResponseDTO.builder()
                .diaryId(diary.getId())
                .title(diary.getTitle())
                .content(diary.getContent())
                .diaryEmotion(diary.getDiaryEmotion())
                .createDate(diary.getCreateDate())
                .images(imageFiles)
                .build();
    }

    private List<File> getImageFiles(Long diaryId) {

        List<DiaryImage> diaryImages = diaryImageRepository.findByDiaryId(diaryId);

        return diaryImages.stream()
                .map(image -> new File(imageUploadDir + File.separator + image.getImagePath()))
                .filter(File::exists)
                .collect(Collectors.toList());
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
