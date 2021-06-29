package com.tookan;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;


import java.util.HashMap;


import tookan.appdata.AppManager;
import tookan.appdata.Constants;
import tookan.listener.OnJobStatusListener;
import tookan.listener.OnTookanSDKListener;
import tookan.retrofit2.APIError;
import tookan.retrofit2.CommonResponse;

import tookan.sdk.models.CaptureUserData;
import tookan.sdk.models.TookanAgentConfig;
import tookan.sdk.models.TookanConfigAttributes;
import tookan.utility.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelId = "notification_channel";
            String channelName = "tookan_sdk";
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW));
        }

        // If a notification message is tapped, any data accompanying the notification
        // message is available in the intent extras. In this sample the launcher
        // intent is fired when the notification is tapped, so any accompanying data would
        // be handled here. If you want a different intent fired, set the click_action
        // field of the notification message to the desired intent. The launcher intent
        // is used when no click_action is specified.
        //
        // Handle possible data accompanying notification message.
        // [START handle_data_extras]
        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Log.d("Key: ", key + " Value: " + value);
            }
        }
        // [END handle_data_extras]

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.e("getInstanceId failed", task.getException().toString());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast

                        Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();

                        HashMap<String, String> customAttr = new HashMap<>();

                        customAttr.put("app_name", "SDK Tookan"); // your app name

                        CaptureUserData userData = new CaptureUserData.Builder()
                                .userID(1L)
                                .fullName("Ankush Walia")
                                .email("ankush.walia@jungleworks.com")
                                .phoneNumber("")
                                .countryCode("+91")
                                .latitude(30.7191)
                                .longitude(76.8107)
                                .customAttributes(customAttr)
                                .build();

                        TookanConfigAttributes tookanConfigAttributes = new TookanConfigAttributes.Builder()
                                .setCaptureUserData(userData)
                                .setProvider(BuildConfig.APPLICATION_ID + ".provider")
                                .setDeviceToken(token)
                                .setApiKey("Dashboard_api_key")
                                .setUserName("username") // login username
                                .setDeviceType(0)  // device type
                                .setShowLog(BuildConfig.DEBUG)
                                .setLanguage("en")
                                .build();
                        TookanAgentConfig.initTookanConfig(MainActivity.this, tookanConfigAttributes, Constants.SdkType.OPEN_UI , new OnTookanSDKListener() {
                            @Override
                            public void onSuccess( String agentID) {
                                Log.e("Tookan SDK Success ", agentID);
                            }

                            @Override
                            public void onFailure( String error) {
                                Log.e("Tookan SDK failed ", error);
                            }

                        });

                        findViewById(R.id.btnOpenAgentApp).setOnClickListener(v ->
                                /* for open agent app if sdk type is OPEN_UI */
                                TookanAgentConfig.getInstance().triggerToTookan(MainActivity.this, 123)
                        );
                    }
                });

    }


    private void tookanMethods(){

        /* for open agent app if sdk type is OPEN_UI */
        TookanAgentConfig.getInstance().triggerToTookan(MainActivity.this, 123);

        /* for agent on duty */
        TookanAgentConfig.getInstance().turnOnFleetDuty(MainActivity.this, new OnTookanSDKListener() {
            @Override
            public void onSuccess( String agentID) {

            }

            @Override
            public void onFailure( String error) {

            }
        });

        /* for agent off duty */
        TookanAgentConfig.getInstance().turnOffFleetDuty(MainActivity.this, new OnTookanSDKListener() {
            @Override
            public void onSuccess( String agentID) {

            }

            @Override
            public void onFailure( String error) {

            }
        });

        /* for agent log out */
        TookanAgentConfig.getInstance().logoutTookanSDK(MainActivity.this, new OnTookanSDKListener(){
            @Override
            public void onSuccess( String agentID) {

            }

            @Override
            public void onFailure( String error) {

            }
        });




        /* If you want to clear all sessions or data then call */
        TookanAgentConfig.clearAllSessions(MainActivity.this);


        /* for job status changed */
        TookanAgentConfig.getInstance().changeJobStatus(MainActivity.this, Constants.TaskStatus.STARTED.value + "", "260902840", "", new OnJobStatusListener() {
            @Override
            public void onSuccess( CommonResponse commonResponse) {
                Log.e("Job Status Success ", commonResponse.getMessage());
            }

            @Override
            public void onFailure( APIError error) {
                Log.e("Job Status Error ", error.getMessage());
            }
        });


    }

    /* Compare two face via file or imageURL*/
    public void compareTwoFace() {
//       File UserImageFile = new File(getDirectory(Constants.FileType.IMAGE_FILE), "Image_27052021_060019.jpg");
//        TookanAgentConfig.getInstance().compareTwoFace(this, UserImageFile, new AppManager.OnFaceCompareResult() {
//            @Override
//            public void onResult(Float confidence) {
//                if (confidence >= Constants.FaceCompare.THRESHOLD) {
//                    Log.w("Comapre face Result","Compare Face Matched");
//                } else
//                    Log.w("Comapre face Result","Compare Face not matched ");
//            }
//
//            @Override
//            public void onError(String error) {
//                Log.w("Compare Face Error ", error);
//            }
//        });

        String userImageUrl = "https://tookan.s3.amazonaws.com/fleet_profile/L3zE1605682382964-Image18112020122300.jpg";
        TookanAgentConfig.getInstance().compareTwoFace(this, userImageUrl, new AppManager.OnFaceCompareResult() {
            @Override
            public void onResult(Float confidence) {
                if (confidence >= Constants.FaceCompare.THRESHOLD) {
                    Log.e("Comapre face Result","Compare Face Matched");
                }
                else
                    Log.e("Comapre face Result","Compare Face not matched ");
            }

            @Override
            public void onError(String error) {
                Log.e("Compare Face Error ", error);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        TookanAgentConfig.onActivityResult(this, requestCode, resultCode, data);
    }

}
