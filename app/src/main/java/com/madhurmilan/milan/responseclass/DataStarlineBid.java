package com.madhurmilan.milan.responseclass;

public class DataStarlineBid {
    String game_id,game_type,bid_points,digit,panna;

    public DataStarlineBid(String game_id, String game_type, String bid_points, String digit, String panna) {
        this.game_id = game_id;
        this.game_type = game_type;
        this.bid_points = bid_points;
        this.digit = digit;
        this.panna = panna;
    }

    public String getGame_id() {
        return game_id;
    }

    public String getGame_type() {
        return game_type;
    }

    public String getBid_points() {
        return bid_points;
    }

    public String getDigit() {
        return digit;
    }

    public String getPanna() {
        return panna;
    }
}
