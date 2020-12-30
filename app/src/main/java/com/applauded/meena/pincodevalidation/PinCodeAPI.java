package com.applauded.meena.pincodevalidation;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PinCodeAPI {

    @GET("pincode?code=560102")
    Call<PinCode> getmyArea();

    @GET("pincode")
    Call<PinCode> getArea(@Query("code") String code);
}

//getArea(@Query(value="code", encode = true) String pinput);