package com.madhurmilan.milan.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataProfileStatus {

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
        @SerializedName("available_points")
        String availablePoints;

        @Expose
        @SerializedName("transfer")
        String transferPoint;

        @Expose
        @SerializedName("upi_payment_id")
        String upiPaymentId;

        @Expose
        @SerializedName("upi_name")
        String upiName;

        @Expose
        @SerializedName("minimum_bid_amount")
        String minimumBidAmount;

        @Expose
        @SerializedName("maximum_bid_amount")
        String maximumBidAmount;

        @Expose
        @SerializedName("minimum_transfer")
        String minimumTransfer;

        @Expose
        @SerializedName("maximum_transfer")
        String maximumTransfer;

        @Expose
        @SerializedName("minimum_withdraw")
        String minimumWithdraw;

        @Expose
        @SerializedName("maximum_withdraw")
        String maximumWithdraw;

        @Expose
        @SerializedName("minimum_deposit")
        String minimumDeposit;

        @Expose
        @SerializedName("maximum_deposit")
        String maximumDeposit;

        public String getAvailablePoints() {
            return availablePoints;
        }

        public String getTransferPoint() {
            return transferPoint;
        }

        public String getUpiPaymentId() {
            return upiPaymentId;
        }

        public String getUpiName() {
            return upiName;
        }

        public String getMinimumBidAmount() {
            return minimumBidAmount;
        }

        public String getMaximumBidAmount() {
            return maximumBidAmount;
        }

        public String getMinimumTransfer() {
            return minimumTransfer;
        }

        public String getMaximumTransfer() {
            return maximumTransfer;
        }

        public String getMinimumWithdraw() {
            return minimumWithdraw;
        }

        public String getMaximumWithdraw() {
            return maximumWithdraw;
        }

        public String getMinimumDeposit() {
            return minimumDeposit;
        }

        public String getMaximumDeposit() {
            return maximumDeposit;
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
