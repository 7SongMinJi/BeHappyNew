package com.hello.ourApplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.unity3d.player.UnityPlayer;

public class SharedClass {

    public static void showMainActivity(String setToColor) {
        showMainActivity(UnityPlayer.currentActivity, setToColor);
    }

    public static void showMainActivity(Activity activity, String setToColor) {
        Intent intent = new Intent((Context) activity, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("setColor", setToColor);
        activity.startActivity(intent);
    }

    public static void addControlsToUnityFrame(Activity activity) {
        Button showMainButton = new Button(activity);
        showMainButton.setText("Show Main");
        showMainButton.setX(10);
        showMainButton.setY(500);
        showMainButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showMainActivity(activity, "");
            }
        });

        Button sendMsgButton = new Button(activity);
        sendMsgButton.setText("Send Msg");
        sendMsgButton.setX(320);
        sendMsgButton.setY(500);
        sendMsgButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            }
        });

        Button unloadButton = new Button(activity);
        unloadButton.setText("Unload UnityPlayer");
        unloadButton.setX(630);
        unloadButton.setY(500);



        Button finishButton = new Button(activity);
        finishButton.setText("Activity Finish");
        finishButton.setX(630);
        finishButton.setY(800);

        finishButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                activity.finish();
            }
        });
    }


}
