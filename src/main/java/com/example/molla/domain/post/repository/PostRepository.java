package com.example.molla.domain.post.repository;

import com.example.molla.domain.post.domain.Post;
import com.example.molla.domain.post.dto.PostListResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    /**
     * 게시글 목록에 포함되는 내용 -> 게시글 pk(상세 조회용), 제목, 내용, 게시글 감정,
     * 작성시 최근 7일간 사용자 감정, 그 갯수, 댓글 수, 작성자 이름
     * 가장 최근 순으로 조회
     * @return PostListResponseDTO List
     */
    @Query("select new com.example.molla.domain.post.dto.PostListResponseDTO(p.id, p.title, p.content, p.userEmotion, p.userEmotionCount, COUNT(c.id), p.user.username, p.createDate)" +
            " from Post p left join Comment c on p.id = c.post.id" +
            " group by p.id, p.title, p.content, p.userEmotion, p.userEmotionCount, p.user.id" +
            " order by p.createDate desc")
    List<PostListResponseDTO> findPostList();

    /**
     * 게시글 상세 조회 시 사용자 조회 쿼리가 한 번 더 발생하는 것을
     * 방지하기 위해 fetch join 사용
     */
    @Query("select p from Post p join fetch p.user where p.id = :postId")
    Optional<Post> findPostAndUserById(@Param("postId") Long postId);
}
