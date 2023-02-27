package me.bogeun.galma.repository;

import me.bogeun.galma.entity.Batter;
import me.bogeun.galma.entity.BatterStat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BatterStatRepository extends JpaRepository<BatterStat, Long> {
    Optional<BatterStat> findByYearAndBatter(int year, Batter batter);
}
