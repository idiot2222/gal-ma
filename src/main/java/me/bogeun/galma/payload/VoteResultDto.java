package me.bogeun.galma.payload;

import lombok.Getter;
import lombok.Setter;
import me.bogeun.galma.entity.Pitcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class VoteResultDto {

    private final List<VoteDto> first = new ArrayList<>(Collections.nCopies(9, new VoteDto()));

    private Pitcher firstPitcher;

    private final List<VoteDto> second = new ArrayList<>(Collections.nCopies(9, new VoteDto()));

    private Pitcher secondPitcher;

    private final List<VoteDto> third = new ArrayList<>(Collections.nCopies(9, new VoteDto()));

    private Pitcher thirdPitcher;

}
