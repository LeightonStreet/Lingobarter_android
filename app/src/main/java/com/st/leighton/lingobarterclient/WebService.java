package com.st.leighton.lingobarterclient;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import org.json.JSONObject;

public class WebService extends Service {
    public String token;
    private String name;
    private String userid;
    private String username;

    private static WebService instance = null;

    @Override
    public void onCreate() {
        instance = this;
        super.onCreate();
    }

    public static WebService getInstance()
    {
        if (instance == null) instance = new WebService();
        return instance;
    }

    public void Login(String email, String password) {
        WebServiceClient client
                = new WebServiceClient(WebServiceClient.METHOD.Post, "/api/v1/accounts/authorize");
        client.AddHeader("content-type", "application/json");
        client.AddPayload("email", email);
        client.AddPayload("password", password);
        client.Execute();
        Boolean flag = client.Waiting();

        if (flag) {
            JSONObject jsonResult = client.getJSON();
            try {
                int status = jsonResult.getInt("status");
                String feedback;

                switch (status) {
                    case 200:
                        token = jsonResult.getJSONObject("response").getString("auth_token");
                        name = jsonResult.getJSONObject("response").getString("name");
                        username = jsonResult.getJSONObject("response").getString("username");
                        userid = jsonResult.getJSONObject("response").getString("user_id");

                        feedback = "Succeed";
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

                Intent intent = new Intent("android.intent.action.Login");
                intent.putExtra(Login.LOGIN_FEEDBACK, feedback);
                getInstance().sendBroadcast(intent);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void Logout() {
        WebServiceClient client
                = new WebServiceClient(WebServiceClient.METHOD.Get, "/api/v1/accounts/unauthorize");
        client.AddHeader("content-type", "application/json");
        client.AddHeader("Authentication-Token", token);
        client.Execute();
        Boolean flag = client.Waiting();

        if(flag) {
            JSONObject jsonResult = client.getJSON();
            try {
                int status = jsonResult.getInt("status");

                switch (status) {
                    case 200:
                        Intent intent = new Intent("android.intent.action.Login");
                        intent.putExtra(Login.LOGIN_FEEDBACK, "Logout Succeed.");
                        getInstance().sendBroadcast(intent);
                        break;

                    default:

                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void UpdatePassword(String old_password, String new_password) {
        WebServiceClient client
                = new WebServiceClient(WebServiceClient.METHOD.Put, "/api/v1/accounts/password");
        client.AddHeader("content-type", "application/json");
        client.AddHeader("Authentication-Token", token);
        client.AddPayload("cur_password", old_password);
        client.AddPayload("new_password", new_password);
        client.Execute();
        Boolean flag = client.Waiting();

        if (flag) {
            JSONObject jsonResult = client.getJSON();
            try {
                int status = jsonResult.getInt("status");
                String feedback;

                switch (status) {
                    case 200:
                        feedback = "Succeed";
                        break;

                    case 406:
                        feedback = "InvalidPassword";
                        break;

                    default:
                        feedback = "";
                        break;
                }

//                Intent intent = new Intent("android.intent.action.Login");
//                intent.putExtra(Login.LOGIN_FEEDBACK, feedback);
//                getInstance().sendBroadcast(intent);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void ResetPassword(String email) {
        WebServiceClient client
                = new WebServiceClient(WebServiceClient.METHOD.Post, "/api/v1/accounts/password/reset");
        client.AddHeader("content-type", "application/json");
        client.AddPayload("email", email);
        client.Execute();
        Boolean flag = client.Waiting();

        if (flag) {
            JSONObject jsonResult = client.getJSON();
            try {
                int status = jsonResult.getInt("status");
                String feedback;

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

                Intent intent = new Intent("android.intent.action.ForgetPassword");
                intent.putExtra(ForgetPassword.FORGET_PASSWORD_FEEDBACK, feedback);
                getInstance().sendBroadcast(intent);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void UpdateUsername(String username) {
        WebServiceClient client
                = new WebServiceClient(WebServiceClient.METHOD.Put, "/api/v1/accounts/username");
        client.AddHeader("content-type", "application/json");
        client.AddHeader("Authentication-Token", token);
        client.AddPayload("username", username);
        client.Execute();
        Boolean flag = client.Waiting();

        if (flag) {
            JSONObject jsonResult = client.getJSON();
            try {
                int status = jsonResult.getInt("status");
                String feedback;

                switch (status) {
                    case 200:
                        feedback = "Succeed";
                        break;

                    case 304:
                        feedback = "Unchanged";
                        break;

                    case 409:
                        feedback = "UsernameInvalid";
                        break;

                    default:
                        feedback = "";
                        break;
                }

//                Intent intent = new Intent("android.intent.action.Login");
//                intent.putExtra(Login.LOGIN_FEEDBACK, feedback);
//                getInstance().sendBroadcast(intent);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void ConfirmEmail(String email) {

    }

    public void Signup(String username, String email, String password) {

    }

    public void UpdateProfile() {

    }

    public void ViewSelfProfile() {

    }

    public void DeleteAccount() {

    }

    public void ViewOtherProfile() {

    }

    public void UploadAvatar() {

    }

    public void Search() {
        
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
