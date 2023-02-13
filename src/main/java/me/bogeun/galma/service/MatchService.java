package me.bogeun.galma.service;

import lombok.RequiredArgsConstructor;
import me.bogeun.galma.entity.Match;
import me.bogeun.galma.payload.Stadium;
import me.bogeun.galma.repository.MatchRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@RequiredArgsConstructor
@Service
public class MatchService {

    private final MatchRepository matchRepository;

    public Match getTodayMatch() {
        LocalDateTime startOfToday = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime endOfToday = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

        return matchRepository.findByMatchDateTimeBetween(startOfToday, endOfToday)
                .orElse(Match.builder()
                        .matchStadium(Stadium.SJ)
                        .build());
    }

}
