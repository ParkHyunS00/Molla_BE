package com.example.molla.domain.diary.controller;

import com.example.molla.common.DeleteResponse;
import com.example.molla.common.PageResponse;
import com.example.molla.common.StandardResponse;
import com.example.molla.common.UpdateResponse;
import com.example.molla.domain.diary.domain.Diary;
import com.example.molla.domain.diary.dto.DiaryCreateDTO;
import com.example.molla.domain.diary.dto.DiaryListResponseDTO;
import com.example.molla.domain.diary.service.DiaryService;
import com.example.molla.exception.BusinessException;
import com.example.molla.exception.ErrorCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/diary")
@RequiredArgsConstructor
public class DiaryRestController {

    private final DiaryService diaryService;

    @PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<StandardResponse<Long>> saveDiary(
            @Valid @RequestPart("diary") DiaryCreateDTO diaryCreateDTO,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {

        Long diaryId = diaryService.save(diaryCreateDTO, images);
        return StandardResponse.of(diaryId, HttpStatus.CREATED);
    }

    @GetMapping("/list/{id}")
    public ResponseEntity<StandardResponse<PageResponse<DiaryListResponseDTO>>> getDiaryList(
            @PathVariable("id") Long userId,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize) {

        if (pageNumber < 0 || pageSize <= 0) {
            throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
        }

        PageResponse<DiaryListResponseDTO> diaries = diaryService.getDiaryList(userId, pageNumber, pageSize);
        return StandardResponse.ofOk(diaries);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StandardResponse<UpdateResponse>> updateDiary(
            @PathVariable("id") Long diaryId,
            @RequestPart("diary") DiaryCreateDTO diaryCreateDTO,
            @RequestPart(value = "updateImages", required = false) List<MultipartFile> updateImages,
            @RequestPart(value = "deleteImages", required = false) List<Long> deleteImages) {

        UpdateResponse updateResponse = diaryService.updateDiary(diaryId, diaryCreateDTO, updateImages, deleteImages);
        return StandardResponse.ofOk(updateResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<StandardResponse<DeleteResponse>> deleteDiary(@PathVariable("id") Long diaryId) {

        DeleteResponse deleteResponse = diaryService.deleteDiary(diaryId);
        return StandardResponse.ofOk(deleteResponse);
    }
}
