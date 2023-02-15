package me.bogeun.galma.controller;

import lombok.RequiredArgsConstructor;
import me.bogeun.galma.entity.Match;
import me.bogeun.galma.entity.Reply;
import me.bogeun.galma.payload.WeatherDto;
import me.bogeun.galma.service.MatchService;
import me.bogeun.galma.service.ReplyService;
import me.bogeun.galma.utils.WeatherDataSetting;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/match")
public class MatchController {

    private final MatchService matchService;
    private final ReplyService replyService;

    private final WeatherDataSetting weatherDataSetting;

    @GetMapping("/today")
    public String getMatchToday(Model model) {
        Match match = matchService.getTodayMatch();
        List<Reply> replyList = replyService.getRepliesOfMatchByDate(LocalDate.now());
        List<WeatherDto> weather = weatherDataSetting.getWeather(match.getMatchStadium());

        int matchWeatherIdx = getWeatherIdx(match.getMatchDateTime().getHour());
        int nowWeatherIdx = getWeatherIdx(LocalDateTime.now().getHour());

        model.addAttribute(match);
        model.addAttribute("replies", replyList);
        model.addAttribute("matchWeather", weather.get(matchWeatherIdx));
        model.addAttribute("nowWeather", weather.get(nowWeatherIdx));


        return "match/match-today";
    }


    private int getWeatherIdx(int hour) {
        int temp = hour - 3;

        if(temp < 0) {
            temp += 24;
        }

        return temp;
    }

}
