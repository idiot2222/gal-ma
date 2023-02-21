package me.bogeun.galma.service;

import lombok.RequiredArgsConstructor;
import me.bogeun.galma.entity.Account;
import me.bogeun.galma.entity.BoardTopic;
import me.bogeun.galma.entity.Post;
import me.bogeun.galma.payload.HomePostListDto;
import me.bogeun.galma.payload.PostWriteForm;
import me.bogeun.galma.repository.PostRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
@Transactional
@Service
public class PostService {

    private final PostRepository postRepository;
    private final AccountService accountService;

    private final int PAGE_COUNT = 10;


    public void createNewPost(Account account, String topic, PostWriteForm postWriteForm) {
        Post post = Post.builder()
                .writer(account)
                .title(postWriteForm.getTitle())
                .content(postWriteForm.getContent())
                .boardTopic(BoardTopic.valueOf(topic.toUpperCase(Locale.ROOT)))
                .wroteAt(LocalDateTime.now())
                .build();

        postRepository.save(post);
    }

    public List<Post> getPostsByTopicDesc(String topic, int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber-1, PAGE_COUNT, Sort.by(Sort.Direction.DESC, "wroteAt"));

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

    public void addViews(Post post, int views) {
        post.addViews(views);
    }

    public Post getPostOfMatch(LocalDate date) {
        LocalDateTime dateTime = LocalDateTime.of(date, LocalTime.MIN);
        Account admin = accountService.getAccountByUsername("admin");

        return postRepository.findByBoardTopicAndWroteAt(BoardTopic.MATCH, dateTime)
                .orElseGet(() -> {
                    Post post = Post.builder()
                            .title(LocalDate.now().toString())
                            .content(".")
                            .boardTopic(BoardTopic.MATCH)
                            .wroteAt(dateTime)
                            .writer(admin)
                            .build();

                    postRepository.save(post);

                    return post;
                });
    }

    public void updatePost(Long postId, PostWriteForm updateForm) {
        Post post = getPostById(postId);

        post.update(updateForm.getTitle(), updateForm.getContent());

        postRepository.save(post);
    }

    public void deletePost(Long postId) {
        Post post = getPostById(postId);

        postRepository.delete(post);
    }

    public HomePostListDto getHomePostList() {
        HomePostListDto dto = new HomePostListDto();
        LocalDateTime start = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

        dto.setBaseballList(postRepository.findAllTop3ByBoardTopicOrderByWroteAtDesc(BoardTopic.BASEBALL));
        dto.setInfoList(postRepository.findAllTop3ByBoardTopicOrderByWroteAtDesc(BoardTopic.INFO));
        dto.setHumorList(postRepository.findAllTop3ByBoardTopicOrderByWroteAtDesc(BoardTopic.HUMOR));
        dto.setHorrorList(postRepository.findAllTop3ByBoardTopicOrderByWroteAtDesc(BoardTopic.HORROR));
        dto.setTotalList(postRepository.findAllTop7ByWroteAtBetweenAndBoardTopicNotOrderByViewsDesc(start, end, BoardTopic.MATCH));

        return dto;
    }
}
