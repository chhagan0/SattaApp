package com.madhurmilan.milan.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataDesawarList {

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
        @SerializedName("gali_disawar_chart")
        String gali_disawar_chart;

        @Expose
        @SerializedName("gali_disawar_rates")
        List<GalidesawarRates> galidesawarRates;

        @Expose
        @SerializedName("gali_disawar_game")
        List<GaliDesawarGame> galiDesawarGame;

        public static class GalidesawarRates {
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
        public class GaliDesawarGame {
            @Expose
            @SerializedName("id")
            String id;
            @Expose
            @SerializedName("name")
            String name;
            @Expose
            @SerializedName("time")
            String time;
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

            public String getTime() {
                return time;
            }
        }

        public String getGali_disawar_chart() {
            return gali_disawar_chart;
        }

        public List<GalidesawarRates> getGalidesawarRates() {
            return galidesawarRates;
        }

        public List<GaliDesawarGame> getGaliDesawarGame() {
            return galiDesawarGame;
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
