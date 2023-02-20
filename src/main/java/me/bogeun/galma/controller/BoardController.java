package me.bogeun.galma.controller;

import lombok.RequiredArgsConstructor;
import me.bogeun.galma.entity.Account;
import me.bogeun.galma.entity.BoardTopic;
import me.bogeun.galma.entity.Post;
import me.bogeun.galma.entity.Reply;
import me.bogeun.galma.mapper.PostMapper;
import me.bogeun.galma.payload.PostWriteForm;
import me.bogeun.galma.service.PostService;
import me.bogeun.galma.service.ReplyService;
import me.bogeun.galma.utils.CurrentUser;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Controller
@RequestMapping("/board")
public class BoardController {

    private final PostService postService;
    private final ReplyService replyService;

    private final PostMapper postMapper;


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
        model.addAttribute(new PostWriteForm());

        return "board/post-create";
    }

    @PostMapping("/{topic}/post/create")
    public String createPost(@PathVariable String topic, @CurrentUser Account currentAccount, Model model,
                             @Valid @ModelAttribute PostWriteForm postWriteForm, Errors errors) {
        model.addAttribute("boardTopic", BoardTopic.valueOf(topic));
        if(errors.hasErrors()) {
            return "board/post-create";
        }

        postService.createNewPost(currentAccount, topic, postWriteForm);

        return "redirect:/board/" + topic + "/1";
    }

    @GetMapping("/post/{postId}")
    public String getPost(HttpServletRequest request, @CurrentUser Account currentAccount,
                          @PathVariable Long postId, Model model,
                          @RequestParam(defaultValue = "1") int replyPage) {
        Post post = postService.getPostById(postId);
        Account writer = post.getWriter();
        List<Reply> replies = replyService.getRepliesByPost(post, replyPage);
        int maxReplyPageNumber = replyService.getMaxReplyPageNumber(post);

        if (!getReferer(request).equals(request.getServletPath())) {
            postService.addViews(post, 1);
        }

        model.addAttribute(post);
        model.addAttribute("replies", replies);
        model.addAttribute("maxReplyPageNumber", maxReplyPageNumber);
        model.addAttribute("isOwner", Objects.equals(currentAccount.getId(), writer.getId()));

        return "board/post";
    }

    @GetMapping("/post/update/{postId}")
    public String getPostUpdate(@PathVariable Long postId, @CurrentUser Account currentAccount,
                                Model model) {
        Post post = postService.getPostById(postId);
        PostWriteForm createForm = postMapper.entityToWriteForm(post);
        if (!post.getWriter().equals(currentAccount)) {
            throw new BadCredentialsException("have no access.");
        }

        model.addAttribute("postId", postId);
        model.addAttribute("boardTopic", post.getBoardTopic());
        model.addAttribute("postForm", createForm);

        return "board/post-update";
    }

    @PostMapping("/post/update/{postId}")
    public String postUpdate(@PathVariable Long postId, @ModelAttribute PostWriteForm updateForm) {
        postService.updatePost(postId, updateForm);

        return "redirect:/board/post/" + postId;
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
