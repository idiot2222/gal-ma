package me.bogeun.galma.controller;

import lombok.RequiredArgsConstructor;
import me.bogeun.galma.entity.Match;
import me.bogeun.galma.payload.WeatherDto;
import me.bogeun.galma.service.MatchService;
import me.bogeun.galma.utils.WeatherDataSetting;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/match")
public class MatchController {

    private final MatchService matchService;
    private final WeatherDataSetting weatherDataSetting;

    @GetMapping("/today")
    public String getMatchToday(Model model) {
        Match match = matchService.getTodayMatch();
        List<WeatherDto> weather = weatherDataSetting.getWeather(match.getMatchStadium());

        model.addAttribute(match);
        model.addAttribute("matchWeather", weather.get(match.getMatchDateTime().getHour() - 3));
        model.addAttribute("nowWeather", weather.get(LocalDateTime.now().getHour() - 3));


        return "match/match-today";
    }

}
