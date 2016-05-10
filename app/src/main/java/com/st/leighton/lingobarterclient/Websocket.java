package com.st.leighton.lingobarterclient;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import org.json.JSONObject;

public class Websocket extends Service {
    private String token;
    private String name;
    private String userid;
    private String username;

    private static Websocket instance = null;

    @Override
    public void onCreate() {
        instance = this;
        super.onCreate();
    }

    public static Websocket getInstance()
    {
        if (instance == null) instance = new Websocket();
        return instance;
    }

    public void Login(String email, String password) {
        WebsocketClient client
                = new WebsocketClient(WebsocketClient.METHOD.Post, "/api/v1/accounts/authorize");
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
        WebsocketClient client
                = new WebsocketClient(WebsocketClient.METHOD.Post, "/api/v1/accounts/password/reset");
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
        WebsocketClient client
                = new WebsocketClient(WebsocketClient.METHOD.Post, "/api/v1/users");
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
        WebsocketClient client
                = new WebsocketClient(WebsocketClient.METHOD.Post, "/api/v1/accounts/confirm");
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
        WebsocketClient client
                = new WebsocketClient(WebsocketClient.METHOD.Post, "/api/v1/accounts/authorize");
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
