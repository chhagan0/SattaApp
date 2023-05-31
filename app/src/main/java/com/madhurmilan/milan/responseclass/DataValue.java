package com.madhurmilan.milan.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataValue {

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
        @SerializedName("cost_amount")
        String cost_amount;

        @Expose
        @SerializedName("earning_amount")
        String earning_amount;


        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getCost_amount() {
            return cost_amount;
        }

        public String getEarning_amount() {
            return earning_amount;
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
