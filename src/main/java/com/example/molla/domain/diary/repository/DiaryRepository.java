package com.example.molla.domain.diary.repository;

import com.example.molla.domain.diary.domain.Diary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface DiaryRepository extends JpaRepository<Diary, Long> {

    @Query("select d.diaryEmotion, count(d.diaryEmotion) from Diary d" +
            " where d.user.id = :userId" +
            " and d.createDate >= :startDate" +
            " and d.createDate <= :endDate" +
            " group by d.diaryEmotion" +
            " order by count(d.diaryEmotion) desc, max(d.createDate) desc limit 1")
    List<Object[]> findPrimaryEmotionByUserFor7days(@Param("userId") Long userId,
                                                    @Param("startDate") LocalDateTime startDate,
                                                    @Param("endDate") LocalDateTime endDate);

    List<Diary> findByUserId(Long userId);
}
