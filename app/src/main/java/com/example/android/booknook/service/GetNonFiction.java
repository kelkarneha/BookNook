package com.example.android.booknook.service;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Administrator on 1/3/2017.
 */

public interface GetNonFiction {
    @GET("combined-print-and-e-book-nonfiction.json")
    Call<Example> getBestSellers(
            @Query("api-key") String APIKey
    );
}
