package com.st.leighton.lingobarterclient;

import android.app.Service;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.IBinder;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

class GlobalStore {
    private String token;
    private String name;
    private String userid;
    private String username;

    private static GlobalStore globalStore;

    private GlobalStore() {}

    static {
        globalStore = new GlobalStore();
    }

    public static GlobalStore getInstance() {
        return globalStore;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

public class Webservice extends Service {
    private String token;
    private String name;
    private String userid;
    private String username;

    private Geocoder geocoder;
    private static Webservice instance = null;

    @Override
    public void onCreate() {
        geocoder = new Geocoder(this, Locale.getDefault());

        instance = this;
        super.onCreate();
    }

    public static Webservice getInstance()
    {
        if (instance == null) instance = new Webservice();
        return instance;
    }

    public String getToken()
    {
        if (token != null) {
            return token;
        } else {
            return "NONE";
        }
    }

    public void Login(String email, String password) {
        WebserviceClient client
                = new WebserviceClient(WebserviceClient.METHOD.Post, "/api/v1/accounts/authorize");
        client.AddHeader("content-type", "application/json");
        client.AddPayload("email", email);
        client.AddPayload("password", password);
        client.Execute();

        String feedback = "";
        Boolean flag = client.Waiting();

        if (flag) {
            JSONObject jsonResult = client.getJSON();
            try {
                int status = jsonResult.getInt("status");
                switch (status) {
                    case 200:
                        boolean completeFlag = jsonResult.getJSONObject("response").getBoolean("complete");
                        token = jsonResult.getJSONObject("response").getString("auth_token");
                        name = jsonResult.getJSONObject("response").getString("name");
                        username = jsonResult.getJSONObject("response").getString("username");
                        userid = jsonResult.getJSONObject("response").getString("user_id");
                        GlobalStore.getInstance().setToken(token);
                        GlobalStore.getInstance().setName(name);
                        GlobalStore.getInstance().setUsername(username);
                        GlobalStore.getInstance().setUserid(userid);
                        if (completeFlag) {
                            feedback = "Succeed";
                        } else {
                            feedback = "Uncomplete";
                        }
                        break;

                    case 403:
                        feedback = "NeedConfirm";
                        break;

                    case 406:
                        feedback = "InvalidPassword";
                        break;

                    case 404:
                        feedback = "InvalidUser";
                        break;

                    default:
                        feedback = "";
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            feedback = "ERROR";
        }

        Intent intent = new Intent("android.intent.action.Login");
        intent.putExtra(Login.LOGIN_FEEDBACK, feedback);
        getInstance().sendBroadcast(intent);
    }

    public void ResetPassword(String email) {
        WebserviceClient client
                = new WebserviceClient(WebserviceClient.METHOD.Post, "/api/v1/accounts/password/reset");
        client.AddHeader("content-type", "application/json");
        client.AddPayload("email", email);
        client.Execute();

        String feedback = "";
        Boolean flag = client.Waiting();

        if (flag) {
            JSONObject jsonResult = client.getJSON();
            try {
                int status = jsonResult.getInt("status");

                switch (status) {
                    case 200:
                        feedback = "Succeed";
                        break;

                    case 404:
                        feedback = "InvalidUser";
                        break;

                    default:
                        feedback = "";
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            feedback = "ERROR";
        }

        Intent intent = new Intent("android.intent.action.ForgetPassword");
        intent.putExtra(ForgetPassword.FORGET_PASSWORD_FEEDBACK, feedback);
        getInstance().sendBroadcast(intent);
    }

    public void Register(String username, String email, String password) {
        WebserviceClient client
                = new WebserviceClient(WebserviceClient.METHOD.Post, "/api/v1/users");
        client.AddHeader("content-type", "application/json");
        client.AddPayload("username", username);
        client.AddPayload("email", email);
        client.AddPayload("password", password);
        client.Execute();

        String feedback = "";
        Boolean flag = client.Waiting();

        if (flag) {
            JSONObject jsonResult = client.getJSON();
            try {
                int status = jsonResult.getInt("status");

                switch (status) {
                    case 200:
                        feedback = "Succeed";
                        break;

                    case 409:
                        feedback = "EmailTaken";
                        break;

                    default:
                        feedback = "";
                        break;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            feedback = "ERROR";
        }

        Intent intent = new Intent("android.intent.action.Register");
        intent.putExtra(Register.REGISTER_FEEDBACK, feedback);
        getInstance().sendBroadcast(intent);
    }

    public void Confirm(String email) {
        WebserviceClient client
                = new WebserviceClient(WebserviceClient.METHOD.Post, "/api/v1/accounts/confirm");
        client.AddHeader("content-type", "application/json");
        client.AddPayload("email", email);
        client.Execute();

        String feedback = "";
        Boolean flag = client.Waiting();

        if (flag) {
            JSONObject jsonResult = client.getJSON();
            try {
                int status = jsonResult.getInt("status");
                switch (status) {
                    case 200:
                        feedback = "Succeed";
                        break;

                    case 404:
                        feedback = "InvalidUser";
                        break;

                    default:
                        feedback = "";
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            feedback = "ERROR";
        }

        Intent intent = new Intent("android.intent.action.EmailConfirmation");
        intent.putExtra(EmailConfirmation.EMAIL_CONFIRMATION_FEEDBACK, feedback);
        getInstance().sendBroadcast(intent);
    }

    public void EmailConfirmationLogin(String email, String password) {
        WebserviceClient client
                = new WebserviceClient(WebserviceClient.METHOD.Post, "/api/v1/accounts/authorize");
        client.AddHeader("content-type", "application/json");
        client.AddPayload("email", email);
        client.AddPayload("password", password);
        client.Execute();

        String feedback = "";
        Boolean flag = client.Waiting();

        if (flag) {
            JSONObject jsonResult = client.getJSON();
            try {
                int status = jsonResult.getInt("status");
                switch (status) {
                    case 200:
                        token = jsonResult.getJSONObject("response").getString("auth_token");
                        name = jsonResult.getJSONObject("response").getString("name");
                        username = jsonResult.getJSONObject("response").getString("username");
                        userid = jsonResult.getJSONObject("response").getString("user_id");
                        GlobalStore.getInstance().setToken(token);
                        GlobalStore.getInstance().setName(name);
                        GlobalStore.getInstance().setUsername(username);
                        GlobalStore.getInstance().setUserid(userid);
                        feedback = "Succeed";
                        break;

                    default:
                        feedback = "";
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            feedback = "ERROR";
        }

        Intent intent = new Intent("android.intent.action.EmailConfirmationLogin");
        intent.putExtra(EmailConfirmation.EMAIL_CONFIRMATION_LOGIN_FEEDBACK, feedback);
        getInstance().sendBroadcast(intent);
    }

    public void BasicUploadAvatar(String imagePath) {
        WebserviceClient client
                = new WebserviceClient(WebserviceClient.METHOD.Put, "/api/v1/users/upload");

        File file = new File(imagePath);
        client.AddHeader("Authentication-Token", GlobalStore.getInstance().getToken());

        HttpEntity httpEntity = MultipartEntityBuilder.create()
                .addBinaryBody("image", file, ContentType.DEFAULT_BINARY, file.getName())
                .build();

        client.AddHeader("Authentication-Token", GlobalStore.getInstance().getToken());
        client.getHttpPut().setEntity(httpEntity);

        client.ExecuteWithCustomEntity();

        String feedback = "";
        Boolean flag = client.Waiting();

        if (flag) {
            JSONObject jsonResult = client.getJSON();
            System.out.println(jsonResult);
            try {
                int status = jsonResult.getInt("status");
                switch (status) {
                    case 200:
                        feedback = "Succeed";
                        break;

                    default:
                        feedback = "";
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            feedback = "ERROR";
        }

        Intent intent = new Intent("android.intent.action.BasicProfileImage");
        intent.putExtra(BasicProfile.PROFILE_IMAGE_FEEDBACK, feedback);
        getInstance().sendBroadcast(intent);
    }

    public void BasicSetBasicProfile(String name, boolean gender, String nationality,
                                     double latitude, double longitude, double birthday,
                                     HashSet<String> nativeLanguages, HashMap<String, Integer> learnLanguages) {
        //  Gender: true for male

        String feedback = "";
        // Succeed; ERROR;

        Intent intent = new Intent("android.intent.action.BasicProfile");
        intent.putExtra(BasicProfile.PROFILE_FEEDBACK, feedback);
        getInstance().sendBroadcast(intent);
    }

    public void SettingsUploadAvatar(String imagePath) {
        String feedback = "";
        // Succeed; ERROR;

        Intent intent = new Intent("android.intent.action.ProfileSettingsImage");
        intent.putExtra(ProfileSettings.PROFILE_IMAGE_FEEDBACK, feedback);
        getInstance().sendBroadcast(intent);
    }

    public void SettingsSetBasicProfile(String name, boolean gender, String nationality,
                                        double latitude, double longitude, double birthday,
                                        HashSet<String> nativeLanguages, HashMap<String, Integer> learnLanguages) {
        //  Gender: true for male

        String feedback = "";
        // Succeed; ERROR;

        Intent intent = new Intent("android.intent.action.ProfileSettings");
        intent.putExtra(ProfileSettings.PROFILE_FEEDBACK, feedback);
        getInstance().sendBroadcast(intent);
    }

    public void ResetPassword(String oldPassword, String newPassword) {
        String feedback = "";
        // Succeed; InvalidPassword; ERROR

        Intent intent = new Intent("android.intent.action.ChangePassword");
        intent.putExtra(ChangePassword.CHANGE_PASSWORD_FEEDBACK, feedback);
        getInstance().sendBroadcast(intent);
    }

    public void UpdateSelfInformation(String tagline, String biography) {
        String feedback = "";
        // Succeed; ERROR;

        Intent intent = new Intent("android.intent.action.SelfInformation");
        intent.putExtra(SelfInformation.SELF_INFORMATION_UPDATE_FEEDBACK, feedback);
        getInstance().sendBroadcast(intent);
    }

    public void UpdateApplicationSettings(boolean strictFlag, boolean sameGenderFlag,
            boolean nearbySearchFlag, boolean allSearchFlag, boolean partnerConfirmationFlag,
            HashSet<String> hideInformations, int ageRangeFrom, int ageRangeTo) {
        String feedback = "";
        // Succeed; ERROR;

        Intent intent = new Intent("android.intent.action.SelfInformation");
        intent.putExtra(SelfInformation.SELF_INFORMATION_UPDATE_FEEDBACK, feedback);
        getInstance().sendBroadcast(intent);
    }

    public void Search(int ageRangeFrom, int ageRangeTo,
                       HashMap<String, Integer> teachLanguages, HashMap<String, Integer> learnLanguages,
                       HashSet<String> nationalities) {
        HashMap<String, UserInfoBundle> userProfiles = new HashMap<>();
        // Fill userProfiles, using user name as key.
        // If network error, pass a empty userProfiles.

        Bundle passToSearch = new Bundle();
        passToSearch.putSerializable(Search.USER_PROFILES_BUNDLE_KEY, userProfiles);

        Intent intent = new Intent("android.intent.action.Search");
        intent.putExtras(passToSearch);
        getInstance().sendBroadcast(intent);
    }

    public void GetSelfInfo() {
        SelfInfoBundle selfInfo = new SelfInfoBundle();
        // Fill selfInfo.
        // If network error, pass un-initialized selfInfo.

        Bundle passToSettings = new Bundle();
        // Use selfInfo.setBundle() to initialize all the information fields.
//        passToSettings.putSerializable(TESTING.SELF_INFO_KEY, selfInfo);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public String getCityByGeo(double latitude, double longtitude) {
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longtitude, 1);
            return addresses.get(0).getLocality();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
