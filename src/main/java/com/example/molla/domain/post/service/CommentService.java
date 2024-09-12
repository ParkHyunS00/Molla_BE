package com.example.molla.domain.post.service;

import com.example.molla.domain.post.domain.Comment;
import com.example.molla.domain.post.dto.CommentCreateDTO;
import com.example.molla.domain.post.repository.CommentRepository;
import com.example.molla.domain.post.domain.Post;
import com.example.molla.domain.post.repository.PostRepository;
import com.example.molla.domain.user.domain.User;
import com.example.molla.domain.user.repository.UserRepository;
import com.example.molla.exception.BusinessException;
import com.example.molla.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional
    public Long save(CommentCreateDTO commentCreateDTO) {

        User user = userRepository.findById(commentCreateDTO.getUserId())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Post post = postRepository.findById(commentCreateDTO.getPostId())
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        Comment comment = commentCreateDTO.toEntity(user, post, LocalDateTime.now());

        Comment savedComment = commentRepository.save(comment);
        return savedComment.getId();
    }
}
