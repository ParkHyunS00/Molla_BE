package com.example.molla;

import com.example.molla.domain.common.Emotion;
import com.example.molla.domain.post.domain.Post;
import com.example.molla.domain.user.domain.User;
import com.example.molla.domain.post.repository.PostRepository;
import com.example.molla.domain.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class PostRepositoryTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("사용자 ID로 작성된 게시글 조회하기")
    public void testFindPostByUserId() {
        User user = new User("Park", "park@gmail.com", "1234");
        userRepository.save(user);

        Post post1 = new Post();
        post1.setTitle("first post");
        post1.setContent("first post");
        post1.setPostEmotion(Emotion.SAD);
        post1.setUser(user);
        post1.setUserEmotion(Emotion.SAD);
        postRepository.save(post1);

        Post post2 = new Post();
        post2.setTitle("second post");
        post2.setContent("second post");
        post2.setPostEmotion(Emotion.HAPPY);
        post2.setUser(user);
        post2.setUserEmotion(Emotion.HAPPY);
        postRepository.save(post2);

        List<Post> posts = postRepository.findByUserId(user.getId());

        Assertions.assertThat(posts).hasSize(2);
    }
}
