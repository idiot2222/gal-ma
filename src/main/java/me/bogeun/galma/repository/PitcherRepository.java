package me.bogeun.galma.repository;

import me.bogeun.galma.entity.Pitcher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PitcherRepository extends JpaRepository<Pitcher, Long> {
    List<Pitcher> findAllTop3ByOrderByVotesDesc();
}
