package com.madhurmilan.milan.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataDisawarWin {

    @Expose
    @SerializedName("message")
    String message;

    @Expose
    @SerializedName("code")
    String code;

    @Expose
    @SerializedName("status")
    String status;

    @Expose
    @SerializedName("data")
    private List<Data> data;


    public static class Data{
        @SerializedName("game_id")
        @Expose
        private String gameId;
        @SerializedName("game_type")
        @Expose
        private String gameType;

        @SerializedName("left_digit")
        @Expose
        private String left_digit;

        @SerializedName("right_digit")
        @Expose
        private String right_digit;

        @SerializedName("win_points")
        @Expose
        private String winPoints;

        @SerializedName("bid_points")
        @Expose
        private String bidPoints;

        @SerializedName("bidded_at")
        @Expose
        private String biddedAt;

        @SerializedName("won_at")
        @Expose
        private String wonAt;

        @SerializedName("game_name")
        @Expose
        private String gameName;

        public String getGameId() {
            return gameId;
        }

        public String getGameType() {
            return gameType;
        }

        public String getLeft_digit() {
            return left_digit;
        }

        public String getRight_digit() {
            return right_digit;
        }

        public String getWinPoints() {
            return winPoints;
        }

        public String getBidPoints() {
            return bidPoints;
        }

        public String getBiddedAt() {
            return biddedAt;
        }

        public String getWonAt() {
            return wonAt;
        }

        public String getGameName() {
            return gameName;
        }
    }

    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }

    public String getStatus() {
        return status;
    }

    public List<Data> getData() {
        return data;
    }
}
