package com.example.molla.domain.emotion.repository;

import com.example.molla.domain.emotion.domain.EmotionDailyCount;
import com.example.molla.domain.emotion.dto.EmotionSumDTO;
import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EmotionDailyCountRepository extends JpaRepository<EmotionDailyCount, LocalDate> {

    @Query("select new com.example.molla.domain.emotion.dto.EmotionSumDTO(" +
            "sum(e.angryCount), " +
            "sum(e.sadCount), " +
            "sum(e.anxiousCount), " +
            "sum(e.hurtCount), "+
            "sum(e.happyCount), " +
            "sum(e.nothingCount)) " +
            "from EmotionDailyCount e " +
            "where e.date between :startDate and :endDate")
    EmotionSumDTO findEmotionSumBetweenDates(LocalDate startDate, LocalDate endDate);
}
