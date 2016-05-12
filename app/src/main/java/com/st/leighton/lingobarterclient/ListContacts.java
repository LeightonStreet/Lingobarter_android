package com.st.leighton.lingobarterclient;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.st.leighton.util.MyProperty;

import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;

import chat.bean.User;
import chat.widget.SideBar;

/**
 * Created by vicky on 5/12/16.
 */
public class ListContacts extends Activity {
    Context baseContext;
    private Socket mSocket = null;
    private ArrayList<User> partners = new ArrayList<>();
    private View layout, layout_head;
    private ListView lvContact;
    private SideBar indexBar;
    private TextView mDialogText;
    private WindowManager mWindowManager;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horizontal_ntb);

        MyApplication app = (MyApplication) getApplication();
        mSocket = app.getSocket();
        baseContext = this;
        mSocket.on("ret:browse chats", new Emitter.Listener() {
            public void call(Object... args) {
                JSONObject obj = (JSONObject) args[0];
                System.out.println("browse chats");
                System.out.println(obj);
            }
        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            public void call(Object... args) {
                System.out.println("You disconnect me");
            }
        });
        mSocket.connect();
        initViews();
    }

    private void initViews() {
        lvContact = (ListView) layout.findViewById(R.id.lvContact);

        mDialogText = (TextView) LayoutInflater.from(baseContext).inflate(
                R.layout.list_position, null);
        mDialogText.setVisibility(View.INVISIBLE);
        indexBar = (SideBar) layout.findViewById(R.id.sideBar);
        indexBar.setListView(lvContact);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        mWindowManager.addView(mDialogText, lp);
        indexBar.setTextView(mDialogText);
        layout_head = getLayoutInflater().inflate(
                R.layout.layout_head_friend, null);
        lvContact.addHeaderView(layout_head);

    }
}
