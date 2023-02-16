package me.bogeun.galma.controller;

import lombok.RequiredArgsConstructor;
import me.bogeun.galma.entity.*;
import me.bogeun.galma.payload.WeatherDto;
import me.bogeun.galma.service.MatchService;
import me.bogeun.galma.service.PlayerService;
import me.bogeun.galma.service.ReplyService;
import me.bogeun.galma.utils.WeatherDataSetting;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Controller
@RequestMapping("/match")
public class MatchController {

    private final MatchService matchService;
    private final ReplyService replyService;
    private final PlayerService playerService;

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
        model.addAttribute("homeRate", calRate(match.getOurVotes(), match.getOpponentVotes()));
        model.addAttribute("awayRate", calRate(match.getOpponentVotes(), match.getOurVotes()));


        return "match/match-today";
    }

    @ResponseBody
    @PostMapping("/vote")
    public int voteWinPrediction(@RequestBody Map<String, Object> map) {
        return matchService.voteWinPrediction((boolean) map.get("isHome"));
    }

    @GetMapping("/lineup")
    public String getLineUp() {
        return "match/match-lineup";
    }

    @GetMapping("/lineup/vote")
    public String getLineUpVote(Model model) {
        List<Batter> batters = playerService.getBatterList();
        List<Pitcher> pitchers = playerService.getPitcherList();

        model.addAttribute("positionList", Position.values());
        model.addAttribute("batterList", batters);
        model.addAttribute("pitcherList", pitchers);

        return "match/match-lineup-vote";
    }




    private long calRate(double win, double lose) {
        if(win + lose == 0) {
            return 50;
        }

        return Math.round(win / (win + lose) * 100);
    }


    private int getWeatherIdx(int hour) {
        int temp = hour - 3;

        if(temp < 0) {
            temp += 24;
        }

        return temp;
    }

}
