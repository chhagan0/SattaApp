package com.madhurmilan.milan.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataStarlineGameList {

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
    Data data;

    public static class Data{
        @Expose
        @SerializedName("starline_chart")
        String starlineChart;

        @Expose
        @SerializedName("starline_rates")
        List<StarlineRates> starlineRates;

        @Expose
        @SerializedName("starline_game")
        List<StarlineGame> starlineGame;

        public class StarlineRates{
            @Expose
            @SerializedName("cost_amount")
            String cost_amount;
            @Expose
            @SerializedName("name")
            String name;
            @Expose
            @SerializedName("earning_amount")
            String earning_amount;

            public String getCost_amount() {
                return cost_amount;
            }

            public String getName() {
                return name;
            }

            public String getEarning_amount() {
                return earning_amount;
            }
        }
        public class StarlineGame{
            @Expose
            @SerializedName("id")
            String id;
            @Expose
            @SerializedName("name")
            String name;
            @Expose
            @SerializedName("result")
            String result;
            @Expose
            @SerializedName("play")
            boolean play;

            public String getId() {
                return id;
            }

            public String getName() {
                return name;
            }

            public String getResult() {
                return result;
            }

            public boolean isPlay() {
                return play;
            }
        }

        public String getStarlineChart() {
            return starlineChart;
        }

        public List<StarlineRates> getStarlineRates() {
            return starlineRates;
        }

        public List<StarlineGame> getStarlineGame() {
            return starlineGame;
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

    public Data getData() {
        return data;
    }
}
