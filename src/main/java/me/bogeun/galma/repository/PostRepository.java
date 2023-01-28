package me.bogeun.galma.repository;

import me.bogeun.galma.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
