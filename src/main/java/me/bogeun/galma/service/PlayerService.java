package me.bogeun.galma.service;

import lombok.RequiredArgsConstructor;
import me.bogeun.galma.entity.Batter;
import me.bogeun.galma.entity.Pitcher;
import me.bogeun.galma.repository.BatterRepository;
import me.bogeun.galma.repository.PitcherRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PlayerService {

    private final BatterRepository batterRepository;
    private final PitcherRepository pitcherRepository;

    public List<Batter> getBatterList() {
        return batterRepository.findAll();
    }

    public List<Pitcher> getPitcherList() {
        return pitcherRepository.findAll();
    }

}
