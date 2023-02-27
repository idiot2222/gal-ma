package me.bogeun.galma.repository;

import me.bogeun.galma.entity.Pitcher;
import me.bogeun.galma.entity.PitcherStat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PitcherStatRepository extends JpaRepository<PitcherStat, Long> {
    Optional<PitcherStat> findByYearAndPitcher(int year, Pitcher pitcher);
}
