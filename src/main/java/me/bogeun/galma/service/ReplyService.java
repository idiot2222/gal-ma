package me.bogeun.galma.service;

import lombok.RequiredArgsConstructor;
import me.bogeun.galma.entity.Account;
import me.bogeun.galma.entity.Post;
import me.bogeun.galma.entity.Reply;
import me.bogeun.galma.repository.ReplyRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class ReplyService {

    private final ReplyRepository replyRepository;

    private final PostService postService;

    private final int PAGE_COUNT = 10;


    public Reply getReplyById(Long replyId) {
        return replyRepository.findById(replyId).orElseThrow(() -> new IllegalArgumentException("invalid reply id."));
    }

    public List<Reply> getRepliesByPost(Post post, int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber-1, PAGE_COUNT, Sort.by(Sort.Direction.ASC, "wroteAt"));

        return replyRepository.findAllByPost(post, pageable).getContent();
    }

    public List<Reply> getRepliesByPostDesc(Post post) {
        return replyRepository.findAllByPostOrderByWroteAtDesc(post);
    }

    public void createNewReply(Post post, Account account, String reply) {
        Reply createdReply = Reply.builder()
                .content(reply)
                .wroteAt(LocalDateTime.now())
                .account(account)
                .post(post)
                .build();

        replyRepository.save(createdReply);
    }

    public void deleteReply(Long replyId) {
        Reply reply = getReplyById(replyId);
        replyRepository.delete(reply);
    }

    public List<Reply> getRepliesOfMatchByDate(LocalDate date) {
        Post post = postService.getPostOfMatch(date);

        return getRepliesByPostDesc(post);
    }

    public void createNewMatchReply(Account account, String reply) {
        Post post = postService.getPostOfMatch(LocalDate.now());

        createNewReply(post, account, reply);
    }

    public int getMaxReplyPageNumber(Post post) {
        int count = replyRepository.countByPost(post);
        if(count == 0) {
            return 1;
        }

        return count / PAGE_COUNT + (count % PAGE_COUNT > 0 ? 1 : 0);
    }
}
