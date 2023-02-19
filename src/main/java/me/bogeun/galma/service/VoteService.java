package me.bogeun.galma.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import me.bogeun.galma.entity.Batter;
import me.bogeun.galma.entity.BatterVote;
import me.bogeun.galma.entity.Pitcher;
import me.bogeun.galma.entity.Position;
import me.bogeun.galma.payload.LineUpVoteForm;
import me.bogeun.galma.payload.VoteDto;
import me.bogeun.galma.payload.VoteResultDto;
import me.bogeun.galma.repository.BatterVoteRepository;
import me.bogeun.galma.repository.PitcherRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class VoteService {

    private final BatterVoteRepository batterVoteRepository;
    private final PitcherRepository pitcherRepository;

    @Transactional
    public void voteAll(LineUpVoteForm voteForm) {
        List<Batter> batters = voteForm.getPlayers();
        Pitcher pitcher = voteForm.getPitcher();
        List<Position> positions = voteForm.getPositions();

        votePitcher(pitcher);
        for (int i = 0; i < 9; i++) {
            Batter batter = batters.get(i);
            Position position = positions.get(i);

            voteBatterByKey(batter, String.valueOf(i + 1));
            voteBatterByKey(batter, position.name());
        }
    }

    private void votePitcher(Pitcher pitcher) {
        pitcher.vote();

        pitcherRepository.save(pitcher);
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

    public VoteResultDto getAllVoteResult() {
        List<BatterVote> all = batterVoteRepository.findAll();
        Map<Batter, PriorityQueue<Vote>> positionMap = new HashMap<>();
        Map<Batter, PriorityQueue<Vote>> seqMap = new HashMap<>();
        Map<Batter, Integer> totalVotes = new HashMap<>();
        List<Pitcher> pitchers = pitcherRepository.findAllTop3ByOrderByVotesDesc();

        settingsAll(all, positionMap, seqMap, totalVotes);

        PriorityQueue<Vote> totalVotesQueue = convertMapToPriorityQueue(totalVotes);

        VoteResultDto voteResultDto = new VoteResultDto();
        getLineUpByVotes(totalVotesQueue, positionMap, seqMap, voteResultDto.getFirst());
        getLineUpByVotes(totalVotesQueue, positionMap, seqMap, voteResultDto.getSecond());
        getLineUpByVotes(totalVotesQueue, positionMap, seqMap, voteResultDto.getThird());
        voteResultDto.setFirstPitcher(pitchers.get(0));
        voteResultDto.setSecondPitcher(pitchers.get(1));
        voteResultDto.setThirdPitcher(pitchers.get(2));

        return voteResultDto;
    }

    private PriorityQueue<Vote> convertMapToPriorityQueue(Map<Batter, Integer> totalVotes) {
        return totalVotes.entrySet().stream()
                .map(x -> new Vote(x.getKey(), x.getValue()))
                .collect(Collectors.toCollection(PriorityQueue::new));
    }

    private void getLineUpByVotes(PriorityQueue<Vote> queue, Map<Batter, PriorityQueue<Vote>> positionMap,
                                           Map<Batter, PriorityQueue<Vote>> seqMap, List<VoteDto> resultList) {
        boolean[] positionBool = new boolean[11];
        boolean[] seqBool = new boolean[10];
        seqBool[0] = positionBool[0] = positionBool[1] = true;

        List<Vote> listNext = new ArrayList<>(); // 이번 투표에서 배정받지 못하는 애들, 후순위에서 가능성이 있기 때문에 다시 담아두기 위함.
        List<Batter> listNoSeq = new ArrayList<>(); // 포지션은 있으나, 적절한 타순을 찾지 못한 애들. 투표받진 못했지만 다른 타순에라도 넣기 위함.
        List<String> positionsNoSeq = new ArrayList<>(); // 포지션은 미리 찾은 애들의 포지션을 저장해두기 위함.
        List<Integer> votesNoSeq = new ArrayList<>(); // 득표 수
        int cnt = 0; // 현재까지 라인업에 자리를 찾은 애들 카운트.

        while(queue.peek() != null) {
            if(cnt == 9) {
                break;
            }

            Vote vote = queue.poll();
            Batter batter = (Batter) vote.keyword;
            int votes = vote.votes;
            String position = getPosition(positionBool, positionMap, batter);
            if(position == null) {
                listNext.add(vote);
                continue;
            }
            int seq = getBattingSeq(seqBool, seqMap, batter);
            if(seq < 0) {
                listNoSeq.add(batter);
                positionsNoSeq.add(position);
                votesNoSeq.add(votes);
                cnt++;
                continue;
            }

            VoteDto voteDto = new VoteDto(batter.getName(), position, votes);
            resultList.set(seq - 1, voteDto);
            cnt++;
        }

        addAllNoSeq(resultList, listNoSeq, positionsNoSeq, votesNoSeq, seqBool);

        queue.addAll(listNext);
    }

    private void addAllNoSeq(List<VoteDto> resultList, List<Batter> listNoSeq, List<String> positionsNoSeq, List<Integer> votesNoSeq, boolean[] seqBool) {
        int len = listNoSeq.size();
        int idx = 0;

        for (int i = 0; i < len; i++) {
            int seq = getNextSeq(seqBool);
            if(seq == -1) {
                return;
            }

            Batter batter = listNoSeq.get(idx);
            String position = positionsNoSeq.get(idx);
            Integer votes = votesNoSeq.get(idx);
            idx++;

            VoteDto voteDto = new VoteDto(batter.getName(), position, votes);
            resultList.set(seq - 1, voteDto);
        }
    }

    private int getNextSeq(boolean[] seqBool) {
        int len = seqBool.length;

        for (int i = 0; i < len; i++) {
            boolean b = seqBool[i];
            if(b) {
                continue;
            }

            seqBool[i] = true;
            return i;
        }

        return -1;
    }

    private int getBattingSeq(boolean[] seqBool, Map<Batter, PriorityQueue<Vote>> seqMap, Batter batter) {
        PriorityQueue<Vote> queue = seqMap.get(batter);
        PriorityQueue<Vote> temp = new PriorityQueue<>();

        while(queue.peek() != null) {
            Vote now = queue.poll();
            int seq = Integer.parseInt((String) now.keyword);

            if (seqBool[seq]) {
                temp.add(now);
                continue;
            }

            seqBool[seq] = true;
            return seq;
        }

        queue.addAll(temp);
        return -1;
    }

    private String getPosition(boolean[] positionBool, Map<Batter, PriorityQueue<Vote>> positionMap, Batter batter) {
        PriorityQueue<Vote> queue = positionMap.get(batter);
        PriorityQueue<Vote> temp = new PriorityQueue<>();

        while(queue.peek() != null) {
            Vote now = queue.poll();
            Position position = Position.valueOf((String) now.keyword);

            if(positionBool[position.getPositionNum()]) {
                temp.add(now);
                continue;
            }

            positionBool[position.getPositionNum()] = true;
            return position.name();
        }

        queue.addAll(temp);
        return null;
    }

    private void settingsAll(List<BatterVote> all, Map<Batter, PriorityQueue<Vote>> positionMap, Map<Batter, PriorityQueue<Vote>> seqMap, Map<Batter, Integer> totalVotes) {
        for (BatterVote batterVote : all) {
            Batter batter = batterVote.getBatter();
            String keyword = batterVote.getKeyword();
            int votes = batterVote.getVoteCount();
            char c = keyword.charAt(0);

            if(c >= '1' && c <= '9') {
                setMap(seqMap, keyword, batter, votes);
            }
            else {
                setMap(positionMap, keyword, batter, votes);
                totalVotes.put(batter, totalVotes.get(batter) + votes); // 포지션 투표 혹은 타순 하나만 총 득표에 반영되도록
            }

            if (!totalVotes.containsKey(batter)) {
                totalVotes.put(batter, 0);
            }
        }
    }

    private void setMap(Map<Batter, PriorityQueue<Vote>> map, String keyword, Batter batter, int votes) {
        if (!map.containsKey(batter)) {
            map.put(batter, new PriorityQueue<>());
        }

        PriorityQueue<Vote> queue = map.get(batter);
        Vote vote = new Vote(keyword, votes);

        queue.add(vote);
    }

    @AllArgsConstructor
    private class Vote implements Comparable<Vote> {

        Object keyword;
        int votes;

        @Override
        public int compareTo(Vote o) {
            return Integer.compare(o.votes, this.votes); // 내림차순
        }
    }

}
