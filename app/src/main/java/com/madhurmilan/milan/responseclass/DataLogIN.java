package com.madhurmilan.milan.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataLogIN {
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
        @SerializedName("token")
        String token;

        @Expose
        @SerializedName("username")
        String username;

        @Expose
        @SerializedName("mobile")
        String mobile;

        @Expose
        @SerializedName("email")
        String email;

        @Expose
        @SerializedName("bank_name")
        String bank_name;

        @Expose
        @SerializedName("account_holder_name")
        String account_holder_name;

        @Expose
        @SerializedName("ifsc_code")
        String ifsc_code;

        @Expose
        @SerializedName("branch_address")
        String branch_address;

        @Expose
        @SerializedName("bank_account_no")
        String bank_account_no;

        @Expose
        @SerializedName("paytm_mobile_no")
        String paytm_mobile_no;

        @Expose
        @SerializedName("phonepe_mobile_no")
        String phonepe_mobile_no;

        @Expose
        @SerializedName("gpay_mobile_no")
        String gpay_mobile_no;

        public String getToken() {
            return token;
        }

        public String getUsername() {
            return username;
        }

        public String getMobile() {
            return mobile;
        }

        public String getEmail() {
            return email;
        }

        public String getBank_name() {
            return bank_name;
        }

        public String getAccount_holder_name() {
            return account_holder_name;
        }

        public String getIfsc_code() {
            return ifsc_code;
        }

        public String getBranch_address() {
            return branch_address;
        }

        public String getBank_account_no() {
            return bank_account_no;
        }

        public String getPaytm_mobile_no() {
            return paytm_mobile_no;
        }

        public String getPhonepe_mobile_no() {
            return phonepe_mobile_no;
        }

        public String getGpay_mobile_no() {
            return gpay_mobile_no;
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
