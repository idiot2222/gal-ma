package me.bogeun.galma.repository;

import me.bogeun.galma.entity.BoardTopic;
import me.bogeun.galma.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByBoardTopic(BoardTopic topic);
}
