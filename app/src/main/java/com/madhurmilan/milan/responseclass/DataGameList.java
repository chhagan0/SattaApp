package com.madhurmilan.milan.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataGameList {

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
    List<Data> data;

    public static class Data{
        @Expose
        @SerializedName("id")
        String id;
        @Expose
        @SerializedName("name")
        String name;
        @Expose
        @SerializedName("open_time")
        String open_time;
        @Expose
        @SerializedName("close_time")
        String close_time;
        @Expose
        @SerializedName("chart_url")
        String chart_url;
        @Expose
        @SerializedName("play")
        boolean play;
        @Expose
        @SerializedName("result")
        String result;
        @Expose
        @SerializedName("market_open")
        boolean market_open;
        @Expose
        @SerializedName("open")
        boolean open;

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getOpen_time() {
            return open_time;
        }

        public String getClose_time() {
            return close_time;
        }

        public String getChart_url() {
            return chart_url;
        }

        public boolean isPlay() {
            return play;
        }

        public String getResult() {
            return result;
        }

        public boolean isMarket_open() {
            return market_open;
        }

        public boolean isOpen() {
            return open;
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
