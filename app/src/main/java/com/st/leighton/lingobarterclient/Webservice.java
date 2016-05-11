package com.st.leighton.lingobarterclient;

import android.app.Service;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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

public class Webservice extends Service{
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
                                     double latitude, double longitude, long birthday,
                                     HashSet<String> nativeLanguages, HashMap<String, Integer> learnLanguages) {
        //  Gender: true for male
        // Succeed; ERROR;

        WebserviceClient client
                = new WebserviceClient(WebserviceClient.METHOD.Put, "/api/v1/users");
        client.AddHeader("content-type", "application/json");
        client.AddHeader("Authentication-Token", GlobalStore.getInstance().getToken());

        client.AddPayload("name", name);
        client.AddPayload("gender", gender ? "male" : "female");
        client.AddPayload("nationality", nationality);
        JSONObject location = new JSONObject();
        try {
            location.put("type", "Point");
            JSONArray array = new JSONArray();
            array.put(longitude);
            array.put(latitude);
            location.put("coordinates", array);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        client.AddPayload("location", location);
        client.AddPayload("birthday", birthday);
        JSONArray array_teach = new JSONArray();
        JSONArray array_learn = new JSONArray();
        try {
            for (String lan : nativeLanguages) {
                JSONObject temp = new JSONObject();
                temp.put("language_id", lan);
                temp.put("level", 5);
                array_teach.put(temp);
            }

            for (Map.Entry<String, Integer> entry : learnLanguages.entrySet()) {
                JSONObject temp = new JSONObject();
                temp.put("language_id", entry.getKey());
                temp.put("level", entry.getValue());
                array_learn.put(temp);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        client.AddPayload("teach_langs", array_teach);
        client.AddPayload("learn_langs", array_learn);

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

        Intent intent = new Intent("android.intent.action.BasicProfile");
        intent.putExtra(BasicProfile.PROFILE_FEEDBACK, feedback);
        getInstance().sendBroadcast(intent);
    }

    public void SettingsUploadAvatar(String imagePath) {
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

        Intent intent = new Intent("android.intent.action.ProfileSettingsImage");
        intent.putExtra(ProfileSettings.PROFILE_IMAGE_FEEDBACK, feedback);
        getInstance().sendBroadcast(intent);
    }

    public void SettingsSetBasicProfile(String name, boolean gender, String nationality,
                                        double latitude, double longitude, double birthday,
                                        HashSet<String> nativeLanguages, HashMap<String, Integer> learnLanguages) {
        //  Gender: true for male

        WebserviceClient client
                = new WebserviceClient(WebserviceClient.METHOD.Put, "/api/v1/users");
        client.AddHeader("content-type", "application/json");
        client.AddHeader("Authentication-Token", GlobalStore.getInstance().getToken());

        client.AddPayload("name", name);
        client.AddPayload("gender", gender ? "male" : "female");
        client.AddPayload("nationality", nationality);
        JSONObject location = new JSONObject();
        try {
            location.put("type", "Point");
            JSONArray array = new JSONArray();
            array.put(longitude);
            array.put(latitude);
            location.put("coordinates", array);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        client.AddPayload("location", location);
        client.AddPayload("birthday", birthday);
        JSONArray array_teach = new JSONArray();
        JSONArray array_learn = new JSONArray();
        try {
            for (String lan : nativeLanguages) {
                JSONObject temp = new JSONObject();
                temp.put("language_id", lan);
                temp.put("level", 0);
                array_teach.put(temp);
            }

            for (Map.Entry<String, Integer> entry : learnLanguages.entrySet()) {
                JSONObject temp = new JSONObject();
                temp.put("language_id", entry.getKey());
                temp.put("level", entry.getValue());
                array_learn.put(temp);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        client.AddPayload("teach_langs", array_teach);
        client.AddPayload("learn_langs", array_learn);

        client.Execute();

        String feedback = "";
        Boolean flag = client.Waiting();

        if (flag) {
            JSONObject jsonResult = client.getJSON();
            Log.d("Testing", client.getRawResult());
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

        Intent intent = new Intent("android.intent.action.ProfileSettings");
        intent.putExtra(ProfileSettings.PROFILE_FEEDBACK, feedback);
        getInstance().sendBroadcast(intent);
    }

    public void ResetPassword(String oldPassword, String newPassword) {
        String feedback = "";
        // Succeed; InvalidPassword; ERROR

        WebserviceClient client
                = new WebserviceClient(WebserviceClient.METHOD.Put, "/api/v1/accounts/password");
        client.AddHeader("content-type", "application/json");
        client.AddHeader("Authentication-Token", GlobalStore.getInstance().getToken());
        client.AddPayload("cur_password", oldPassword);
        client.AddPayload("new_password", newPassword);
        client.Execute();

        Boolean flag = client.Waiting();

        if (flag) {
            JSONObject jsonResult = client.getJSON();
            try {
                int status = jsonResult.getInt("status");
                switch (status) {
                    case 200:
                        feedback = "Succeed";
                        break;

                    default:
                        feedback = "InvalidPassword";
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            feedback = "ERROR";
        }

        Intent intent = new Intent("android.intent.action.ChangePassword");
        intent.putExtra(ChangePassword.CHANGE_PASSWORD_FEEDBACK, feedback);
        getInstance().sendBroadcast(intent);
    }

    public void UpdateSelfInformation(String tagline, String biography) {
        String feedback = "";
        // Succeed; ERROR;

        WebserviceClient client
                = new WebserviceClient(WebserviceClient.METHOD.Put, "/api/v1/users");
        client.AddHeader("content-type", "application/json");
        client.AddHeader("Authentication-Token", GlobalStore.getInstance().getToken());

        client.AddPayload("tagline", tagline);
        client.AddPayload("bio", biography);

        client.Execute();

        Boolean flag = client.Waiting();

        if (flag) {
            JSONObject jsonResult = client.getJSON();
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

        Intent intent = new Intent("android.intent.action.SelfInformation");
        intent.putExtra(SelfInformation.SELF_INFORMATION_UPDATE_FEEDBACK, feedback);
        getInstance().sendBroadcast(intent);
    }

    public void UpdateApplicationSettings(boolean strictFlag, boolean sameGenderFlag,
            boolean nearbySearchFlag, boolean allSearchFlag, boolean partnerConfirmationFlag,
            HashSet<String> hideInformations, int ageRangeFrom, int ageRangeTo) {
        String feedback = "";
        // Succeed; ERROR;

        WebserviceClient client
                = new WebserviceClient(WebserviceClient.METHOD.Put, "/api/v1/users");
        client.AddHeader("content-type", "application/json");
        client.AddHeader("Authentication-Token", GlobalStore.getInstance().getToken());

        JSONObject settings = new JSONObject();
        try {
            settings.put("strict_lang_match", strictFlag);
            settings.put("same_gender", sameGenderFlag);
            settings.put("hide_from_nearby", nearbySearchFlag);
            settings.put("hide_from_search", allSearchFlag);
            settings.put("partner_confirmation", partnerConfirmationFlag);
            JSONArray hideInfo = new JSONArray();
            for (String field : hideInformations) {
                hideInfo.put(field);
            }
            settings.put("hide_info_fields", hideInfo);
            JSONArray ageRange = new JSONArray();
            ageRange.put(ageRangeFrom);
            ageRange.put(ageRangeTo);
            settings.put("age_range", ageRange);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        client.AddPayload("settings", settings);

        client.Execute();

        Boolean flag = client.Waiting();

        if (flag) {
            JSONObject jsonResult = client.getJSON();
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

        WebserviceClient client
                = new WebserviceClient(WebserviceClient.METHOD.Put, "/api/v1/search");
        client.AddHeader("content-type", "application/json");
        client.AddHeader("Authentication-Token", GlobalStore.getInstance().getToken());

        JSONArray array_teach = new JSONArray();
        JSONArray array_learn = new JSONArray();
        try {
            for (Map.Entry<String, Integer> entry : teachLanguages.entrySet()) {
                JSONObject temp = new JSONObject();
                temp.put("language_id", entry.getKey());
                temp.put("level", entry.getValue());
                array_teach.put(temp);
            }

            for (Map.Entry<String, Integer> entry : learnLanguages.entrySet()) {
                JSONObject temp = new JSONObject();
                temp.put("language_id", entry.getKey());
                JSONArray temp_arr = new JSONArray();
                temp_arr.put(0);
                temp_arr.put(entry.getValue());
                temp.put("level", temp_arr);
                array_learn.put(temp);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        client.AddPayload("teach_langs", array_teach);
        client.AddPayload("learn_langs", array_learn);

        JSONArray ageRange = new JSONArray();
        ageRange.put(ageRangeFrom);
        ageRange.put(ageRangeTo);
        client.AddPayload("age_range", ageRange);

        JSONArray n_arr = new JSONArray();
        for (String nation : nationalities) {
            n_arr.put(nation);
        }
        client.AddPayload("nationality", n_arr);

        client.Execute();

        Boolean flag = client.Waiting();

        if (flag) {
            JSONObject jsonResult = client.getJSON();
            try {
                int status = jsonResult.getInt("status");
                switch (status) {
                    case 200:
                        JSONArray array = jsonResult.getJSONArray("response");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject user = array.getJSONObject(i);
                            String name = user.getString("name");
                            String username = user.getString("username");
                            String avatar_url = user.getString("avatar_url");
                            String gender = user.getString("gender");
                            String birthday;
                            if (user.get("birthday") == null) {
                                birthday = "Unknown";
                            } else {
                                Date date = new Date();
                                date.setTime((long) user.getDouble("birthday"));
                                birthday = DateFormatUtils.format(date, "YYYY/MM/DD");
                            }
                            String tagline = user.getString("tagline");
                            String bio = user.getString("bio");
                            // GO TO SLEEP
                        }
                        break;

                    default:
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

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
