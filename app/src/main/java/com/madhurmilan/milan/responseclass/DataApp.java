package com.madhurmilan.milan.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataApp {
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
        @SerializedName("banner_marquee")
        String banner_marquee;

        @Expose
        @SerializedName("contact_details")
        ContactDetails contact_details;

        @Expose
        @SerializedName("banner_image")
        BannerImages banner_image;

        public static class ContactDetails{
            @Expose
            @SerializedName("whatsapp_no")
            String whatsapp_no;

            @Expose
            @SerializedName("mobile_no_1")
            String mobile_no_1;


            @Expose
            @SerializedName("telegram_channel_link")
            String telegram_channel_link;

            @Expose
            @SerializedName("email_1")
            String email_1;

            public String getWhatsapp_no() {
                return whatsapp_no;
            }

            public String getMobile_no_1() {
                return mobile_no_1;
            }

            public String getTelegram_channel_link() {
                return telegram_channel_link;
            }

            public String getEmail_1() {
                return email_1;
            }
        }

        public static class BannerImages{
            @Expose
            @SerializedName("banner_img_1")
            String banner_img_1;

            @Expose
            @SerializedName("banner_img_2")
            String banner_img_2;

            @Expose
            @SerializedName("banner_img_3")
            String banner_img_3;

            public String getBanner_img_1() {
                return banner_img_1;
            }

            public String getBanner_img_2() {
                return banner_img_2;
            }

            public String getBanner_img_3() {
                return banner_img_3;
            }
        }

        public String getBanner_marquee() {
            return banner_marquee;
        }

        public ContactDetails getContact_details() {
            return contact_details;
        }

        public BannerImages getBanner_image() {
            return banner_image;
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
