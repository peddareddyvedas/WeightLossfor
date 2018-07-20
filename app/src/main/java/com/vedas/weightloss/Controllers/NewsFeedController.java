package com.vedas.weightloss.Controllers;

import android.content.Context;
import android.util.Log;

import com.vedas.weightloss.Models.NewsFeedObject;
import com.vedas.weightloss.ServerApis.ServerApisInterface;
import com.vedas.weightloss.ServerObjects.NewsServerObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Rise on 14/06/2018.
 */

public class NewsFeedController {

    public static NewsFeedController newsObj;
    Context context;

    public ArrayList<NewsFeedObject> newsarraylist; //for fetching news


    public static NewsFeedController getInstance() {
        if (newsObj == null) {
            newsObj = new NewsFeedController();
            newsObj.newsarraylist = new ArrayList<>();


        }
        return newsObj;
    }

    public void fillCOntext(Context context1) {
        context = context1;

    }

    //fetching Videos vedas team
    public void fetchnews() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ServerApisInterface.home_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ServerApisInterface api = retrofit.create(ServerApisInterface.class);
        Call<NewsServerObject> saleateam = api.fetchnewsfeed();
        saleateam.enqueue(new Callback<NewsServerObject>() {
            @Override
            public void onResponse(Call<NewsServerObject> call, Response<NewsServerObject> response) {
                Log.e("newsresponse", " " + response.body() + " " + response.body().response);
                String statuscode = response.body().response;

                if (response.body() != null) {

                    String statusCode = response.body().response;
                    Log.e("codefor3", "" + statusCode);
                    Log.e("response", "" + response.body().data);

                    newsarraylist = response.body().data;
                    Log.e("newsarraylist", "call" + newsarraylist.size());


                }
            }

            @Override
            public void onFailure(Call<NewsServerObject> call, Throwable t) {
                Log.e("newsresponse", "failed");
            }
        });
    }
}