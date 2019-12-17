package com.sf_ert.ertdispatcher.Api;

import com.sf_ert.ertdispatcher.Model.Alert;
import com.sf_ert.ertdispatcher.Model.Bus;
import com.sf_ert.ertdispatcher.Model.Messages;
import com.sf_ert.ertdispatcher.Model.Route;
import com.sf_ert.ertdispatcher.Model.Schedule;
import com.sf_ert.ertdispatcher.Model.SendMessage;
import com.sf_ert.ertdispatcher.Model.SignIn;
import com.sf_ert.ertdispatcher.Model.Terminal;
import com.sf_ert.ertdispatcher.Model.Users;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("Api/sendMessage")
    Call<List<SendMessage>> sendMessage(@Field("sender") String sender,
                                        @Field("receiver") String receiver,
                                        @Field("message") String message);
    @FormUrlEncoded
    @POST("Api/getMessages")
    Call<List<Messages>> getMessages(@Field("receiver") String receiver,
                                     @Field("sender") String sender);


    @FormUrlEncoded
    @POST("Init/dispatcherLogin")
        //user Call<List<[yourModel]>> if you are expecting array in response, use below code if expecting object response
    Call<SignIn> signIn(@Field("adminUserName") String email,
                              @Field("adminUserPass") String password);
    @POST("Api/terminalLocations")
    Call<List<Terminal>> terminalLocation();

    @POST("Api/alert")
    Call<List<Alert>> transitActivity();

    @POST("Api/getBus")
    Call<List<Bus>> getBus();

    @POST("Api/spinnerRoute")
    Call<List<Route>> getSpinnerRoute();

    @FormUrlEncoded
    @POST("Api/getListDriverAssignToTwoTerminal")
    Call<List<Users>> userList(@Field("userId") String userId,
                                  @Field("terminal") String to_terminal);

    @FormUrlEncoded
    @POST("Api/addSchedule")
    Call<Schedule> setSched(@Field("busNum") String busNum,
                                  @Field("departTime") String departTime,
                                  @Field("busRoute") String busRoute);
}
