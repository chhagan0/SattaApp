package com.madhurmilan.milan.apiclass;

import com.madhurmilan.milan.responseclass.DataApp;
import com.madhurmilan.milan.responseclass.DataDesawarList;
import com.madhurmilan.milan.responseclass.DataDisawarWin;
import com.madhurmilan.milan.responseclass.DataLogIN;
import com.madhurmilan.milan.responseclass.DataMain;
import com.madhurmilan.milan.responseclass.DataStarlineGameList;
import com.madhurmilan.milan.responseclass.DataWalletHistory;
import com.madhurmilan.milan.responseclass.DataGameList;
import com.madhurmilan.milan.responseclass.DataValue;
import com.madhurmilan.milan.responseclass.DataPlayTraining;
import com.madhurmilan.milan.responseclass.DataStarlineWin;
import com.madhurmilan.milan.responseclass.DataVerifyTransferCoin;
import com.madhurmilan.milan.responseclass.DataProfileStatus;
import com.madhurmilan.milan.responseclass.DataWin;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiInterface {

    @FormUrlEncoded
    @POST(ApiUrls.appLiveStatus)
    Call<DataPlayTraining> appLiveStatus(
            @Field("string") String string
    );

    @FormUrlEncoded
    @POST(ApiUrls.Registration)
    Call<DataMain> getRegisterMethod(
            @Field("full_name") String naam,
            @Field("mobile") String mobile,
            @Field("pin") String pin,
            @Field("password") String password
    );
    @FormUrlEncoded
    @POST(ApiUrls.resendOtp)
    Call<DataMain> resendOtp(
            @Field("mobile") String mobile
    );
    @FormUrlEncoded
    @POST(ApiUrls.verifyOtp)
    Call<DataLogIN> verifyOtp(
            @Field("mobile") String PhoneNumber,
            @Field("otp") String otp
    );
    @FormUrlEncoded
    @POST(ApiUrls.forgotPassword)
    Call<DataMain> forgotPassword(
            @Field("mobile") String mobile
    );
    @FormUrlEncoded
    @POST(ApiUrls.forgotPin)
    Call<DataMain> forgotPin(
            @Field("mobile") String mobile
    );
    @FormUrlEncoded
    @POST(ApiUrls.newPassword)
    Call<DataLogIN> newPassword(
            @Header("token") String token,
            @Field("mobile") String PhoneNumber,
            @Field("mobile_token") String mobileToken,
            @Field("password") String password
    );
    @FormUrlEncoded
    @POST(ApiUrls.newPin)
    Call<DataLogIN> newPin(
            @Header("token") String token,
            @Field("mobile") String PhoneNumber,
            @Field("mobile_token") String mobileToken,
            @Field("pin") String pin
    );

    @FormUrlEncoded
    @POST(ApiUrls.verify_customer)
    Call<DataLogIN> verifyCustomer(
            @Field("mobile") String mobile,
            @Field("mobile_token") String mobile_token,
            @Field("otp") String otp
    );


    @FormUrlEncoded
    @POST(ApiUrls.Signin)
    Call<DataLogIN> getSignIn(
      @Field("mobile") String PhoneNumber,
      @Field("password") String password
    );

    @FormUrlEncoded
    @POST(ApiUrls.signin_pin)
    Call<DataLogIN> signInPin(
            @Header("token") String token,
            @Field("pin") String pin
    );

    @FormUrlEncoded
    @POST(ApiUrls.customer_status)
    Call<DataProfileStatus> Customer_status(
            @Header("token") String token,
            @Field("string") String string
    );

    @FormUrlEncoded
    @POST(ApiUrls.for_pass_verify)
    Call<DataLogIN> forgotPassVerify(
            @Header("token") String token,
            @Field("mobile") String PhoneNumber,
            @Field("mobile_token") String PhoneToken,
            @Field("password") String password

    );

    @FormUrlEncoded
    @POST(ApiUrls.upgrade_phnpay)
    Call<DataMain> UpgradePhnePe(
            @Header("token") String token,
            @Field("phonepe") String phnePayNumber
    );

    @FormUrlEncoded
    @POST(ApiUrls.upgrade_bnk_info)
    Call<DataMain> upgradeBInfo(
            @Header("token") String token,
            @Field("account_holder_name") String accountHolderPerson,
            @Field("account_no") String BankAccNumber,
            @Field("ifsc_code") String BankIfsc,
            @Field("bank_name") String BranchName,
            @Field("branch_address") String BranchAddress
    );

    @FormUrlEncoded
    @POST(ApiUrls.upgrade_gpay)
    Call<DataMain> upgradeGpay(
            @Header("token") String token,
            @Field("gpay") String GpayNumber
    );

    @FormUrlEncoded
    @POST(ApiUrls.upgrade_ptm)
    Call<DataMain> UpgrdePytm(
            @Header("token") String token,
            @Field("paytm") String pytmNumber
    );

    @FormUrlEncoded
    @POST(ApiUrls.upgrade_user_profile)
    Call<DataLogIN> UpgrdeUserInfo(
            @Header("token") String token,
            @Field("email") String email,
            @Field("name") String name
    );

    @FormUrlEncoded
    @POST(ApiUrls.upgrade_fb_pin)
    Call<DataMain> UpgradeFbtoken(
            @Header("token") String token,
            @Field("token_id") String tokenId
    );

    @FormUrlEncoded
    @POST(ApiUrls.get_cstmr_info)
    Call<DataLogIN> GetUserInfo(
            @Header("token") String token,
            @Field("string") String string
    );

    @FormUrlEncoded
    @POST(ApiUrls.app_info)
    Call<DataApp> getAppInfo(
            @Field("string") String string
    );


    @FormUrlEncoded
    @POST(ApiUrls.add_coin)
    Call<DataMain> AddCoins(
            @Header("token") String token,
            @Field("points") String points,
            @Field("trans_status") String trans_status,
            @Field("trans_id") String trans_id
    );

    @FormUrlEncoded
    @POST(ApiUrls.transmitcoins)
    Call<DataMain> transmitCoins(
            @Header("token") String token,
            @Field("points") String points,
            @Field("user_number") String userNumber
    );
    @FormUrlEncoded
    @POST(ApiUrls.transmit_verification)
    Call<DataVerifyTransferCoin> transmitVerify(
            @Header("token") String token,
            @Field("user_number") String userNumber
    );

    @FormUrlEncoded
    @POST(ApiUrls.learn_to_play)
    Call<DataPlayTraining> howToLearn(
            @Header("token") String token,
            @Field("string") String string
    );
    @FormUrlEncoded
    @POST(ApiUrls.main_tournament_list)
    Call<DataGameList> MainTournamentList(
            @Header("token") String token,
            @Field("string") String string
    );
    @FormUrlEncoded
    @POST(ApiUrls.makeoffer)
    Call<DataMain> makeoffer(
            @Header("token") String token,
            @Field("game_bids") String gameBids
    );
    @FormUrlEncoded
    @POST(ApiUrls.tournament_price_list)
    Call<DataValue> tournamentValueList(
            @Header("token") String token,
            @Field("string") String string
    );

    @FormUrlEncoded
    @POST(ApiUrls.wthdraw)
    Call<DataMain> RetrieveAmnt(
            @Header("token") String token,
            @Field("points") String string,
            @Field("method") String method
    );

    @FormUrlEncoded
    @POST(ApiUrls.history_bids)
    Call<DataWin> HistoryOfBids(
            @Header("token") String token,
            @Field("from_date") String fromDate,
            @Field("to_date") String toDate
    );
    @FormUrlEncoded
    @POST(ApiUrls.history_wons)
    Call<DataWin> HistoryOfWins(
            @Header("token") String token,
            @Field("from_date") String fromDate,
            @Field("to_date") String toDate
    );

    @FormUrlEncoded
    @POST(ApiUrls.pursestatemnt)
    Call<DataWalletHistory> purseStatement(
            @Header("token") String token,
            @Field("string") String string
    );

    @FormUrlEncoded
    @POST(ApiUrls.wthdrawstatmnt)
    Call<DataWalletHistory> withdSatment(
            @Header("token") String token,
            @Field("string") String string
    );

    @FormUrlEncoded
    @POST(ApiUrls.gamesStrlne)
    Call<DataStarlineGameList> slTurnament(
            @Header("token") String token,
            @Field("string") String string
    );

    @FormUrlEncoded
    @POST(ApiUrls.strlineMakeOffer)
    Call<DataMain> starlinePlaceBid(
            @Header("token") String token,
            @Field("game_bids") String gameBids
    );

    @FormUrlEncoded
    @POST(ApiUrls.BidHistorystrline)
    Call<DataStarlineWin> starLineBidHistory(
            @Header("token") String token,
            @Field("from_date") String fromDate,
            @Field("to_date") String toDate
    );

    @FormUrlEncoded
    @POST(ApiUrls.strlnewonhistory)
    Call<DataStarlineWin> HistorySLBids(
            @Header("token") String token,
            @Field("from_date") String fromDate,
            @Field("to_date") String toDate
    );

    @FormUrlEncoded
    @POST(ApiUrls.gali_disawar_win_history)
    Call<DataDisawarWin> desawarWinHistory(
            @Header("token") String token,
            @Field("from_date") String fromDate,
            @Field("to_date") String toDate
    );

    @FormUrlEncoded
    @POST(ApiUrls.gali_disawar_game)
    Call<DataDesawarList> GaliDesawarMethod(
            @Header("token") String token,
            @Field("string") String string
    );
    @FormUrlEncoded
    @POST(ApiUrls.gali_disawar_bid_history)
    Call<DataDisawarWin> deasawarBidHistory(
            @Header("token") String token,
            @Field("from_date") String fromDate,
            @Field("to_date") String toDate
    );
    @FormUrlEncoded
    @POST(ApiUrls.galiDesawarPlaceBid)
    Call<DataMain> galiDesawarPlaceBid(
            @Header("token") String token,
            @Field("game_bids") String gameBids
    );
}
