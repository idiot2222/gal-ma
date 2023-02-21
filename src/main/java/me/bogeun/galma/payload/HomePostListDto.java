package me.bogeun.galma.payload;

import lombok.Getter;
import lombok.Setter;
import me.bogeun.galma.entity.Post;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class HomePostListDto {

    List<Post> baseballList = new ArrayList<>();

    List<Post> infoList = new ArrayList<>();

    List<Post> humorList = new ArrayList<>();

    List<Post> horrorList = new ArrayList<>();

    List<Post> totalList = new ArrayList<>();

}
