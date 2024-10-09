package com.example.molla.domain.post.service;

import com.example.molla.common.DeleteResponse;
import com.example.molla.common.PageResponse;
import com.example.molla.common.UpdateResponse;
import com.example.molla.domain.post.domain.Comment;
import com.example.molla.domain.post.dto.CommentResponseDTO;
import com.example.molla.domain.post.repository.CommentRepository;
import com.example.molla.domain.common.Emotion;
import com.example.molla.domain.diary.repository.DiaryRepository;
import com.example.molla.domain.post.domain.Post;
import com.example.molla.domain.post.dto.PostCreateDTO;
import com.example.molla.domain.post.dto.PostDetailResponseDTO;
import com.example.molla.domain.post.dto.PostListResponseDTO;
import com.example.molla.domain.post.repository.PostRepository;
import com.example.molla.domain.user.domain.User;
import com.example.molla.domain.user.repository.UserRepository;
import com.example.molla.exception.BusinessException;
import com.example.molla.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final DiaryRepository diaryRepository;
    private final CommentRepository commentRepository;

    /**
     * 1. 존재하는 사용자인지 판별
     * 2. 사용자가 작성한 일기에 따른 최근 7일간의 주된 감정과 그 갯수 조회
     * 3. 감정과 갯수를 포함해 Post 엔티티 생성 후 저장
     * 4. 사용자가 최근 7일간 일기를 작성하지 않은 경우 NOTHING 감정이 저장됨
     * @return post.id
     */
    // TODO : 감정 분석 비동기 처리 후 따로 저장
    @Transactional
    public Long save(PostCreateDTO postCreateDTO) {
        User user = userRepository.findById(postCreateDTO.getUserId())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        LocalDateTime startDate = LocalDateTime.now().minusDays(7);
        LocalDateTime endDate = LocalDateTime.now();

        List<Object[]> userEmotionAndCount = diaryRepository.findPrimaryEmotionByUserFor7days(user.getId(), startDate, endDate);

        // 사용자가 최근 7일간 일기를 작성하지 않은 경우
        Emotion primaryUserEmotion = Emotion.NOTHING;
        Long emotionCount = 0L;

        if (!userEmotionAndCount.isEmpty()) {
            Object[] result = userEmotionAndCount.get(0);
            primaryUserEmotion = (Emotion) result[0];
            emotionCount = (Long) result[1];
        }

        Post post = postCreateDTO.toEntity(LocalDateTime.now(), primaryUserEmotion, emotionCount, user);
        postRepository.save(post);

        return post.getId();
    }

    // 모든 게시글 목록 조회
    public PageResponse<PostListResponseDTO> findPostList(int pageNumber, int pageSize) {

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<PostListResponseDTO> postPage = postRepository.findPostList(pageable);

        List<PostListResponseDTO> content = postPage.getContent();

        return PageResponse.<PostListResponseDTO>builder()
                .content(content)
                .totalPage(postPage.getTotalPages())
                .totalElement(postPage.getTotalElements())
                .currentPage(postPage.getNumber())
                .isLast(postPage.isLast())
                .build();
    }

    // 특정 게시글 수정
    @Transactional
    public UpdateResponse updatePost(Long postId, PostCreateDTO postCreateDTO) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        post.updatePost(postCreateDTO.getTitle(), postCreateDTO.getContent(), postCreateDTO.getPostEmotion());
        return new UpdateResponse(post.getId(), "게시글");
    }

    @Transactional
    public void updatePostEmotion(Long postId, Emotion postEmotion) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        post.setPostEmotion(postEmotion);
    }

    // 특정 게시글 삭제
    @Transactional
    public DeleteResponse deletePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        commentRepository.deleteByPostId(postId);
        postRepository.delete(post);

        return new DeleteResponse(post.getId(), "게시글");
    }

    // 게시글 상세보기 조회
    public PostDetailResponseDTO getPostDetail(Long postId) {
        Post post = postRepository.findPostAndUserById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        List<Comment> comments = commentRepository.findByPostId(postId); // 게시글에 담긴 댓글 조회

        List<CommentResponseDTO> responseComments = comments.stream()
                .map(CommentResponseDTO::fromEntity)
                .toList();

        return PostDetailResponseDTO.builder()
                .title(post.getTitle())
                .content(post.getContent())
                .postEmotion(post.getPostEmotion())
                .userEmotion(post.getUserEmotion())
                .userEmotionCount(post.getUserEmotionCount())
                .username(post.getUser().getUsername())
                .createDate(post.getCreateDate())
                .comments(responseComments)
                .build();
    }
}
