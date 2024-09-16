package com.example.molla.domain.post.controller;

import com.example.molla.common.DeleteResponse;
import com.example.molla.common.PageResponse;
import com.example.molla.common.StandardResponse;
import com.example.molla.domain.post.dto.PostCreateDTO;
import com.example.molla.domain.post.dto.PostDetailResponseDTO;
import com.example.molla.domain.post.dto.PostListResponseDTO;
import com.example.molla.domain.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostRestController {

    private final PostService postService;

    @GetMapping("/detail/{id}")
    public ResponseEntity<StandardResponse<PostDetailResponseDTO>> getDetail(@PathVariable("id") Long postId) {

        PostDetailResponseDTO postDetailResponseDTO = postService.getPostDetail(postId);
        return StandardResponse.ofOk(postDetailResponseDTO);
    }

    @PostMapping("/save")
    public ResponseEntity<StandardResponse<Long>> savePost(@Valid @RequestBody PostCreateDTO postCreateDTO) {

        Long postId = postService.save(postCreateDTO);
        return StandardResponse.of(postId, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<StandardResponse<PageResponse<PostListResponseDTO>>> getAllPosts(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize
    ) {

        PageResponse<PostListResponseDTO> pagePost = postService.findPostList(pageNumber, pageSize);
        return StandardResponse.ofOk(pagePost);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<StandardResponse<DeleteResponse>> deletePost(@PathVariable("id") Long postId) {

        DeleteResponse deleteResponse = postService.deletePost(postId);
        return StandardResponse.ofOk(deleteResponse);
    }
}
