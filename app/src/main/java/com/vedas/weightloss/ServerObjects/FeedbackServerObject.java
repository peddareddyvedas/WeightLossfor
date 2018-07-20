package com.vedas.weightloss.ServerObjects;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Rise on 21/06/2018.
 */

public class FeedbackServerObject {



    @SerializedName("email")
    public String mailid;

    @SerializedName("feel_support")
    public String feelsupport;

    @SerializedName("additional_comment")
    public String additionalcomment;

    @SerializedName("timestamp")
    public String timestamp;

    @SerializedName("response")
    public String result;

    @SerializedName("message")
    public String message;


}
