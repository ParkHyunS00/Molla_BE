package com.example.molla.domain.emotion.service;

import com.example.molla.domain.common.Emotion;
import com.example.molla.domain.emotion.domain.EmotionDailyCount;
import com.example.molla.domain.emotion.dto.EmotionSumDTO;
import com.example.molla.domain.emotion.repository.EmotionDailyCountRepository;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class EmotionDailyCountService {

    private final EmotionDailyCountRepository emotionDailyCountRepository;

    public EmotionSumDTO getEmotionSumForLast7Days(String endDateString) {
        LocalDate endDate = LocalDate.parse(endDateString);
        LocalDate startDate = endDate.minusDays(6);

        return emotionDailyCountRepository.findEmotionSumBetweenDates(startDate, endDate);
    }

    @Transactional
    public void updateEmotionCount(LocalDate date, Emotion oldEmotion, Emotion newEmotion) {
        EmotionDailyCount emotionDailyCount = emotionDailyCountRepository.findById(date)
                .orElseGet(() -> new EmotionDailyCount(date));

        if (oldEmotion != null) {
            emotionDailyCount.decrementEmotionCount(oldEmotion);
        }

        if (newEmotion != null) {
            emotionDailyCount.incrementEmotionCount(newEmotion);
        }

        emotionDailyCountRepository.save(emotionDailyCount);
    }
}
