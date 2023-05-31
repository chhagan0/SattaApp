package com.madhurmilan.milan.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataWalletHistory {

    @Expose
    @SerializedName("message")
    String message;

    @Expose
    @SerializedName("code")
    String code;

    @Expose
    @SerializedName("status")
    String status;

    @SerializedName("data")
    @Expose
    private Data data;

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

    public static class Data {
        @SerializedName("available_points")
        @Expose
        private String availablePoints;
        @SerializedName("withdraw_open_time")
        @Expose
        private String withdrawOpenTime;
        @SerializedName("withdraw_close_time")
        @Expose
        private String withdrawCloseTime;
        @SerializedName("statement")
        @Expose
        private final List<Statement> statement = null;

        public String getAvailablePoints() {
            return availablePoints;
        }

        public String getWithdrawOpenTime() {
            return withdrawOpenTime;
        }

        public String getWithdrawCloseTime() {
            return withdrawCloseTime;
        }

        public List<Statement> getStatement() {
            return statement;
        }

        public static class Statement{
            @SerializedName("points")
            @Expose
            private String points;
            @SerializedName("trans_type")
            @Expose
            private String transType;
            @SerializedName("trans_det")
            @Expose
            private String transDet;
            @SerializedName("trans_status")
            @Expose
            private String transStatus;
            @SerializedName("created_at")
            @Expose
            private String createdAt;
            @SerializedName("trans_msg")
            @Expose
            private String transMsg;

            public String getPoints() {
                return points;
            }

            public String getTransType() {
                return transType;
            }

            public String getTransDet() {
                return transDet;
            }

            public String getTransStatus() {
                return transStatus;
            }

            public String getCreatedAt() {
                return createdAt;
            }

            public String getTransMsg() {
                return transMsg;
            }
        }
    }


}
