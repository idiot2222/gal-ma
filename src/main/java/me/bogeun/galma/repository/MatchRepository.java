package me.bogeun.galma.repository;

import me.bogeun.galma.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface MatchRepository extends JpaRepository<Match, Long> {

    Optional<Match> findByMatchDateTimeBetween(LocalDateTime start, LocalDateTime end);

}
