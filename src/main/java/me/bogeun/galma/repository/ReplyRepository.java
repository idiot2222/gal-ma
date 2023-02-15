package me.bogeun.galma.repository;

import me.bogeun.galma.entity.Post;
import me.bogeun.galma.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    List<Reply> findAllByPost(Post post);

    List<Reply> findAllByPostOrderByWroteAtDesc(Post post);
}
