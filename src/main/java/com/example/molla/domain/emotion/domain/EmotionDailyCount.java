package com.example.molla.domain.emotion.domain;

import com.example.molla.domain.common.Emotion;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class EmotionDailyCount {

    @Id
    private LocalDate date;
    private Long angryCount = 0L;
    private Long sadCount = 0L;
    private Long anxiousCount = 0L;
    private Long hurtCount = 0L;
    private Long happyCount = 0L;
    private Long nothingCount = 0L;

    public EmotionDailyCount(LocalDate date) {
        this.date = date;
    }

    public void incrementEmotionCount(Emotion emotion) {
        switch (emotion) {
            case ANGRY:
                this.angryCount++;
                break;
            case SAD:
                this.sadCount++;
                break;
            case ANXIOUS:
                this.anxiousCount++;
                break;
            case HURT:
                this.hurtCount++;
                break;
            case HAPPY:
                this.happyCount++;
                break;
            case NOTHING:
                this.nothingCount++;
                break;
        }
    }

    public void decrementEmotionCount(Emotion emotion) {
        switch (emotion) {
            case ANGRY:
                if (this.angryCount > 0) this.angryCount--;
                break;
            case SAD:
                if (this.sadCount > 0) this.sadCount--;
                break;
            case ANXIOUS:
                if (this.anxiousCount > 0) this.anxiousCount--;
                break;
            case HURT:
                if (this.hurtCount > 0) this.hurtCount--;
                break;
            case HAPPY:
                if (this.happyCount > 0) this.happyCount--;
                break;
            case NOTHING:
                if (this.nothingCount > 0) this.nothingCount--;
                break;
        }
    }
}
