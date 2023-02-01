package me.bogeun.galma.controller;

import lombok.RequiredArgsConstructor;
import me.bogeun.galma.entity.Account;
import me.bogeun.galma.entity.BoardTopic;
import me.bogeun.galma.entity.Post;
import me.bogeun.galma.entity.Reply;
import me.bogeun.galma.payload.PostCreateForm;
import me.bogeun.galma.service.PostService;
import me.bogeun.galma.service.ReplyService;
import me.bogeun.galma.utils.CurrentUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/board")
public class BoardController {

    private final PostService postService;
    private final ReplyService replyService;


    @GetMapping("/{topic}/{pageNumber}")
    public String getBoard(@PathVariable String topic, Model model,
                           @PathVariable int pageNumber) {

        List<Post> posts = postService.getPostsByTopic(topic, pageNumber);
        int maxPageNumber = postService.getMaxPageNumber(topic);

        model.addAttribute("boardTopic", BoardTopic.toEnumType(topic));
        model.addAttribute("posts", posts);
        model.addAttribute("maxPageNumber", maxPageNumber);

        return "board/board-main";
    }

    @GetMapping("/{topic}/post/create")
    public String getPostCreate(@PathVariable String topic, Model model) {
        model.addAttribute("boardTopic", BoardTopic.toEnumType(topic));
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

        return "redirect:/board/" + topic + "/1";
    }

    @GetMapping("/post/{postId}")
    public String getPost(@PathVariable Long postId, Model model) {
        Post post = postService.getPostById(postId);
        List<Reply> replies = replyService.getRepliesByPost(post);

        postService.addViews(post, 1);

        model.addAttribute(post);
        model.addAttribute("replies", replies);

        return "board/post";
    }

}
