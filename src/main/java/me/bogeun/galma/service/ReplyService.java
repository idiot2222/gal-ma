package me.bogeun.galma.service;

import lombok.RequiredArgsConstructor;
import me.bogeun.galma.entity.Account;
import me.bogeun.galma.entity.Post;
import me.bogeun.galma.entity.Reply;
import me.bogeun.galma.repository.ReplyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class ReplyService {

    private final ReplyRepository replyRepository;

    private final PostService postService;


    public Reply getReplyById(Long replyId) {
        return replyRepository.findById(replyId).orElseThrow(() -> new IllegalArgumentException("invalid reply id."));
    }

    public List<Reply> getRepliesByPost(Post post) {
        return replyRepository.getAllByPost(post);
    }

    public void createNewReply(Long postId, Account account, String reply) {
        Post post = postService.getPostById(postId);

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
}
