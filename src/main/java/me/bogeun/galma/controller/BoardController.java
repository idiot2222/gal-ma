package me.bogeun.galma.controller;

import lombok.RequiredArgsConstructor;
import me.bogeun.galma.entity.Account;
import me.bogeun.galma.entity.BoardTopic;
import me.bogeun.galma.payload.PostCreateForm;
import me.bogeun.galma.service.PostService;
import me.bogeun.galma.utils.CurrentUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Locale;

@RequiredArgsConstructor
@Controller
@RequestMapping("/board")
public class BoardController {

    private final PostService postService;

    @GetMapping("/{topic}")
    public String getBoard(@PathVariable String topic, Model model) {
        model.addAttribute("topic", topic);

        return "board/board-main";
    }

    @GetMapping("/{topic}/post/create")
    public String getPostCreate(@PathVariable String topic, Model model) {
        model.addAttribute("boardTopic", BoardTopic.BASEBALL.koreanValueOf(topic.toUpperCase(Locale.ROOT)
        ));
        model.addAttribute(new PostCreateForm());

        return "board/post-create";
    }

    @PostMapping("/{topic}/post/create")
    public String createPost(@PathVariable String topic, @CurrentUser Account currentAccount,
                             @Valid @ModelAttribute PostCreateForm postCreateForm, Errors errors) {

        if(errors.hasErrors()) {
            return "board/post-create";
        }

        postService.createNewPost(currentAccount, topic, postCreateForm);

        return "redirect:/board/" + topic;
    }

}
