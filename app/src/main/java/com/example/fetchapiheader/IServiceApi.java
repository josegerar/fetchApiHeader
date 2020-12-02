package com.example.fetchapiheader;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface IServiceApi {
    @Headers("Public-Merchant-Id: 985e73d97c854ccc9affa120002f3091")
    @GET("bankList")
    Call<List<BankModel>> getListBank();
}
