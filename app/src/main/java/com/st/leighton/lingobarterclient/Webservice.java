package com.st.leighton.lingobarterclient;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.HashSet;

public class Webservice extends Service {
    private String token;
    private String name;
    private String userid;
    private String username;

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
        String feedback = "";
        // Succeed; ERROR;

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

        Bundle passToSearch = new Bundle();
        passToSearch.putSerializable(Search.USER_PROFILES_BUNDLE_KEY, userProfiles);

        Intent intent = new Intent("android.intent.action.Search");
        intent.putExtras(passToSearch);
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
