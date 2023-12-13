package com.hello.ourApplication.DTO;

import com.google.gson.annotations.SerializedName;

public class Recommend {
    @SerializedName("operation")
    private String operation;
    @SerializedName("emotion")
    private String emotion;

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getEmotion() {
        return emotion;
    }

    public void setEmotion(String emotion) {
        this.emotion = emotion;
    }

    public Recommend(String emotion) {
        this.operation = "recommend";
        this.emotion = emotion;
    }
}
