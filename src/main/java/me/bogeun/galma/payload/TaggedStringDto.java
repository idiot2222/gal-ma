package me.bogeun.galma.payload;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.bogeun.galma.entity.Player;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class TaggedStringDto {

    private final String content;

    private final List<Player> playerList;

    private final List<String> formatStatList;

}
