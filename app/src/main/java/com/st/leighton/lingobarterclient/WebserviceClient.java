package com.st.leighton.lingobarterclient;

import android.util.Log;

import com.st.leighton.util.MyProperty;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

/**
 * Created by xuhao on 2016/5/8.
 */
public class WebserviceClient {
    public enum METHOD {
        Post, Get, Put, Delete
    }

    METHOD method;

    private String host;
    private Integer port;
    private String protocol;

    private JSONObject content;

    private StringEntity stringEntity;
    private DefaultHttpClient httpClient;
    private HttpHost httpHost;
    private HttpGet httpGet;
    private HttpPut httpPut;
    private HttpPost httpPost;
    private HttpEntity httpEntity;
    private HttpResponse httpResponse;

    private Boolean finished = false;

    private String status;
    private String raw_result;
    private Header[] headers;
    private JSONObject json_result;

    public WebserviceClient(METHOD p_method, String p_target) {
        host = MyProperty.getProperty("host");
        port = MyProperty.getIntProperty("port");
        protocol = MyProperty.getProperty("protocol");

        method = p_method;
        content = new JSONObject();

        switch (method) {
            case Post:
                httpPost = new HttpPost(p_target);
                break;

            case Get:
                httpGet = new HttpGet(p_target);
                break;

            case Put:
                httpPut = new HttpPut(p_target);
                break;

            default:
                break;
        }
    }

    public void AddHeader(String p_key, String p_value) {
        switch (method) {
            case Post:
                httpPost.addHeader(p_key, p_value);
                break;

            case Get:
                httpGet.addHeader(p_key, p_value);
                break;

            case Put:
                httpPut.addHeader(p_key, p_value);
                break;

            default:
                break;
        }
    }

    public void AddPayload(String p_key, String p_value) {
        try {
            content.put(p_key, p_value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Boolean Waiting() {
        int cycles = 0;
        while (!finished) {
            cycles += 1;
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (Exception e) {
                return false;
            }
            if (cycles == 100) return false;
        }
        return true;
    }

    public String getStatus() {
        return status;
    }

    public String getRawResult() {
        return raw_result;
    }

    public JSONObject getJSON() {
        return json_result;
    }

    public Vector<String> getAllHeaders() {
        Vector<String> result = new Vector<>();
        for (int i = 0; i < headers.length; ++i) result.addElement(headers[i].toString());
        return result;
    }

    public static String getResponse(String in) {
        HttpClient httpClient = new DefaultHttpClient();
        String send = "";
        try {
            send = URLEncoder.encode(in, "utf-8");
        } catch (Exception e) { e.printStackTrace(); }
        String send_url = "http://api.program-o.com/v2/chatbot/?bot_id=6&say=" + send + "&convo_id=exampleusage_1231232&format=json";
        HttpGet httpGet = new HttpGet(send_url);
        HttpResponse httpResponse;

        try {
            httpResponse = httpClient.execute(httpGet);
            Log.i("Pa", httpResponse.getStatusLine().toString());
            HttpEntity response = httpResponse.getEntity();
            if (response != null) {
                String raw_result = EntityUtils.toString(response);
                Log.d("Reply", raw_result);
                JSONObject result = new JSONObject(raw_result);
                return result.getString("botsay");
            }
        } catch (Exception e) { e.printStackTrace(); }

        return "";
    }

    public void Execute() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    httpClient = new DefaultHttpClient();
                    httpHost = new HttpHost(host, port, protocol);

                    stringEntity = new StringEntity(content.toString());

                    switch (method) {
                        case Post:
                            httpPost.setEntity(stringEntity);
                            httpResponse = httpClient.execute(httpHost, httpPost);
                            break;

                        case Get:
                            httpResponse = httpClient.execute(httpHost, httpGet);
                            break;

                        case Put:
                            httpPut.setEntity(stringEntity);
                            httpResponse = httpClient.execute(httpHost, httpPut);
                            break;

                        default:
                            break;
                    }

                    if (httpResponse != null) {
                        status = httpResponse.getStatusLine().toString();
                        headers = httpResponse.getAllHeaders();
                        httpEntity = httpResponse.getEntity();

                        if (httpEntity != null) {
                            raw_result = EntityUtils.toString(httpEntity);
                            System.out.println(raw_result);
                            json_result = new JSONObject(raw_result);
                        }
                    }
                    finished = true;
                } catch (Exception e) {
                    finished = true;
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void ExecuteWithCustomEntity() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    httpClient = new DefaultHttpClient();
                    httpHost = new HttpHost(host, port, protocol);

                    switch (method) {
                        case Post:
                            httpResponse = httpClient.execute(httpHost, httpPost);
                            break;

                        case Get:
                            httpResponse = httpClient.execute(httpHost, httpGet);
                            break;

                        case Put:
                            httpResponse = httpClient.execute(httpHost, httpPut);
                            break;

                        default:
                            break;
                    }

                    if (httpResponse != null) {
                        status = httpResponse.getStatusLine().toString();
                        headers = httpResponse.getAllHeaders();
                        httpEntity = httpResponse.getEntity();

                        if (httpEntity != null) {
                            raw_result = EntityUtils.toString(httpEntity);
                            System.out.println(raw_result);
                            json_result = new JSONObject(raw_result);
                        }
                    }
                    finished = true;
                } catch (Exception e) {
                    finished = true;
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public HttpGet getHttpGet() {
        return httpGet;
    }

    public HttpPut getHttpPut() {
        return httpPut;
    }

    public HttpPost getHttpPost() {
        return httpPost;
    }

    public HttpHost getHttpHost() {
        return httpHost;
    }
}