package me.bogeun.galma.service;

import lombok.RequiredArgsConstructor;
import me.bogeun.galma.entity.Account;
import me.bogeun.galma.entity.BoardTopic;
import me.bogeun.galma.entity.Post;
import me.bogeun.galma.payload.PostCreateForm;
import me.bogeun.galma.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
@Transactional
@Service
public class PostService {

    private final PostRepository postRepository;


    public void createNewPost(Account account, String topic, PostCreateForm postCreateForm) {
        Post post = Post.builder()
                .writer(account)
                .title(postCreateForm.getTitle())
                .content(postCreateForm.getContent())
                .boardTopic(BoardTopic.valueOf(topic.toUpperCase(Locale.ROOT)))
                .wroteAt(LocalDateTime.now())
                .build();

        postRepository.save(post);
    }

    public List<Post> getPostsByTopic(String topic) {
        return postRepository.findAllByBoardTopic(BoardTopic.toEnumType(topic));
    }
}
