package com.example.molla.domain.post.controller;

import com.example.molla.common.StandardResponse;
import com.example.molla.domain.post.dto.CommentCreateDTO;
import com.example.molla.domain.post.dto.CommentResponseDTO;
import com.example.molla.domain.post.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/post/comment")
@RequiredArgsConstructor
public class CommentRestController {

    private final CommentService commentService;

    @PostMapping("/save")
    public ResponseEntity<StandardResponse<Long>> saveComment(@Valid @RequestBody CommentCreateDTO commentCreateDTO) {

        Long commentId = commentService.save(commentCreateDTO);
        return StandardResponse.of(commentId, HttpStatus.CREATED);
    }
}
