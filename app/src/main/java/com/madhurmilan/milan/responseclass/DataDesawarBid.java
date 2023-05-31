package com.madhurmilan.milan.responseclass;

public class DataDesawarBid {
    String game_id,game_type,bid_points, left_digit, right_digit;

    public DataDesawarBid(String game_id, String game_type, String bid_points, String left_digit, String right_digit) {
        this.game_id = game_id;
        this.game_type = game_type;
        this.bid_points = bid_points;
        this.left_digit = left_digit;
        this.right_digit = right_digit;
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

    public String getLeft_digit() {
        return left_digit;
    }

    public String getRight_digit() {
        return right_digit;
    }
}
