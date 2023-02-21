package me.bogeun.galma.controller;

import lombok.RequiredArgsConstructor;
import me.bogeun.galma.payload.HomePostListDto;
import me.bogeun.galma.service.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class HomeController {

    private final PostService postService;

    @GetMapping("/")
    public String getIndex(Model model) {
        HomePostListDto listDto = postService.getHomePostList();

        model.addAttribute("lists", listDto);

        return "index";
    }
}
