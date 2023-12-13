package com.hello.ourApplication.Retrofit;

import com.hello.ourApplication.DTO.DiaryResponse;
import com.hello.ourApplication.DTO.EmotionResponse;
import com.hello.ourApplication.DTO.Login;
import com.hello.ourApplication.DTO.LoginResponse;
import com.hello.ourApplication.DTO.ReadDiary;
import com.hello.ourApplication.DTO.ReadEmotion;
import com.hello.ourApplication.DTO.Recommend;
import com.hello.ourApplication.DTO.RecommendResponse;
import com.hello.ourApplication.DTO.Signup;
import com.hello.ourApplication.DTO.SignupResponse;
import com.hello.ourApplication.DTO.WriteDiary;
import com.hello.ourApplication.DTO.WriteEmotion;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitAPI {
    @POST("behappy/login")
    Call<LoginResponse> getLoginResponse(@Body Login login);

    @POST("behappy/signup")
    Call<SignupResponse> getSignupResponse(@Body Signup signup);

    @POST("behappy/diary")
    Call<DiaryResponse> getWriteDiaryResponse(@Body WriteDiary writeDiary);

    @POST("behappy/diary")
    Call<DiaryResponse> getReadDiaryResponse(@Body ReadDiary readDiary);

    @POST("behappy/emotion")
    Call<EmotionResponse> getReadEmotionResponse(@Body ReadEmotion readEmotion);

    @POST("behappy/emotion")
    Call<EmotionResponse> getWriteEmotionResponse(@Body WriteEmotion writeEmotion);
    @POST("behappy/recommend")
    Call<RecommendResponse> getRecommendResponse(@Body Recommend recommend);
}
