package com.vedas.weightloss.ServerApis;
import com.vedas.weightloss.GoogleMaps.GooglePlaceObject;
import com.vedas.weightloss.ServerObjects.FeedbackServerObject;
import com.vedas.weightloss.ServerObjects.NewsServerObject;
import com.vedas.weightloss.ServerObjects.PersonalInfoServerObject;
import com.vedas.weightloss.ServerObjects.RegionServerObject;
import com.vedas.weightloss.ServerObjects.UserServerObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by VEDAS on 5/24/2018.
 */

public interface ServerApisInterface {
    public static String home_URL = "http://117.247.13.135/weightloss/";
    String GOOGLE_MAPS_BASE_URL = "https://maps.googleapis.com";
    public static String country = "http://117.247.13.135/weightloss/";
    public static String image_URL ="http://117.247.13.135";

    @GET("countries/list")
    Call<RegionServerObject> countries(@Query("token") String token);

    @Multipart
    @POST("profile")
    Call<PersonalInfoServerObject> personalinfo(@Part MultipartBody.Part filePart,
                                                @Part("email")RequestBody  email,
                                                @Part("gender") RequestBody gender,
                                                @Part("birthday") RequestBody birthday,
                                                @Part("targetWeight") RequestBody targetWeight,
                                                @Part("location") RequestBody location,
                                                @Part("height") RequestBody height,
                                                @Part("weight") RequestBody weight,
                                                @Part("bmi") RequestBody bmi,
                                                @Part("bmr") RequestBody bmr,
                                                @Part("activityLevel") RequestBody activityLevel,
                                                @Part("recommendedPlan") RequestBody recommendedPlan,
                                                @Part("targetCalories") RequestBody targetCalories,
                                                @Part("zip") RequestBody zip,
                                                @Part("targetDays") RequestBody targetDays
                                                );

    //for register
    @POST("register")
    Call<UserServerObject> register(@Body UserServerObject body);

    //for register
    @POST("verification")
    Call<UserServerObject> verification(@Body UserServerObject body);

    //for register
    @POST("forgot")
    Call<UserServerObject> forgotAPiExection(@Body UserServerObject body);


    //for register
    @POST("setpassword")
    Call<UserServerObject> newPasswordAPiExection(@Body UserServerObject body);


    //for register
    @POST("login")
    Call<UserServerObject> loginAPiExection(@Body UserServerObject body);

    //for delete
    @HTTP(method = "DELETE", path = "delete", hasBody = true)
    Call<UserServerObject> delete(@Body UserServerObject body);


    @POST("feedback")
    Call<FeedbackServerObject> feedback(@Body FeedbackServerObject body);

    @GET("newsfeed")
    Call<NewsServerObject> fetchnewsfeed();

    ////////////////for maps

    @GET("/maps/api/place/nearbysearch/json")
    Call<GooglePlaceObject> getNearbyPlaceswithOutPageToken(@Query("type") String type, @Query("location") String location, @Query("radius") Integer radius, @Query("key") String key);

    @GET("/maps/api/place/nearbysearch/json")
    Call<GooglePlaceObject> getNearbyPlaceswithPageToken(@Query("key") String key, @Query("pagetoken") String pagetoken);


    // http://maps.googleapis.com/maps/api/directions/json?origin=Chicago,IL&destination=Los%20Angeles,CA&sensor=false
    @GET("/maps/api/directions/json")
    Call<GooglePlaceObject> getDirections(@Query("origin") String origin, @Query("destination") String destination, @Query("key") String key, @Query("Mode") String mode);

    ///
    @GET("/maps/api/place/details/json")
    Call<GooglePlaceObject> getPlaceDetailsWithPlaceId(@Query("key") String key,@Query("placeid") String placeid);

}
