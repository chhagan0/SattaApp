package com.madhurmilan.milan.shareprefclass;

import android.content.Context;
import android.content.SharedPreferences;

public class SharPrefClass {
    public static String SHARED_PREF_NAME = "appName";
    public static String WSTPNO = "91+ 9216869685";
    public static String KEY_USER_NAME = "userName";
    public static String KEY_PHONE_NUMBER = "phoneNumber";
    public static String KEY_CLIENT_EMAIL = "ClientMail";
    public static String KEY_BANK_USER_NAME = "bUserName";
    public static String KEY_B_AC_N = "bAccountNumber";
    public static String KEY_B_IFSC_C = "bIfscCode";
    public static String KEY_B_N = "bName";
    public static String KEY_BRANCH_ADDRESS = "branchAddress";

    public static String KEY_P_UPI_ID = "pUPIId";
    public static String KEY_PP_UPI_ID = "ppUPIId";
    public static String KEY_G_PAY_UPI_ID = "gPayUPIId";
    public static String KEY_MAR_TXT = "marTxt";

    public static String KEY_PHONE_NUMBER1 = "phoneNumber1";
    public static String KEY_PHONE_NUMBER2 = "phoneNumber2";
    public static String KEY_WHATSAP_NUMBER = "whtappNumber";
    public static String KEY_REACH_US_EMAIL = "reachusEmail";
    public static String KEY_POSTER_IMAGES1 = "posterImages1";
    public static String KEY_POSTER_IMAGES0 = "https://i.ibb.co/b3BfrKX/vdrslider.jpg";
    public static String KEY_POSTER_IMAGES2 = "posterImages2";
    public static String KEY_POSTER_IMAGES3 = "posterImages3";

    static String KEY_SIGNIN_SUCCESS = "signinSuccess";
    static String KEY_LIVE_USER = "liveUser";
    static String KEY_SIGNIN_TOKEN = "SignInToken";
    public static String KEY_FB_TOKEN = "firebasebToken";
    static String KEY_CUSTOMER_COINS = "customercoins";
    static String KEY_TRANSMIT_COINS = "transmitcoins";

    public static String KEY_ADD_COINS_BHIM_ID = "addcoinsUpiID";
    public static String KEY_ADD_COINS_BHIM_NAME = "addcoinsUpiName";

    public static String KEY_MIN_EXTRACT_COINS = "minextractcoins";
    public static String KEY_MAX_EXTRACT_COINS = "maxextractcoins";
    public static String KEY_MIN_TRANSMIT_COINS = "minTransmitPoints";
    public static String KEY_MAX_TRANSMIT_COINS = "maxTransferPoints";
    public static String KEY_MIN_OFFER_SUM = "minoffersumAmount";
    public static String KEY_MAX_OFFER_SUM = "maxoffersumAmount";
    public static String KEY_MIN_ADD_AMOUNT_COINS = "minAddamountPoints";
    public static String KEY_MAX_ADD_AMOUNT_COINS = "maxAddamountPoints";
    public static String KEY_FIREBSE_ALLOW = "firebaseAllow";
    public static String KEY_DEVELOPER_MODE = "developerMode";

    public static SharedPreferences getshrprefMthd(Context context){
        return context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
    }

    //setter
    public static void setSharedBooleanStatus(Context context, String KEY, boolean status){
        SharedPreferences.Editor editor = getshrprefMthd(context).edit();
        editor.putBoolean(KEY, status);
        editor.apply();
    }
    public static boolean getSharedBooleanStatus(Context context, String KEY){
        SharedPreferences sharedPreferences = getshrprefMthd(context);
        return sharedPreferences.getBoolean(KEY, false);
    }

    public static void setPrefrenceStrngData(Context context, String KEY, String data){
        SharedPreferences.Editor editor = getshrprefMthd(context).edit();
        editor.putString(KEY, data);
        editor.apply();
    }
    public static String getPrfrnceinfo(Context context, String KEY){
        SharedPreferences sharedPreferences = getshrprefMthd(context);
        return sharedPreferences.getString(KEY, null);
    }
    public static void setBinalData(Context context, String KEY, boolean data){
        SharedPreferences.Editor editor = getshrprefMthd(context).edit();
        editor.putBoolean(KEY, data);
        editor.apply();
    }
    public static boolean getBinalObject(Context context, String KEY, boolean defaultValue){
        SharedPreferences sharedPreferences = getshrprefMthd(context);
        return sharedPreferences.getBoolean(KEY, defaultValue);
    }

    public static void setLiveUser(Context context, boolean status){
        SharedPreferences.Editor editor = getshrprefMthd(context).edit();
        editor.putBoolean(KEY_LIVE_USER, status);
        editor.apply();
    }
    public static boolean getLiveUser(Context context){
        SharedPreferences sharedPreferences = getshrprefMthd(context);
        return sharedPreferences.getBoolean(KEY_LIVE_USER, false);
    }

    public static void setMaxMinData(Context context, String KEY, String data ){
        SharedPreferences.Editor editor = getshrprefMthd(context).edit();
        editor.putString(KEY, data);
        editor.apply();
    }
    public static String getMaxMinObject(Context context, String KEY){
        SharedPreferences sharedPreferences = getshrprefMthd(context);
        return sharedPreferences.getString(KEY, null);
    }
    public static void setRegisterData(Context context, String KEY, String data ){
        SharedPreferences.Editor editor = getshrprefMthd(context).edit();
        editor.putString(KEY, data);
        editor.apply();
    }
    public static String getRegistrationObject(Context context, String KEY){
        SharedPreferences sharedPreferences = getshrprefMthd(context);
        return sharedPreferences.getString(KEY, null);
    }
    public static void setBankInformation(Context context, String KEY, String details){
        SharedPreferences.Editor editor = getshrprefMthd(context).edit();
        editor.putString(KEY, details);
        editor.apply();
    }
    public static String getBankObject(Context context, String KEY){
        SharedPreferences sharedPreferences = getshrprefMthd(context);
        return sharedPreferences.getString(KEY, null);
    }

    public static void setPosterImages(Context context, String  KEY, String imageUrl){
        SharedPreferences.Editor editor = getshrprefMthd(context).edit();
        editor.putString(KEY, imageUrl);
        editor.apply();
    }
    public static String getPosterImage(Context context, String KEY){
        SharedPreferences sharedPreferences = getshrprefMthd(context);
        return sharedPreferences.getString(KEY, null);
    }

    public static void setContactUsInfo(Context context, String KEY , String details){
        SharedPreferences.Editor editor = getshrprefMthd(context).edit();
        editor.putString(KEY, details);
        editor.apply();
    }
    public static String getContactObject(Context context, String KEY){
        SharedPreferences sharedPreferences = getshrprefMthd(context);
        return sharedPreferences.getString(KEY, null);
    }
    public static void setAddAmountUPI(Context context, String KEY, String upi){
        SharedPreferences.Editor editor = getshrprefMthd(context).edit();
        editor.putString(KEY, upi);
        editor.apply();
    }
    public static String getAddAmountUpiId(Context context, String KEY){
        SharedPreferences sharedPreferences = getshrprefMthd(context);
        return sharedPreferences.getString(KEY, "");
    }

    public static void setTransmitCoins(Context context , boolean status){
        SharedPreferences.Editor editor = getshrprefMthd(context).edit();
        editor.putBoolean(KEY_TRANSMIT_COINS, status);
        editor.apply();
    }

    public static void setSigninSuccess(Context context, boolean status){
        SharedPreferences.Editor editor = getshrprefMthd(context).edit();
        editor.putBoolean(KEY_SIGNIN_SUCCESS, status);
        editor.apply();
    }
    public static boolean getsignInSuccess(Context context){
        SharedPreferences sharedPreferences = getshrprefMthd(context);
        return sharedPreferences.getBoolean(KEY_SIGNIN_SUCCESS,false);
    }
    public static void setSigninTkn(Context context, String token){
        SharedPreferences.Editor editor = getshrprefMthd(context).edit();
        editor.putString(KEY_SIGNIN_TOKEN, token);
        editor.apply();
    }
    public static String getLoginInToken(Context context){
        SharedPreferences sharedPreferences = getshrprefMthd(context);
        return sharedPreferences.getString(KEY_SIGNIN_TOKEN, null);
    }
    public static void setUserCoins(Context context, String points){
        SharedPreferences.Editor editor = getshrprefMthd(context).edit();
        editor.putString(KEY_CUSTOMER_COINS, points);
        editor.apply();
    }
    public static String getCustomerCoins(Context context){
        SharedPreferences sharedPreferences = getshrprefMthd(context);
        return sharedPreferences.getString(KEY_CUSTOMER_COINS, "0");
    }
    public static boolean getTransmitCoins(Context context){
        SharedPreferences sharedPreferences = getshrprefMthd(context);
        return sharedPreferences.getBoolean(KEY_TRANSMIT_COINS, false);
    }

    public static void setCleaninfo(Context context){
        SharedPreferences.Editor editor = getshrprefMthd(context).edit();
        editor.clear();
        editor.apply();
    }

}
