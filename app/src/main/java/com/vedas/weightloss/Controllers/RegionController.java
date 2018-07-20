package com.vedas.weightloss.Controllers;

import android.content.Context;
import android.util.Log;

import com.vedas.weightloss.Models.CountryList;
import com.vedas.weightloss.ServerApis.ServerApisInterface;
import com.vedas.weightloss.ServerObjects.RegionServerObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Rise on 05/07/2018.
 */

public class RegionController {


    public static RegionController regionObj;
    Context context;
    String token = "";
    public ArrayList<CountryList> regionlist; //for fetching news
    public CountryList selectedCountry;

    public static RegionController getInstance() {
        if (regionObj == null) {
            regionObj = new RegionController();
            regionObj.regionlist = new ArrayList<>();
        }
        return regionObj;
    }

    public void fillCOntext(Context context1) {
        context = context1;

    }

    //fetching Region

    public void RegionServerAPI() {

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ServerApisInterface.country)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final ServerApisInterface api = retrofit.create(ServerApisInterface.class);
        Call<RegionServerObject> saleateam = api.countries(token);
        saleateam.enqueue(new Callback<RegionServerObject>() {
            @Override
            public void onResponse(Call<RegionServerObject> call, Response<RegionServerObject> response) {
                String statuscode = response.body().getStatus();

                if (response.isSuccessful()) {
                    String status = response.body().getStatus();
                    Log.e("statuscode", "" + status);
                    Log.e("result", "" + response.body().getCountryList());

                    if (RegionController.getInstance() != null) {
                        regionlist.addAll(response.body().getCountryList()); //response.body().getCountryList();
                        Log.e("regionlist", "call" + regionlist.size());
                        boolean can = response.body().getCanLoadMore();
                        Log.e("bool", "" + can);

                        for (CountryList country : response.body().getCountryList()) {
                            if (country.getName().equals("India")) {
                                selectedCountry = country;
                            }
                        }

                        if (can) {
                            token = response.body().getNextPageToken();
                            RegionServerAPI();
                        } else {
                            Log.e("canload", "" + false);
                        }
                    }

                }
            }

            @Override
            public void onFailure(Call<RegionServerObject> call, Throwable t) {
                Log.e("regionresponse", "failed");
            }
        });
    }
}

