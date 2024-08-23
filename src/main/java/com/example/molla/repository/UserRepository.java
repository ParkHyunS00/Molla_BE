package com.example.molla.repository;

import com.example.molla.domain.Post;
import com.example.molla.domain.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

    @PersistenceContext
    EntityManager em;

    public void save(User user) {
        em.persist(user);
    }
}
