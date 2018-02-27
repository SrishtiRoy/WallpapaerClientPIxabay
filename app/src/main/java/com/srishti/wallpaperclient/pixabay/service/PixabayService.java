package com.srishti.wallpaperclient.pixabay.service;


import com.srishti.wallpaperclient.pixabay.model.PixabayResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import static com.srishti.wallpaperclient.pixabay.util.Constatns.Pixabay.PIXABAY_API_PATH;



public interface PixabayService {
    @GET(PIXABAY_API_PATH)
    Call<PixabayResponse> imageSearch(@Query("q") String keywords);

    @GET(PIXABAY_API_PATH + "&editors_choice=true")
    Call<PixabayResponse> editorsChoice();

    @GET(PIXABAY_API_PATH + "&q=flower")
    Call<PixabayResponse> flower();

    @GET(PIXABAY_API_PATH + "&q=abstract")
    Call<PixabayResponse> abstractArt();
    @GET(PIXABAY_API_PATH + "&q=nature")
    Call<PixabayResponse> nature();
    @GET(PIXABAY_API_PATH + "&q=painting")
    Call<PixabayResponse> painting();
}
