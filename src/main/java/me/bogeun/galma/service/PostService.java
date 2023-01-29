package me.bogeun.galma.service;

import lombok.RequiredArgsConstructor;
import me.bogeun.galma.entity.Account;
import me.bogeun.galma.entity.BoardTopic;
import me.bogeun.galma.entity.Post;
import me.bogeun.galma.payload.PostCreateForm;
import me.bogeun.galma.repository.PostRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    private final int PAGE_COUNT = 10;


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

    public List<Post> getPostsByTopic(String topic, int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber-1, PAGE_COUNT);

        return postRepository.findAllByBoardTopic(BoardTopic.toEnumType(topic), pageable).getContent();
    }

    public Post getPostById(Long postId) {
        return postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("invalid post id."));
    }

    public int getMaxPageNumber(String topic) {
        int count = postRepository.countByBoardTopic(BoardTopic.toEnumType(topic));
        if(count == 0) {
            return 1;
        }

        return count / PAGE_COUNT + (count % PAGE_COUNT > 0 ? 1 : 0);
    }
}
