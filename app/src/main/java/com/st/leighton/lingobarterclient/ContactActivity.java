package com.st.leighton.lingobarterclient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lingobarter.socketclient.WebsocketClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.Timestamp;
import java.util.ArrayList;

import chat.adapter.ContactAdapter;
import chat.adapter.MessageAdapter;
import chat.bean.Contact;
import io.socket.emitter.Emitter;

/**
 * Created by vicky on 5/12/16.
 */
public class ContactActivity extends Activity {
    Context baseContext;
    private WebsocketClient mSocket = null;
    private ArrayList<Contact> partners = new ArrayList<>();
    private View layout_head;
    private ListView lvContact;
    private TextView mDialogText;
    private WindowManager mWindowManager;

    private ContactAdapter adapter;

    private Button BtnAddPartner;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MyApplication app = (MyApplication) getApplication();
        mSocket = app.getSocket();
        mSocket.on("ret:add partner", new Emitter.Listener() {
            public void call(Object... args) {
                JSONObject obj = (JSONObject) args[0];
                System.out.println(obj);
                try{
                    JSONObject response = obj.getJSONObject("response");
                    int status = response.getInt("status");
                    if(status == 200){
                        JSONObject user = response.getJSONObject("to_user");
                        double birthday = user.getDouble("birthday");
                        Contact new_partner = new Contact(birthday/1000.0,
                                user.getString("username"), user.getString("name"),
                                user.getString("avatar_url"), user.getString("tagline"),
                                user.getString("gender"), user.getString("bio"),
                                user.getJSONObject("location"), user.getString("nationality"),
                                user.getJSONArray("learn_langs"), user.getJSONArray("teach_langs"));
                        partners.add(new_partner);
                        System.out.println("add partner successfully");
                        adapter.refresh(partners);
                    }
                }catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("fail to send message");
                }
            }
        });

        baseContext = this;
//        initViews();
    }

    private void initViews() {
        setContentView(R.layout.activity_main);
//        lvContact = (ListView) findViewById(R.id.lvContact);
        adapter = new ContactAdapter(this, partners);
        lvContact.setAdapter(adapter);
        lvContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int i, long l) {
                Contact friend = partners.get(i);
                Intent intent = new Intent(getApplicationContext(), UserProfile.class);
                intent.putExtra("USER_NAME", friend.getUserName());
                startActivity(intent);
            }
        });

        mDialogText = (TextView) LayoutInflater.from(baseContext).inflate(
                R.layout.list_position, null);
        mDialogText.setVisibility(View.INVISIBLE);
        mWindowManager = (WindowManager) baseContext.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        mWindowManager.addView(mDialogText, lp);
        layout_head = getLayoutInflater().inflate(
                R.layout.layout_head_friend, null);
        lvContact.addHeaderView(layout_head);

//        BtnAddPartner = (Button) findViewById(R.id.btn_add_partner);
        BtnAddPartner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(baseContext, Search.class);
                startActivity(intent);
            }
        });
    }
}
