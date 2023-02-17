package me.bogeun.galma.repository;

import me.bogeun.galma.entity.Batter;
import me.bogeun.galma.entity.BatterVote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BatterVoteRepository extends JpaRepository<BatterVote, Long> {

    Optional<BatterVote> findVoteByBatterAndKeyword(Batter batter, String keyword );

}
