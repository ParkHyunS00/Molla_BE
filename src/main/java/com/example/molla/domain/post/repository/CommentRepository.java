package com.example.molla.domain.post.repository;

import com.example.molla.domain.post.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByPostId(Long postId);

    /**
     * 각 엔티티를 영속성 컨텍스트에 로드한 후 개별 delete 쿼리를 발생시키는 것이 아닌
     * 조건에 맞는 모든 엔티티를 한번에 삭제
     */
    @Modifying // 수정하는 쿼리임을 알림
    @Query("delete from Comment c where c.post.id = :postId")
    void deleteByPostId(@Param("postId") Long postId);
}
