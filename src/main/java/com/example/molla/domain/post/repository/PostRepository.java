package com.example.molla.domain.post.repository;

import com.example.molla.domain.post.domain.Post;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PostRepository {

    @PersistenceContext
    EntityManager em;

    public void save(Post post) {
        em.persist(post);
    }

    public Post findByPostId(Long id) {
        return em.find(Post.class, id);
    }

    public List<Post> findByUserId(Long userId) {
        return em.createQuery("select p from Post p where p.user.id = :userId", Post.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    public List<Post> findAll() {
        return em.createQuery("select p from Post p", Post.class).getResultList();
    }
}
