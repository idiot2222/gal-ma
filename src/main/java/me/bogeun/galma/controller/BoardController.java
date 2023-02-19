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

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
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

        List<Post> posts = postService.getPostsByTopicDesc(topic, pageNumber);
        int maxPageNumber = postService.getMaxPageNumber(topic);

        model.addAttribute("boardTopic", BoardTopic.toEnumType(topic));
        model.addAttribute("posts", posts);
        model.addAttribute("currentPageNumber", pageNumber);
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
    public String getPost(HttpServletRequest request,
                          @PathVariable Long postId, Model model,
                          @RequestParam(defaultValue = "1") int replyPage) {
        Post post = postService.getPostById(postId);
        List<Reply> replies = replyService.getRepliesByPost(post, replyPage);
        int maxReplyPageNumber = replyService.getMaxReplyPageNumber(post);

        if (!getReferer(request).equals(request.getServletPath())) {
            postService.addViews(post, 1);
        }

        model.addAttribute(post);
        model.addAttribute("replies", replies);
        model.addAttribute("maxReplyPageNumber", maxReplyPageNumber);

        return "board/post";
    }

    private String getReferer(HttpServletRequest request) {
        URI uri;

        try {
            uri = new URI(request.getHeader("Referer"));
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("잘못된 uri 경로입니다.");
        }

        return uri.getPath();
    }

}
