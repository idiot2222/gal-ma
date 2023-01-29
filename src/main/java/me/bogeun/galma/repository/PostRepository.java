package me.bogeun.galma.repository;

import me.bogeun.galma.entity.BoardTopic;
import me.bogeun.galma.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAllByBoardTopic(BoardTopic topic, Pageable pageable);

    int countByBoardTopic(BoardTopic topic);
}
