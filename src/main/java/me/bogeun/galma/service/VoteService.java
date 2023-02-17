package me.bogeun.galma.service;

import lombok.RequiredArgsConstructor;
import me.bogeun.galma.entity.Batter;
import me.bogeun.galma.entity.BatterVote;
import me.bogeun.galma.entity.Position;
import me.bogeun.galma.payload.LineUpVoteForm;
import me.bogeun.galma.repository.BatterVoteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class VoteService {

    private final BatterVoteRepository batterVoteRepository;

    @Transactional
    public void voteBatterAll(LineUpVoteForm voteForm) {
        List<Batter> batters = voteForm.getPlayers();
        List<Position> positions = voteForm.getPositions();

        for (int i = 0; i < 9; i++) {
            Batter batter = batters.get(i);
            Position position = positions.get(i);

            voteBatterByKey(batter, String.valueOf(i + 1));
            voteBatterByKey(batter, position.name());
        }
    }

    private void voteBatterByKey(Batter batter, String keyword) {
        BatterVote batterVote = batterVoteRepository.findVoteByBatterAndKeyword(batter, keyword)
                .orElseGet(() -> BatterVote.builder()
                        .batter(batter)
                        .voteCount(0)
                        .keyword(keyword)
                        .build());

        batterVote.vote();
        batterVoteRepository.save(batterVote);
    }

}
