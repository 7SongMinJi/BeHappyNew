package com.hello.ourApplication.DTO;

import com.google.gson.annotations.SerializedName;

public class RecommendResponse {
    @SerializedName("statusCode")
    public String statusCode;

    @SerializedName("body")
    public String token;

    public String getStatusCode() {
        return statusCode;
    }

    public String getToken() {
        return token;
    }
}
