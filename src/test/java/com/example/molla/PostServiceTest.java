package com.example.molla;

import com.example.molla.domain.comment.repository.CommentRepository;
import com.example.molla.domain.common.Emotion;
import com.example.molla.domain.diary.repository.DiaryRepository;
import com.example.molla.domain.post.domain.Post;
import com.example.molla.domain.post.dto.PostCreateDTO;
import com.example.molla.domain.post.dto.PostListResponseDTO;
import com.example.molla.domain.post.repository.PostRepository;
import com.example.molla.domain.post.service.PostService;
import com.example.molla.domain.user.domain.User;
import com.example.molla.domain.user.repository.UserRepository;
import com.example.molla.exception.BusinessException;
import com.example.molla.exception.ErrorCode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import com.example.molla.domain.diary.domain.Diary;
import com.example.molla.domain.comment.domain.Comment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class PostServiceTest {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DiaryRepository diaryRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostService postService;

    private User user;

    private Post post1, post2;

    @BeforeEach
    void setUp() {

        // 테스트용 사용자 생성
        user = new User();
        user.setEmail("test@test.com");
        user.setPassword("123456");
        user.setUsername("testUser");
        userRepository.save(user);

        // 테스트용 일기 데이터 생성
        createDiaryEntry("Title 1", "Content 1", Emotion.ANXIOUS, LocalDateTime.now().minusDays(1));
        createDiaryEntry("Title 2", "Content 2", Emotion.ANXIOUS, LocalDateTime.now().minusDays(2));
        createDiaryEntry("Title 3", "Content 3", Emotion.SAD, LocalDateTime.now().minusDays(3));
        createDiaryEntry("Title 4", "Content 4", Emotion.ANGRY, LocalDateTime.now().minusDays(4));

        // 테스트용 게시글 데이터
        PostCreateDTO postCreateDTO = new PostCreateDTO();
        postCreateDTO.setTitle("Test Post");
        postCreateDTO.setContent("This is a test post.");
        postCreateDTO.setPostEmotion(Emotion.HAPPY);
        postCreateDTO.setUserId(user.getId());

        PostCreateDTO postCreateDTO2 = new PostCreateDTO();
        postCreateDTO2.setTitle("Test2 Post");
        postCreateDTO2.setContent("This is a test post.2");
        postCreateDTO2.setPostEmotion(Emotion.HAPPY);
        postCreateDTO2.setUserId(user.getId());

        Long postId = postService.save(postCreateDTO);
        Long postId2 = postService.save(postCreateDTO2);

        post1 = postRepository.findById(postId).orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));
        post2 = postRepository.findById(postId2).orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));


        createTestPostAndComments();
    }

    private void createTestPostAndComments() {
        // 댓글 생성
        Comment comment1 = new Comment("First comment", user, post1);
        Comment comment2 = new Comment("Second comment", user, post1);
        Comment comment3 = new Comment("Third comment", user, post2);

        commentRepository.save(comment1);
        commentRepository.save(comment2);
        commentRepository.save(comment3);
    }

    private void createDiaryEntry(String title, String content, Emotion emotion, LocalDateTime createDate) {
        Diary diary = new Diary();
        diary.setTitle(title);
        diary.setContent(content);
        diary.setDiaryEmotion(emotion);
        diary.setUser(user);
        diary.setCreateDate(createDate);
        diaryRepository.save(diary);
    }

    @Test
    @DisplayName("게시글 저장 시 사용자의 최근 7일간 감정 중 가장 많이 나타난 감정을 저장")
    void testSave_PostCreationWithPrimaryEmotion() {
        // Given: PostCreateDTO를 생성
        PostCreateDTO postCreateDTO = new PostCreateDTO();
        postCreateDTO.setTitle("Test Post");
        postCreateDTO.setContent("This is a test post.");
        postCreateDTO.setPostEmotion(Emotion.HAPPY);
        postCreateDTO.setUserId(user.getId());

        // When: Post를 저장
        Long postId = postService.save(postCreateDTO);

        // Then: Post가 정상적으로 저장되고, 주된 감정이 HAPPY(감정의 빈도가 가장 높음)이어야 함
        Post savedPost = postRepository.findById(postId).orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        Assertions.assertThat(savedPost).isNotNull();
        Assertions.assertThat(savedPost.getUserEmotion()).isEqualTo(Emotion.HAPPY);
        Assertions.assertThat(savedPost.getUserEmotionCount()).isEqualTo(2L); // HAPPY가 2번 등장
    }

    @Test
    @DisplayName("게시글 저장 시 사용자가 최근 7일간 일기를 작성하지 않은 경우 NOTHING 감정 저장")
    void testSave_PostCreationWithNoDiaries() {


        // Given: 새로운 사용자를 생성 (일기가 없는 상태)
        User newUser = new User();
        newUser.setEmail("newuser@test.com");
        newUser.setPassword("123456");
        newUser.setUsername("newUser");
        userRepository.save(newUser);

        PostCreateDTO postCreateDTO = new PostCreateDTO();
        postCreateDTO.setTitle("Test Post");
        postCreateDTO.setContent("This is a test post.");
        postCreateDTO.setUserId(newUser.getId());

        // When: Post를 저장
        Long postId = postService.save(postCreateDTO);

        // Then: Post가 정상적으로 저장되고, 주된 감정이 NOTHING
        Post savedPost = postRepository.findById(postId).orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        assertThat(savedPost).isNotNull();
        assertThat(savedPost.getUserEmotion()).isEqualTo(Emotion.NOTHING);
        assertThat(savedPost.getUserEmotionCount()).isEqualTo(0L); // 감정이 없기 때문에 0
    }


    @Test
    @DisplayName("게시글 리스트 조회")
    void testFindAllPostsWithCommentCountAndUserId() {
        // When: 게시글 목록 조회
        List<PostListResponseDTO> postDTOList = postService.findPostList();

        // Then: 게시글 목록에 댓글 개수 및 작성자 ID가 제대로 조회되는지 확인
        Assertions.assertThat(postDTOList).isNotEmpty();
        assertThat(postDTOList.size()).isEqualTo(2);

        // 첫 번째 게시글 검증 (최신 글)
        PostListResponseDTO post1 = postDTOList.get(0);
        assertThat(post1.getTitle()).isEqualTo("Test2 Post"); // Test2 Post가 더 최신
        assertThat(post1.getCommentCount()).isEqualTo(1L);
        assertThat(post1.getUsername()).isEqualTo(user.getUsername());
        System.out.println(post1.getUserEmotion());

        // 두 번째 게시글 검증
        PostListResponseDTO post2 = postDTOList.get(1);
        assertThat(post2.getTitle()).isEqualTo("Test Post");
        assertThat(post2.getCommentCount()).isEqualTo(2L);
        assertThat(post2.getUsername()).isEqualTo(user.getUsername());
        System.out.println(post2.getUserEmotion());
    }

    @Test
    @DisplayName("게시글 삭제 테스트")
    void testDeletePost() {
        // Given: 게시글이 존재함
        PostCreateDTO postCreateDTO = new PostCreateDTO();
        postCreateDTO.setTitle("Test Post for Deletion");
        postCreateDTO.setContent("This post will be deleted.");
        postCreateDTO.setPostEmotion(Emotion.SAD);
        postCreateDTO.setUserId(user.getId());

        Long postId = postService.save(postCreateDTO);

        Optional<Post> savedPost = postRepository.findById(postId);
        Assertions.assertThat(savedPost).isPresent(); // 게시글 존재

        // When: 게시글을 삭제
        postService.deletePost(postId);

        // Then: 게시글이 삭제되었는지 확인
        Optional<Post> deletedPost = postRepository.findById(postId);
        Assertions.assertThat(deletedPost).isEmpty(); // 삭제 후 게시글이 없음
    }
}
