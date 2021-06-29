Tookan Agent SDK
===================================

Transforming the way businesses manage their field workforce.

Tookan is an off-the-shelf solution to manage and track, field workforces that enable on-demand deliveries, at-home services and on-street customer acquisition. Businesses will need to create an account on <a href="https://www.tookanapp.com">www.TookanApp.com</a>  to get started.

Using the Tookan Sdk, fleet members will never have to call the manager again to get the delivery information or to provide their status. The SDK does it all, automatically.

This SDK allows fleet members to:
&raquo; Get a birds eye view of all the assigned tasks.

Pre-requisites
--------------
- Android Support Repository
- Android compileSdkVersion 29
- Android buildToolsVersion "28.0.3"
- Android minSdkVersion 16
- Android targetSdkVersion 29

Getting Started
---------------
1 . Implementation

  Setup SDK with gradle

      implementation 'com.tookanapp.app:tookanagent:1.2.2'

  Add it in your root build.gradle at the end of repositories:

    allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}

   Do not forget to add internet permission in manifest if already not present

    <uses-permission android:name="android.permission.INTERNET"/>

  Enable multidex in your app build.gradle if required:

     android {
      defaultConfig {
          multiDexEnabled true
        }
     }

 2 . Add provider in AndroidManifest.xml

    <provider
        android:name="androidx.core.content.FileProvider"
        android:authorities="${applicationId}.provider"
        android:exported="false"
        android:grantUriPermissions="true">

    <meta-data
        android:name="android.support.FILE_PROVIDER_PATHS"
        android:resource="@xml/provider_paths"/>
    </provider>

    Create a folder named xml in app/src/main/res Create a file named provider_paths.xml
    in xml folder and add following code:

    <paths xmlns:android="http://schemas.android.com/apk/res/android">
        <external-path name="external_files" path="."/>
    </paths>

    For map:
     <meta-data
         android:name="com.google.android.geo.API_KEY"
         android:value="google_map_key" />

 3 . Initializing Tookan SDK

    Create the Application call and add the following lines in onCreate() method:

        public void onCreate() {
            TookanActivityLifecycleCallback.registerApplication(this);
            super.onCreate();
            ...
        }

    Add this code on your activity class where you want to open the sdk

        HashMap<String, String> customAttr = new HashMap<>();

        customAttr.put("app_name", "your app name");        // set your app name here(optional)

        CaptureUserData userData = new CaptureUserData.Builder()
               .userID(1L)                                  //set userId here(optional)
               .fullName("Ankush Walia")                    //set username here(optional)
               .email("ankush.walia@jungleworks.com")       //set emailId here(optional)
               .phoneNumber("")                             //set phone no here(optional)
               .countryCode("+91")                          //set country code here(optional)
               .latitude(0.0)                               //set latitude here(optional)
               .latitude(0.0)                               //set longitude here(optional)
               .customAttributes(customAttr)
               .build();

        TookanConfigAttributes tookanConfigAttributes = new TookanConfigAttributes.Builder()
               .setCaptureUserData(userData)
               .setProvider(BuildConfig.APPLICATION_ID + ".provider")
               .setDeviceToken(token)                        // FCM token (Required)
               .setApiKey("")                                // Add your dashboard api key here (Required)
               .setUserName("")                              // Agent username for login (Required)
               .setDeviceType()                              // Device type (Required)
               .setShowLog(BuildConfig.DEBUG)
               .setLanguage("en")
               .build();

        findViewById(R.id.btnOpenAgentApp).setOnClickListener(v ->

        TookanAgentConfig.getInstance().triggerToTookan(MainActivity.this,123));

        /* Constants.SdkType
            1. SILENT_LOGIN // when you don't want trigger agent sdk UI or just sdk works only for background
            2. OPEN_UI      // its refer to open agent ui
        */

        TookanAgentConfig.initTookanConfig(MainActivity.this, tookanConfigAttributes, Constants.SdkType.SILENT_LOGIN,  new OnTookanSDKListener() {
           @Override
           public void onSuccess() {
               findViewById(R.id.btnOpenAgentApp).setVisibility(View.VISIBLE);
           }

           @Override
           public void onFailure(@NotNull String error) {
               Log.w("Tookan SDK failed", error);
           }
        })

 4 . Use Agent Methods as per your needs:

         /* for open agent app if sdk type is OPEN_UI */
          TookanAgentConfig.getInstance().triggerToTookan(MainActivity.this, 123);

          /* for agent on duty */
          TookanAgentConfig.getInstance().turnOnFleetDuty(MainActivity.this, new OnTookanSDKListener() {
              @Override
              public void onSuccess(@NotNull String agentID) {

              }

              @Override
              public void onFailure(@NotNull String error) {

              }
          });

          /* for agent off duty */
          TookanAgentConfig.getInstance().turnOffFleetDuty(MainActivity.this, new OnTookanSDKListener() {
              @Override
              public void onSuccess(@NotNull String agentID) {

              }

              @Override
              public void onFailure(@NotNull String error) {

              }
          });

          /* for agent log out */
         TookanAgentConfig.getInstance().logoutTookanSDK(MainActivity.this, new OnTookanSDKListener(){
                     @Override
                     public void onSuccess(@NotNull String agentID) {

                     }

                     @Override
                     public void onFailure(@NotNull String error) {

                     }
                 });


          /* If you want to clear all sessions or data then call */
          TookanAgentConfig.clearAllSessions(MainActivity.this);


          /* for job status changed */
          TookanAgentConfig.getInstance().changeJobStatus(MainActivity.this, Constants.TaskStatus.STARTED.value + "", "260902840", "", new OnJobStatusListener() {
              @Override
              public void onSuccess(@NotNull CommonResponse commonResponse) {
                  Log.w("Job Status Success ", commonResponse.getMessage());
              }

              @Override
              public void onFailure(@NotNull APIError error) {
                  Log.w("Job Status Error ", error.getMessage());
              }
          });


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



 5 . Add Tookan Fcm in FirebaseMessaging class

         @Override
         public void onMessageReceived(RemoteMessage remoteMessage) {
         TookanNotificationConfig tookanNotification = TookanNotificationConfig.getInstance();
               if (tookanNotification.isTookanPushNotification(remoteMessage)) {
                   tookanNotification.setSmallIcon(R.mipmap.ic_tkn_notif);        //set your icon here
                   tookanNotification.setLargeIcon(R.mipmap.ic_tkn_launcher);
                   tookanNotification.handleTookanPushNotification(this, remoteMessage);
               }
          }






