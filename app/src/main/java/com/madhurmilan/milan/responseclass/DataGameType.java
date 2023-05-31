package com.madhurmilan.milan.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataGameType {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("data")
    @Expose
    private Data data;

    public static class Data{
        @SerializedName("single_digit")
        @Expose
        private Boolean singleDigit;
        @SerializedName("jodi_digit")
        @Expose
        private Boolean jodiDigit;
        @SerializedName("single_panna")
        @Expose
        private Boolean singlePanna;
        @SerializedName("double_panna")
        @Expose
        private Boolean doublePanna;
        @SerializedName("tripple_panna")
        @Expose
        private Boolean tripplePanna;
        @SerializedName("half_sangam")
        @Expose
        private Boolean halfSangam;
        @SerializedName("full_sangam")
        @Expose
        private Boolean fullSangam;

        public Boolean getSingleDigit() {
            return singleDigit;
        }

        public Boolean getJodiDigit() {
            return jodiDigit;
        }

        public Boolean getSinglePanna() {
            return singlePanna;
        }

        public Boolean getDoublePanna() {
            return doublePanna;
        }

        public Boolean getTripplePanna() {
            return tripplePanna;
        }

        public Boolean getHalfSangam() {
            return halfSangam;
        }

        public Boolean getFullSangam() {
            return fullSangam;
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
