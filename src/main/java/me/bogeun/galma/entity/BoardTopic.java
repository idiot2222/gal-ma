package me.bogeun.galma.entity;

public enum BoardTopic {
    BASEBALL("야구"), INFO("정보"), HUMOR("유머"), HORROR("공포");

    final String korean;

    BoardTopic(String korean) {
        this.korean = korean;
    }

    public String getKorean() {
        return korean;
    }

}
