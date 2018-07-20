package com.vedas.weightloss.ServerObjects;

import com.google.gson.annotations.SerializedName;

/**
 * Created by WAVE on 2/27/2018.
 */

public class UserServerObject {

    @SerializedName("email")
    public String mailid;

    @SerializedName("password")
    public String password;

    @SerializedName("regTime")
    public String register_time;

    @SerializedName("type")
    public  String type;

    @SerializedName("pin")
    public  String pin;//

    @SerializedName("attempt_time")
    public  String attempt_time;//

    @SerializedName("latitude")
    public String latitude;

    @SerializedName("longitude")
    public String longitude;

    @SerializedName("response")
    public String response;

    @SerializedName("result")
    public String result;


    @SerializedName("message")
    public String message;


}
