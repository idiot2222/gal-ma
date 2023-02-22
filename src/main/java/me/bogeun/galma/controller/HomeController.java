package me.bogeun.galma.controller;

import lombok.RequiredArgsConstructor;
import me.bogeun.galma.entity.Match;
import me.bogeun.galma.payload.HomePostListDto;
import me.bogeun.galma.payload.WeatherDto;
import me.bogeun.galma.service.MatchService;
import me.bogeun.galma.service.PostService;
import me.bogeun.galma.utils.WeatherDataSetting;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class HomeController {

    private final PostService postService;
    private final MatchService matchService;

    private final WeatherDataSetting weatherDataSetting;

    @GetMapping("/")
    public String getIndex(Model model) {
        HomePostListDto listDto = postService.getHomePostList();
        Match todayMatch = matchService.getTodayMatch();

        List<WeatherDto> weather = weatherDataSetting.getWeather(todayMatch.getMatchStadium());

        model.addAttribute("lists", listDto);
        model.addAttribute("match", todayMatch);
        model.addAttribute("weather", weather);

        return "index";
    }
}
