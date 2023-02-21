package me.bogeun.galma.repository;

import me.bogeun.galma.entity.BoardTopic;
import me.bogeun.galma.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAllByBoardTopic(BoardTopic topic, Pageable pageable);

    int countByBoardTopic(BoardTopic topic);

    Optional<Post> findByBoardTopicAndWroteAt(BoardTopic topic, LocalDateTime wroteAt);

    List<Post> findAllTop3ByBoardTopicOrderByWroteAtDesc(BoardTopic boardTopic);

    List<Post> findAllTop7ByWroteAtBetweenAndBoardTopicNotOrderByViewsDesc(LocalDateTime start, LocalDateTime end, BoardTopic boardTopic);

}
