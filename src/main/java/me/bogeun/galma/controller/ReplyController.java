package me.bogeun.galma.controller;

import lombok.RequiredArgsConstructor;
import me.bogeun.galma.entity.Account;
import me.bogeun.galma.service.ReplyService;
import me.bogeun.galma.utils.CurrentUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
@RequestMapping("/reply")
public class ReplyController {

    private final ReplyService replyService;


    @PostMapping("/create/{postId}")
    public String createReply(@PathVariable Long postId, @CurrentUser Account currentAccount,
                              @RequestParam String reply) {
        replyService.createNewReply(postId, currentAccount, reply);

        return "redirect:/board/post/" + postId;
    }

    @PostMapping("/delete/{postId}/{replyId}")
    public String deleteReply(@PathVariable Long postId, @PathVariable Long replyId, @CurrentUser Account currentAccount) {
        replyService.deleteReply(replyId);

        return "redirect:/board/post/" + postId;
    }

    @PostMapping("/create/match")
    public String createMatchReply(@CurrentUser Account currentAccount,
                                   @RequestParam String reply) {
        replyService.createNewMatchReply(currentAccount, reply);

        return "redirect:/match/today";
    }

    @PostMapping("/delete/match/{replyId}")
    public String deleteMatchReply(@PathVariable Long replyId, @CurrentUser Account currentAccount) {
        replyService.deleteReply(replyId);

        return "redirect:/match/today";
    }

}
