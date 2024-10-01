package com.example.molla.domain.diary.repository;

import com.example.molla.domain.diary.domain.DiaryImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiaryImageRepository extends JpaRepository<DiaryImage, Long> {

    List<DiaryImage> findByDiaryId(Long diaryId);

    @Query("select di from DiaryImage di where di.diary.id in :diaryIds")
    List<DiaryImage> findByDiaryIds(@Param("diaryIds") List<Long> diaryIds);
}
