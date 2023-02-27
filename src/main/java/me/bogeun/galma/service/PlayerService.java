package me.bogeun.galma.service;

import lombok.RequiredArgsConstructor;
import me.bogeun.galma.entity.*;
import me.bogeun.galma.repository.BatterRepository;
import me.bogeun.galma.repository.BatterStatRepository;
import me.bogeun.galma.repository.PitcherRepository;
import me.bogeun.galma.repository.PitcherStatRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PlayerService {

    private final BatterRepository batterRepository;
    private final PitcherRepository pitcherRepository;
    private final BatterStatRepository batterStatRepository;
    private final PitcherStatRepository pitcherStatRepository;

    public List<Batter> getBatterList() {
        return batterRepository.findAll();
    }

    public List<Pitcher> getPitcherList() {
        return pitcherRepository.findAll();
    }

    public Batter getBatterById(Long batterId) {
        return batterRepository.findById(batterId).orElseThrow(() -> new IllegalArgumentException("invalid batter id."));
    }

    public Player getPlayerByName(String playerName) {
        Player player = pitcherRepository.findByName(playerName).orElse(null);

        if (player == null) {
            player = batterRepository.findByName(playerName).orElse(null);
        }

        return player;
    }

    public PitcherStat getPitcherStat(int year, Pitcher pitcher) {
        return pitcherStatRepository.findByYearAndPitcher(year, pitcher).orElse(null);
    }

    public BatterStat getBatterStat(int year, Batter batter) {
        return batterStatRepository.findByYearAndBatter(year, batter).orElse(null);
    }

    public Pitcher getPitcherById(Long pitcherId) {
        return pitcherRepository.findById(pitcherId).orElseThrow();
    }
}
