package com.st.leighton.lingobarterclient;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.ibm.watson.developer_cloud.language_translation.v2.LanguageTranslation;
import com.ibm.watson.developer_cloud.language_translation.v2.model.Language;
import com.ibm.watson.developer_cloud.language_translation.v2.model.TranslationResult;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.http.HttpUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

class GlobalStore {
    private String token;
    private String name;
    private String userid;
    private String username;

    private static GlobalStore globalStore;

    public HashMap<String, UserInfoBundle> searchResults;

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
    private static Webservice instance = null;

    @Override
    public void onCreate() {
        instance = this;
        super.onCreate();
    }

    public static Webservice getInstance()
    {
        if (instance == null) instance = new Webservice();
        return instance;
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
                        GlobalStore.getInstance().setToken(jsonResult.getJSONObject("response").getString("auth_token"));
                        GlobalStore.getInstance().setName(jsonResult.getJSONObject("response").getString("name"));
                        GlobalStore.getInstance().setUsername(jsonResult.getJSONObject("response").getString("username"));
                        GlobalStore.getInstance().setUserid(jsonResult.getJSONObject("response").getString("user_id"));
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
                        GlobalStore.getInstance().setToken(jsonResult.getJSONObject("response").getString("auth_token"));
                        GlobalStore.getInstance().setName(jsonResult.getJSONObject("response").getString("name"));
                        GlobalStore.getInstance().setUsername(jsonResult.getJSONObject("response").getString("username"));
                        GlobalStore.getInstance().setUserid(jsonResult.getJSONObject("response").getString("user_id"));
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

        Intent intent = new Intent("android.intent.action.ApplicationSettings");
        intent.putExtra(ApplicationSettings.APPLICATION_SETTINGS_FEEDBACK, feedback);
        getInstance().sendBroadcast(intent);
    }

    public void Search(int ageRangeFrom, int ageRangeTo,
                       HashMap<String, Integer> teachLanguages, HashMap<String, Integer> learnLanguages,
                       HashSet<String> nationalities) {
        WebserviceClient client
                = new WebserviceClient(WebserviceClient.METHOD.Post, "/api/v1/search");
        client.AddHeader("content-type", "application/json");
        client.AddHeader("Authentication-Token", GlobalStore.getInstance().getToken());

        if (ageRangeFrom != -1 && ageRangeTo != -1) {
            JSONArray ageRange = new JSONArray();
            ageRange.put(ageRangeFrom);
            ageRange.put(ageRangeTo);
            client.AddPayload("age_range", ageRange);
        }

        if (teachLanguages != null && !teachLanguages.isEmpty()) {
            JSONArray array_teach = new JSONArray();
            try {
                for (Map.Entry<String, Integer> entry : teachLanguages.entrySet()) {
                    JSONObject temp = new JSONObject();
                    temp.put("language_id", entry.getKey());
                    temp.put("level", entry.getValue());
                    array_teach.put(temp);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            client.AddPayload("teach_langs", array_teach);
        }

        if(learnLanguages != null && !learnLanguages.isEmpty()) {
            JSONArray array_learn = new JSONArray();
            try {
                for (Map.Entry<String, Integer> entry : learnLanguages.entrySet()) {
                    JSONObject temp = new JSONObject();
                    temp.put("language_id", entry.getKey());
                    JSONArray temp_arr = new JSONArray();
                    temp_arr.put(entry.getValue());
                    temp_arr.put(5);
                    temp.put("level", temp_arr);
                    array_learn.put(temp);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            client.AddPayload("learn_langs", array_learn);
        }

        if(nationalities != null && !nationalities.isEmpty()) {
            JSONArray n_arr = new JSONArray();
            for (String nation : nationalities) {
                n_arr.put(nation);
            }
            client.AddPayload("nationality", n_arr);
        }

        client.Execute();
        Boolean flag = client.Waiting();

        Log.d("Feedback", client.getRawResult());

        String feedback = "";
        HashMap<String, UserInfoBundle> userProfiles = GlobalStore.getInstance().searchResults;
        if (userProfiles == null) {
            userProfiles = new HashMap<>();
        } else {
            userProfiles.clear();
        }

        if (flag) {
            JSONObject jsonResult = client.getJSON();
            try {
                int status = jsonResult.getInt("status");
                Log.d("Feedback", Integer.toString(status));
                switch (status) {
                    case 200:
                        feedback = "Succeed";
                        JSONArray array = jsonResult.getJSONArray("response");

                        for (int i = 0; i < array.length(); ++i) {
                            String username_str = "";
                            JSONObject user = array.getJSONObject(i);
                            UserInfoBundle userInfo = new UserInfoBundle();

                            JSONObject name = user.getJSONObject("name");
                            if (name != JSONObject.NULL) {
                                userInfo.setName(user.getString("name"));
                            }

                            JSONObject username = user.getJSONObject("username");
                            if (username != JSONObject.NULL) {
                                username_str = user.getString("username");
                                userInfo.setUsername(user.getString("username"));
                            }

                            JSONObject id = user.getJSONObject("id");
                            if (id != JSONObject.NULL) {
                                userInfo.setUserid(user.getString("id"));
                            }

                            JSONObject avatar_url = user.getJSONObject("avatar_url");
                            if (avatar_url != JSONObject.NULL) {
                                userInfo.setImageURL(user.getString("avatar_url"));
                            }

                            JSONObject gender = user.getJSONObject("gender");
                            if (gender != JSONObject.NULL) {
                                userInfo.setGender(user.getString("gender"));
                            }

                            JSONObject birthday = user.getJSONObject("birthday");
                            if (birthday != JSONObject.NULL) {
                                userInfo.setBirthday(user.getString("birthday"));
                            }

                            JSONObject tagline = user.getJSONObject("tagline");
                            if (tagline != JSONObject.NULL) {
                                userInfo.setTagline(user.getString("tagline"));
                            }

                            JSONObject bio = user.getJSONObject("bio");
                            if (bio != JSONObject.NULL) {
                                userInfo.setBiography(user.getString("bio"));
                            }

                            JSONObject location = user.getJSONObject("location");
                            if (location != JSONObject.NULL) {
                                userInfo.setCity(user.getString("location"));
                            }

                            JSONObject nationality = user.getJSONObject("nationality");
                            if (nationality != JSONObject.NULL) {
                                userInfo.setNationality(user.getString("nationality"));
                            }

                            JSONObject teach_langs = user.getJSONObject("teach_langs");
                            if (teach_langs != JSONObject.NULL) {
                                JSONArray langs = user.getJSONArray("teach_langs");
                                HashSet<String> teach_langs_bundle = new HashSet<>();

                                for (int j = 0; j < langs.length(); ++j) {
                                    JSONObject lang = array.getJSONObject(i);
                                    String lang_id = lang.getString("language_id");
                                    teach_langs_bundle.add(lang_id);
                                }

                                userInfo.setTeach_languages(teach_langs_bundle);
                            }

                            JSONObject learn_langs = user.getJSONObject("learn_langs");
                            if (learn_langs != JSONObject.NULL) {
                                JSONArray langs = user.getJSONArray("learn_langs");
                                HashMap<String, Integer> learn_langs_bundle = new HashMap<>();

                                for (int j = 0; j < langs.length(); ++j) {
                                    JSONObject lang = array.getJSONObject(i);
                                    String lang_id = lang.getString("language_id");
                                    int level = lang.getInt("level");
                                    learn_langs_bundle.put(lang_id, level);
                                }

                                userInfo.setLearn_languages(learn_langs_bundle);
                            }

                            userProfiles.put(username_str, userInfo);
                        }
                        break;

                    default:
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            feedback = "ERROR";
        }

        GlobalStore.getInstance().searchResults = userProfiles;

        Intent intent = new Intent("android.intent.action.Search");
        intent.putExtra(Search.USER_PROFILES_BUNDLE_KEY, feedback);
        getInstance().sendBroadcast(intent);
    }

    public void GetSelfInfo() {
        WebserviceClient client
                = new WebserviceClient(WebserviceClient.METHOD.Get, "/api/v1/users/" + GlobalStore.getInstance().getUsername());
        client.AddHeader("content-type", "application/json");
        client.AddHeader("Authentication-Token", GlobalStore.getInstance().getToken());

        client.Execute();
        Boolean flag = client.Waiting();

        String feedback = "";
        SelfInfoBundle infoBundle = new SelfInfoBundle();

        if (flag) {
            JSONObject jsonResult = client.getJSON();
            try {
                int status = jsonResult.getInt("status");
                switch (status) {
                    case 200:
                        JSONObject content = jsonResult.getJSONObject("response");

                        JSONObject username = content.getJSONObject("username");
                        if (username != JSONObject.NULL) {
                            infoBundle.setUsername(content.getString("username"));
                        }

                        JSONObject name = content.getJSONObject("name");
                        if (name != JSONObject.NULL) {
                            infoBundle.setName(content.getString("name"));
                        }

                        JSONObject gender = content.getJSONObject("gender");
                        if (gender != JSONObject.NULL) {
                            infoBundle.setGender(content.getString("gender"));
                        }

                        JSONObject birthday = content.getJSONObject("birthday");
                        if (birthday != JSONObject.NULL) {
                            infoBundle.setImageURL(content.getString("birthday"));
                        }

                        JSONObject avatar_url = content.getJSONObject("avatar_url");
                        if (avatar_url != JSONObject.NULL) {
                            infoBundle.setImageURL(content.getString("avatar_url"));
                        }

                        JSONObject nationality = content.getJSONObject("nationality");
                        if (nationality != JSONObject.NULL) {
                            infoBundle.setNationality(content.getString("nationality"));
                        }

                        JSONObject location = content.getJSONObject("location");
                        if (location != JSONObject.NULL) {
                            infoBundle.setLocation(content.getString("location"));
                        }

                        JSONObject teach_langs = content.getJSONObject("teach_langs");
                        if (teach_langs != JSONObject.NULL) {
                            JSONArray langs = content.getJSONArray("teach_langs");
                            HashSet<String> teach_langs_bundle = new HashSet<>();

                            for (int j = 0; j < langs.length(); ++j) {
                                JSONObject lang = langs.getJSONObject(j);
                                String lang_id = lang.getString("language_id");
                                teach_langs_bundle.add(lang_id);
                            }

                            infoBundle.setNativeLanguages(teach_langs_bundle);
                        }

                        JSONObject learn_langs = content.getJSONObject("learn_langs");
                        if (learn_langs != JSONObject.NULL) {
                            JSONArray langs = content.getJSONArray("learn_langs");
                            HashMap<String, Integer> learn_langs_bundle = new HashMap<>();

                            for (int j = 0; j < langs.length(); ++j) {
                                JSONObject lang = langs.getJSONObject(j);
                                String lang_id = lang.getString("language_id");
                                int level = lang.getInt("level");
                                learn_langs_bundle.put(lang_id, level);
                            }

                            infoBundle.setLearnLanguages(learn_langs_bundle);
                        }

                        JSONObject tagline = content.getJSONObject("tagline");
                        if (tagline != JSONObject.NULL) {
                            infoBundle.setTagline(content.getString("tagline"));
                        }

                        JSONObject bio = content.getJSONObject("bio");
                        if (bio != JSONObject.NULL) {
                            infoBundle.setBiography(content.getString("bio"));
                        }

                        feedback = "Succeed";
                        break;

                    case 404:
                        feedback = "NoUser";
                        break;

                    default:
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            feedback = "ERROR";
        }

        Intent intent = new Intent("android.intent.action.AccountSettings");
        intent.putExtra(ProfileSettings.ACCOUNT_INFORMATION_FEEDBACK, feedback);
        getInstance().sendBroadcast(intent);
    }

    public static void detectLanguageType(final String input) {
        String url = "http://gateway-a.watsonplatform.net/calls/text/TextGetLanguage";
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);

        String result = "";

        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("apikey", "4f20bfa1b1eeb6f1093b74dbc7077c439917bf7a"));
        urlParameters.add(new BasicNameValuePair("text", "Hello"));
        urlParameters.add(new BasicNameValuePair("outputMode", "json"));
        try {
            post.setEntity(new UrlEncodedFormEntity(urlParameters));
            HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity);
            Log.d("Translation", result);
//            System.out.println("Response Code : "
//                    + response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent intent = new Intent("android.intent.action.Detect");
        intent.putExtra("DETECTION", result);
        getInstance().sendBroadcast(intent);
    }

    public static void translateToEnglish(final String input, final Language language) {
        final LanguageTranslation service = new LanguageTranslation();
        service.setUsernameAndPassword("619d24ac-3713-45b7-883a-180c5dfa0121", "8fESEmEXIEBr");

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    TranslationResult translationResult = service.translate(input, language, Language.ENGLISH).execute();
                    String raw_result = translationResult.toString();
                    JSONObject json_result = new JSONObject(raw_result);

                    JSONArray translation_result = json_result.getJSONArray("translations");
                    for (int i = 0; i < translation_result.length(); ++i) {
                        JSONObject translation = translation_result.getJSONObject(i);
                        String result = translation.getString("translation");

                        Intent intent = new Intent("android.intent.action.Translate");
                        intent.putExtra("TRANSLATION", result);
                        getInstance().sendBroadcast(intent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void botchat(String input) {
        Intent intent = new Intent("android.intent.action.BotChat");
        intent.putExtra("RESPONSE", WebserviceClient.getResponse(input));
        getInstance().sendBroadcast(intent);
    }

    public static void translate(String input) {
        Intent intent = new Intent("android.intent.action.Translate");
        intent.putExtra("ANSWER", WebserviceClient.translate(input));
        getInstance().sendBroadcast(intent);
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
}
