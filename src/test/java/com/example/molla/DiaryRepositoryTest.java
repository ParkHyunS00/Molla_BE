package com.example.molla;

import com.example.molla.domain.common.Emotion;
import com.example.molla.domain.diary.domain.Diary;
import com.example.molla.domain.diary.repository.DiaryRepository;
import com.example.molla.domain.user.domain.User;
import com.example.molla.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class DiaryRepositoryTest {

    @Autowired
    private DiaryRepository diaryRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        // 테스트용 사용자 생성
        user = new User();
        user.setEmail("test@test.com");
        user.setPassword("123456");
        user.setUsername("test1");
        userRepository.save(user);

        // 테스트용 다이어리 데이터 생성
        createDiaryEntry("Title 1", "Content 1", Emotion.SAD, user, LocalDateTime.now().minusDays(1));
        createDiaryEntry("Title 2", "Content 2", Emotion.HURT, user, LocalDateTime.now().minusDays(2));
        createDiaryEntry("Title 3", "Content 3", Emotion.ANXIOUS, user, LocalDateTime.now().minusDays(3));
        createDiaryEntry("Title 4", "Content 4", Emotion.HAPPY, user, LocalDateTime.now().minusDays(4));
        createDiaryEntry("Title 5", "Content 5", Emotion.NOTHING, user, LocalDateTime.now().minusDays(5));
    }

    private void createDiaryEntry(String title, String content, Emotion emotion, User user, LocalDateTime createDate) {
        Diary diary = new Diary();
        diary.setTitle(title);
        diary.setContent(content);
        diary.setDiaryEmotion(emotion);
        diary.setUser(user);
        diary.setCreateDate(createDate);
        diaryRepository.save(diary);
    }

    @Test
    void testFindPrimaryEmotionAndCountByUserAndDateRange() {
        // Given: 최근 7일 동안의 범위를 지정
        LocalDateTime startDate = LocalDateTime.now().minusDays(7);
        LocalDateTime endDate = LocalDateTime.now();

        // When: 사용자와 날짜 범위를 기준으로 주된 감정과 그 빈도를 조회
        List<Object[]> results = diaryRepository.findPrimaryEmotionByUserFor7days(user.getId(), startDate, endDate);

        System.out.println(results.size());
        // Then: 결과 검증
        assertThat(results).isNotEmpty();
        System.out.println(results.get(0)[0] + " " + results.get(0)[1]);

    }
}
