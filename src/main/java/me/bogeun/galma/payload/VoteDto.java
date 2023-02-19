package me.bogeun.galma.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VoteDto {

    private String name = "-";

    private String position = "-";

    private int votes;

    private String hand;

}
