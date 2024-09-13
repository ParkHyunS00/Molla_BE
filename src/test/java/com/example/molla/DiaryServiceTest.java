//package com.example.molla;
//
//import com.example.molla.domain.common.Emotion;
//import com.example.molla.domain.diary.domain.Diary;
//import com.example.molla.domain.diary.dto.DiaryCreateDTO;
//import com.example.molla.domain.diary.dto.DiaryListResponseDTO;
//import com.example.molla.domain.diary.repository.DiaryRepository;
//import com.example.molla.domain.diary.service.DiaryService;
//import com.example.molla.domain.diary.domain.DiaryImage;
//import com.example.molla.domain.diary.repository.DiaryImageRepository;
//import com.example.molla.domain.user.domain.User;
//import com.example.molla.domain.user.repository.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
////import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.assertj.core.api.Assertions.assertThat;
//
//@ExtendWith(SpringExtension.class)
//@SpringBootTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@Transactional
//public class DiaryServiceTest {
//
//    @Autowired
//    private DiaryService diaryService;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private DiaryRepository diaryRepository;
//
//    @Autowired
//    private DiaryImageRepository diaryImageRepository;
//
//    private User user;
//
//    @BeforeEach
//    void setUp() {
//        user = userRepository.save(new User("testuser", "testuser@example.com", "password"));
//    }
//
//    @Test
//    @DisplayName("일기와 일기에 포함된 이미지가 함께 저장되어야 한다.")
//    void testSaveDiaryWithImages() throws IOException {
//        // Given
//        DiaryCreateDTO diaryCreateDTO = new DiaryCreateDTO();
//        diaryCreateDTO.setTitle("Test Diary");
//        diaryCreateDTO.setContent("This is a test diary.");
//        diaryCreateDTO.setDiaryEmotion(Emotion.HAPPY);
//        diaryCreateDTO.setCreateDate(LocalDateTime.now());
//        diaryCreateDTO.setUserId(user.getId());
//
//        // 절대 경로로 이미지 파일 준비
//        String absolutePath = "/Users/parkhyunsoo/Downloads/406EF422-5382-4B90-B007-09755B192220_1_201_a-Photoroom.jpg";
//        File file = new File(absolutePath);
//        FileInputStream input = new FileInputStream(file);
//        MockMultipartFile multipartFile = new MockMultipartFile("images", file.getName(), "image/jpeg", input);
//
//        // When
//        Long diaryId = diaryService.save(diaryCreateDTO, List.of(multipartFile));
//
//        // Then
//        Optional<Diary> savedDiary = diaryRepository.findById(diaryId);
//        assertThat(savedDiary).isPresent();
//        assertThat(savedDiary.get().getTitle()).isEqualTo(diaryCreateDTO.getTitle());
//
//        // 이미지 파일이 저장되었는지 확인
//        List<DiaryImage> diaryImage = diaryImageRepository.findByDiaryId(diaryId);
//        System.out.println(diaryImage.get(0).getImagePath());
//        File savedImageFile = new File("/Users/parkhyunsoo/Desktop/Development/Thesis Project/molla_diary_images/" + diaryImage.get(0).getImagePath());
//        assertTrue(savedImageFile.exists());
//    }
//
//    @Test
//    @DisplayName("이미지 포함안된 일기 목록 조회")
////    void testGetDiaryList() {
////        // Given: 일기 2개 저장
////        DiaryCreateDTO diaryCreateDTO1 = new DiaryCreateDTO();
////        diaryCreateDTO1.setTitle("Diary 1");
////        diaryCreateDTO1.setContent("This is diary 1.");
////        diaryCreateDTO1.setDiaryEmotion(Emotion.HAPPY);
////        diaryCreateDTO1.setCreateDate(LocalDateTime.now());
////        diaryCreateDTO1.setUserId(user.getId());
////
////        DiaryCreateDTO diaryCreateDTO2 = new DiaryCreateDTO();
////        diaryCreateDTO2.setTitle("Diary 2");
////        diaryCreateDTO2.setContent("This is diary 2.");
////        diaryCreateDTO2.setDiaryEmotion(Emotion.SAD);
////        diaryCreateDTO2.setCreateDate(LocalDateTime.now());
////        diaryCreateDTO2.setUserId(user.getId());
////
////        diaryService.save(diaryCreateDTO1, new ArrayList<>());
////        diaryService.save(diaryCreateDTO2, new ArrayList<>());
////
////        // When: 유저의 일기 목록 조회
////        List<DiaryListResponseDTO> diaryList = diaryService.getDiaryList(user.getId());
////
////        // Then: 일기 목록이 제대로 조회되는지 검증
////        assertThat(diaryList.size()).isEqualTo(2);
////        assertThat(diaryList.get(0).getTitle()).isEqualTo("Diary 1");
////        assertThat(diaryList.get(1).getTitle()).isEqualTo("Diary 2");
////    }
//
//    @Test
//    @DisplayName("이미지가 포함된 일기 목록 조회")
//    void testGetDiaryListWithImages() throws IOException {
//        // Given: 이미지가 포함된 일기 2개 저장
//        DiaryCreateDTO diaryCreateDTO1 = new DiaryCreateDTO();
//        diaryCreateDTO1.setTitle("Diary 1 with Image");
//        diaryCreateDTO1.setContent("This is diary 1 with an image.");
//        diaryCreateDTO1.setDiaryEmotion(Emotion.HAPPY);
//        diaryCreateDTO1.setCreateDate(LocalDateTime.now());
//        diaryCreateDTO1.setUserId(user.getId());
//
//        DiaryCreateDTO diaryCreateDTO2 = new DiaryCreateDTO();
//        diaryCreateDTO2.setTitle("Diary 2 with Image");
//        diaryCreateDTO2.setContent("This is diary 2 with an image.");
//        diaryCreateDTO2.setDiaryEmotion(Emotion.SAD);
//        diaryCreateDTO2.setCreateDate(LocalDateTime.now());
//        diaryCreateDTO2.setUserId(user.getId());
//
//        // 절대 경로로 테스트할 이미지 파일 준비
//        String absolutePath1 = "/Users/parkhyunsoo/Downloads/406EF422-5382-4B90-B007-09755B192220_1_201_a-Photoroom.jpg"; // 실제 경로로 수정
//        File file1 = new File(absolutePath1);
//        FileInputStream input1 = new FileInputStream(file1);
//        MockMultipartFile multipartFile1 = new MockMultipartFile("images", file1.getName(), "image/jpeg", input1);
//
//        String absolutePath2 = "/Users/parkhyunsoo/Downloads/406EF422-5382-4B90-B007-09755B192220_1_201_a-Photoroom.jpg"; // 실제 경로로 수정
//        File file2 = new File(absolutePath2);
//        FileInputStream input2 = new FileInputStream(file2);
//        MockMultipartFile multipartFile2 = new MockMultipartFile("images", file2.getName(), "image/jpeg", input2);
//
//        // 일기 1에 이미지 추가하여 저장
//        diaryService.save(diaryCreateDTO1, List.of(multipartFile1));
//
//        // 일기 2에 이미지 추가하여 저장
//        diaryService.save(diaryCreateDTO2, List.of(multipartFile2));
//
//        // When: 유저의 일기 목록 조회
//        List<DiaryListResponseDTO> diaryList = diaryService.getDiaryList(user.getId());
//
//        // Then: 일기 목록이 제대로 조회되고 이미지도 조회되는지 검증
//        assertThat(diaryList.size()).isEqualTo(2);
//
//        // 일기 1 검증
//        DiaryListResponseDTO diary1 = diaryList.get(0);
//        assertThat(diary1.getTitle()).isEqualTo("Diary 1 with Image");
//        assertThat(diary1.getContent()).isEqualTo("This is diary 1 with an image.");
//        assertThat(diary1.getImages()).isNotEmpty(); // 이미지 리스트가 비어있지 않은지 확인
//        File savedImageFile1 = new File("/Users/parkhyunsoo/Desktop/Development/Thesis Project/molla_diary_images/" + diary1.getImages().get(0).getName());
//        assertThat(savedImageFile1.exists()).isTrue(); // 실제로 이미지 파일이 저장되었는지 확인
//
//        // 일기 2 검증
//        DiaryListResponseDTO diary2 = diaryList.get(1);
//        assertThat(diary2.getTitle()).isEqualTo("Diary 2 with Image");
//        assertThat(diary2.getContent()).isEqualTo("This is diary 2 with an image.");
//        assertThat(diary2.getImages()).isNotEmpty(); // 이미지 리스트가 비어있지 않은지 확인
//        File savedImageFile2 = new File("/Users/parkhyunsoo/Desktop/Development/Thesis Project/molla_diary_images/" + diary2.getImages().get(0).getName());
//        assertThat(savedImageFile2.exists()).isTrue(); // 실제로 이미지 파일이 저장되었는지 확인
//    }
//
//    @Test
//    @DisplayName("이미지가 포함된 일기 삭제 테스트")
//    void testDeleteDiary() throws IOException {
//        // Given: 이미지가 포함된 일기 저장
//        DiaryCreateDTO diaryCreateDTO = new DiaryCreateDTO();
//        diaryCreateDTO.setTitle("Diary to be deleted");
//        diaryCreateDTO.setContent("This diary contains images and will be deleted.");
//        diaryCreateDTO.setDiaryEmotion(Emotion.HAPPY);
//        diaryCreateDTO.setCreateDate(LocalDateTime.now());
//        diaryCreateDTO.setUserId(user.getId());
//
//        // 절대 경로로 테스트할 이미지 파일 준비
//        String absolutePath = "/Users/parkhyunsoo/Downloads/406EF422-5382-4B90-B007-09755B192220_1_201_a-Photoroom.jpg";  // 실제 경로로 수정
//        File file = new File(absolutePath);
//        FileInputStream input = new FileInputStream(file);
//        MockMultipartFile multipartFile = new MockMultipartFile("images", file.getName(), "image/jpeg", input);
//
//        // 일기 저장
//        Long savedDiaryId = diaryService.save(diaryCreateDTO, List.of(multipartFile));
//
//        // 저장된 이미지 경로 확인
//        List<DiaryImage> diaryImages = diaryImageRepository.findByDiaryId(savedDiaryId);
//        assertThat(diaryImages).isNotEmpty();
//        String savedImagePath = "/Users/parkhyunsoo/Desktop/Development/Thesis Project/molla_diary_images/" + diaryImages.get(0).getImagePath();
//        File savedImageFile = new File(savedImagePath);
//        assertTrue(savedImageFile.exists());  // 이미지 파일이 존재하는지 확인
//
//        System.out.println(diaryImages.get(0).getImagePath());
//
//        // When: 일기 삭제
//        diaryService.deleteDiary(savedDiaryId);
//
//        // Then: 일기가 정상적으로 삭제되었는지 확인
//        Optional<Diary> deletedDiary = diaryRepository.findById(savedDiaryId);
//        assertThat(deletedDiary).isEmpty();  // 일기가 삭제되었는지 확인
//
//
//        // 저장된 이미지도 삭제되었는지 확인
//        diaryImages = diaryImageRepository.findByDiaryId(savedDiaryId);
//        assertThat(diaryImages).isEmpty();  // 데이터베이스에서 이미지가 삭제되었는지 확인
//        assertThat(savedImageFile.exists()).isFalse();  // 파일 시스템에서 이미지가 삭제되었는지 확인
//    }
//
//    @Test
//    @DisplayName("이미지가 포함된 일기 수정 테스트")
//    void testUpdateDiaryWithImages() throws IOException {
//        // Given: 기존 일기 저장
//        DiaryCreateDTO diaryCreateDTO = new DiaryCreateDTO();
//        diaryCreateDTO.setTitle("Original Diary");
//        diaryCreateDTO.setContent("This is the original content.");
//        diaryCreateDTO.setDiaryEmotion(Emotion.HAPPY);
//        diaryCreateDTO.setCreateDate(LocalDateTime.now());
//        diaryCreateDTO.setUserId(user.getId());
//
//        // 절대 경로로 테스트할 이미지 파일 준비
//        String absolutePath = "/Users/parkhyunsoo/Downloads/406EF422-5382-4B90-B007-09755B192220_1_201_a-Photoroom.jpg";  // 실제 경로로 수정
//        File file = new File(absolutePath);
//        FileInputStream input = new FileInputStream(file);
//        MockMultipartFile originalImage = new MockMultipartFile("images", file.getName(), "image/jpeg", input);
//
//        // 일기 저장
//        Long savedDiaryId = diaryService.save(diaryCreateDTO, List.of(originalImage));
//
//        // 저장된 이미지 파일 확인
//        List<DiaryImage> savedImages = diaryImageRepository.findByDiaryId(savedDiaryId);
//        assertThat(savedImages).isNotEmpty();
//        String savedImagePath = "/Users/parkhyunsoo/Desktop/Development/Thesis Project/molla_diary_images/" + savedImages.get(0).getImagePath();
//        File savedImageFile = new File(savedImagePath);
//        assertTrue(savedImageFile.exists());  // 이미지 파일이 존재하는지 확인
//
//        // Update: 수정할 이미지 파일 준비
//        String updatedImagePath = "/Users/parkhyunsoo/Downloads/406EF422-5382-4B90-B007-09755B192220_1_201_a-Photoroom.jpg";  // 수정할 이미지 경로
//        File updatedFile = new File(updatedImagePath);
//        FileInputStream updatedInput = new FileInputStream(updatedFile);
//        MockMultipartFile updatedImage = new MockMultipartFile("images", updatedFile.getName(), "image/jpeg", updatedInput);
//
//        // 일기 수정 데이터 준비
//        DiaryCreateDTO updatedDiaryCreateDTO = new DiaryCreateDTO();
//        updatedDiaryCreateDTO.setTitle("Updated Diary");
//        updatedDiaryCreateDTO.setContent("This is the updated content.");
//        updatedDiaryCreateDTO.setDiaryEmotion(Emotion.SAD);
//        updatedDiaryCreateDTO.setCreateDate(LocalDateTime.now());
//        updatedDiaryCreateDTO.setUserId(user.getId());
//
//        // When: 일기 수정 (기존 이미지를 삭제하고 새 이미지를 추가)
//        diaryService.updateDiary(savedDiaryId, updatedDiaryCreateDTO, List.of(updatedImage), List.of(savedImages.get(0).getId()));
//
//        // Then: 수정된 일기 데이터 검증
//        Optional<Diary> updatedDiary = diaryRepository.findById(savedDiaryId);
//        assertThat(updatedDiary).isPresent();
//        assertThat(updatedDiary.get().getTitle()).isEqualTo("Updated Diary");
//        assertThat(updatedDiary.get().getContent()).isEqualTo("This is the updated content.");
//        assertThat(updatedDiary.get().getDiaryEmotion()).isEqualTo(Emotion.SAD);
//
//        // 기존 이미지 파일 삭제되었는지 확인
//        assertThat(savedImageFile.exists()).isFalse();
//
//        // 새 이미지가 제대로 저장되었는지 확인
//        List<DiaryImage> updatedImages = diaryImageRepository.findByDiaryId(savedDiaryId);
//        assertThat(updatedImages).isNotEmpty();
//        File newImageFile = new File("/Users/parkhyunsoo/Desktop/Development/Thesis Project/molla_diary_images/" + updatedImages.get(0).getImagePath());
//        assertTrue(newImageFile.exists());  // 새 이미지 파일이 존재하는지 확인
//    }
//}
