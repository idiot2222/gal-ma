package me.bogeun.galma.repository;

import me.bogeun.galma.entity.Post;
import me.bogeun.galma.entity.Reply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    Page<Reply> findAllByPost(Post post, Pageable pageable);

    List<Reply> findAllByPostOrderByWroteAtDesc(Post post);

    int countByPost(Post post);
}
