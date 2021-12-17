package com.example.kotov_majorov_stadnichenko_samost;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserService {
    @POST("authenticate/")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);

    @POST("users2/")
    Call<RegisterResponse> registerUsers(@Body RegisterRequest registerRequest);
}
