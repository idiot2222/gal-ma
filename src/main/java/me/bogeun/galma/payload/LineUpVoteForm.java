package me.bogeun.galma.payload;

import lombok.Getter;
import lombok.Setter;
import me.bogeun.galma.entity.Batter;
import me.bogeun.galma.entity.Position;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class LineUpVoteForm {

    private List<Batter> players = new ArrayList<>();

    private List<Position> positions = new ArrayList<>();

}
