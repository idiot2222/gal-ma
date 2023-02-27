package me.bogeun.galma.repository;

import me.bogeun.galma.entity.Batter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BatterRepository extends JpaRepository<Batter, Long> {
    Optional<Batter> findByName(String name);
}
