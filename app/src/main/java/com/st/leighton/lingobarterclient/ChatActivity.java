/*
 * Copyright (c) 2015, 张涛.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.st.leighton.lingobarterclient;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.ibm.watson.developer_cloud.http.HttpMediaType;
import com.ibm.watson.developer_cloud.speech_to_text.v1.RecognizeOptions;
import com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechResults;

import chat.OnOperationListener;
import chat.adapter.MessageAdapter;
import chat.bean.Emojicon;
import chat.bean.Faceicon;
import chat.bean.Message;
import chat.emoji.DisplayRules;
import chat.widget.KJChatKeyboard;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.ui.ViewInject;
import org.kymjs.kjframe.utils.FileUtils;
import org.kymjs.kjframe.utils.KJLoger;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;

/**
 * Chat
 */
public class ChatActivity extends KJActivity {
    private String user1;
    private String user2;

    public static final int REQUEST_CODE_GETIMAGE_BYSDCARD = 0x1;

    private KJChatKeyboard box;
    private ListView mRealListView;

    private Socket mSocket;

    List<Message> messages = new ArrayList<>();
    private MessageAdapter adapter;

    private

    SpeechToText service = new SpeechToText();

    RecognizeOptions options = new RecognizeOptions.Builder().contentType(
            HttpMediaType.AUDIO_RAW + "; rate=44000").build();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user1 = getIntent().getExtras().getString("USER1_ID");
        user2 = getIntent().getExtras().getString("USER2_ID");

//        String authToken = "ChFmVw.4h15p7BS2UGnk2FxDCFP7J3oDv4";
//        IO.Options opts = new IO.Options();
//        opts.forceNew = false;
//        opts.reconnection = false;
//        opts.query = "auth_token=" + authToken;
//        try {
//            mSocket = IO.socket("http://192.168.0.9:5000", opts);
//        } catch (URISyntaxException e){
//            e.printStackTrace();
//        }
//        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
//        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
//        mSocket.on("ret:browse messages", onBrowseMessages);
//        mSocket.connect();

        service.setUsernameAndPassword(getString(R.string.STT_Username), getString(R.string.STT_Password));
        service.setEndPoint(getString(R.string.STT_TokenFactory));

    }

    @Override
    public void setRootView() {
        setContentView(R.layout.activity_chat);
    }

    @Override
    public void initWidget() {
        super.initWidget();
        box = (KJChatKeyboard) findViewById(R.id.chat_msg_input_box);
        mRealListView = (ListView) findViewById(R.id.chat_listview);

        mRealListView.setSelector(android.R.color.transparent);
        initMessageInputToolBox();
        initListView();
    }

    private void initMessageInputToolBox() {
        box.setOnOperationListener(new OnOperationListener() {
            @Override
            public void send(String content) {
                Message message = new Message(Message.MSG_TYPE_TEXT, Message.MSG_STATE_SUCCESS,
                        user1, "avatar", user2,
                        "avatar", content, true, true, new Date());
                messages.add(message);
                adapter.refresh(messages);
                createReplyMsg(message);
            }

            @Override
            public void sendVoiceM(String fileName, final int length) {
                final File audio = new File("fileName");

                new AsyncTask<Void, Void, SpeechResults>(){
                    @Override
                    protected SpeechResults doInBackground(Void... none) {
                        SpeechResults transcript = service.recognize(audio, options).execute();
                        return transcript;
                    }

                    @Override
                    protected void onPostExecute(SpeechResults result) {
                        String content = result.toString();
                        Message message = new Message(Message.MSG_TYPE_VOICE, Message.MSG_STATE_SUCCESS,
                                user1, "avatar", user2, "avatar", content, true, true, new Date());
                        message.setLength(length);
                        messages.add(message);
                    }
                }.execute();

                adapter.refresh(messages);
            }

            @Override
            public void selectedFace(Faceicon content){

            }

            @Override
            public void selectedEmoji(Emojicon emoji) {
                box.getEditTextBox().append(emoji.getValue());
            }

            @Override
            public void selectedBackSpace(Emojicon back) {
                DisplayRules.backspace(box.getEditTextBox());
            }

            @Override
            public void selectedFunction(int index) {
                switch (index) {
                    case 0:
                        goToAlbum();
                        break;
                    case 1:
                        ViewInject.toast("Camera");
                        break;
                }
            }
        });

        List<String> faceCagegory = new ArrayList<>();
//        File faceList = FileUtils.getSaveFolder("chat");
        File faceList = new File("");
        if (faceList.isDirectory()) {
            File[] faceFolderArray = faceList.listFiles();
            for (File folder : faceFolderArray) {
                if (!folder.isHidden()) {
                    faceCagegory.add(folder.getAbsolutePath());
                }
            }
        }

        box.setFaceData(faceCagegory);
        mRealListView.setOnTouchListener(getOnTouchListener());
    }

    private void initListView() {
//        byte[] emoji = new byte[]{
//                (byte) 0xF0, (byte) 0x9F, (byte) 0x98, (byte) 0x81
//        };
//        Message message = new Message(Message.MSG_TYPE_TEXT,
//                Message.MSG_STATE_SUCCESS, user1, "avatar", user2, "avatar",
//                new String(emoji), false, true, new Date(System.currentTimeMillis()
//                - (1000 * 60 * 60 * 24) * 8));
//
//        Message message1 = new Message(Message.MSG_TYPE_TEXT,
//                Message.MSG_STATE_SUCCESS, user2, "avatar", user1, "avatar",
//                "以后的版本支持链接高亮喔:http://www.kymjs.com支持http、https、svn、ftp开头的链接",
//                true, true, new Date());
//
//        Message message6 = new Message(Message.MSG_TYPE_TEXT,
//                Message.MSG_STATE_FAIL, user1, "avatar", user2, "avatar",
//                "test send fail", true, false, new Date(
//                System.currentTimeMillis() - (1000 * 60 * 60 * 24) * 2));
//        Message message7 = new Message(Message.MSG_TYPE_TEXT,
//                Message.MSG_STATE_SENDING, user1, "avatar", user2, "avatar",
//                "<a href=\"http://kymjs.com\">自定义链接</a>也是支持的", true, true, new Date(System.currentTimeMillis()));
//
//        messages.add(message);
//        messages.add(message1);
//        messages.add(message6);
//        messages.add(message7);

        adapter = new MessageAdapter(this, messages, getOnChatItemClickListener());
        mRealListView.setAdapter(adapter);
    }

    private void createReplyMsg(Message message) {
        final Message reMessage = new Message(message.getType(), Message.MSG_STATE_SUCCESS, user2,
                "avatar", user1, "avatar", message.getType() == Message.MSG_TYPE_TEXT ? "Reply:"
                + message.getContent() : message.getContent(), false,
                true, new Date());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000 * (new Random().nextInt(3) + 1));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            messages.add(reMessage);
                            adapter.refresh(messages);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && box.isShow()) {
            box.hideLayout();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    /**
     * 跳转到选择相册界面
     */
    private void goToAlbum() {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "选择图片"),
                    REQUEST_CODE_GETIMAGE_BYSDCARD);
        } else {
            intent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "选择图片"),
                    REQUEST_CODE_GETIMAGE_BYSDCARD);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_GETIMAGE_BYSDCARD) {
            Uri dataUri = data.getData();
            if (dataUri != null) {
                File file = FileUtils.uri2File(aty, dataUri);
                Message message = new Message(Message.MSG_TYPE_PHOTO, Message.MSG_STATE_SUCCESS,
                        user1, "avatar", user2,
                        "avatar", file.getAbsolutePath(), true, true, new Date());
                messages.add(message);
                adapter.refresh(messages);
            }
        }
    }

    /**
     * 若软键盘或表情键盘弹起，点击上端空白处应该隐藏输入法键盘
     *
     * @return 会隐藏输入法键盘的触摸事件监听器
     */
    private View.OnTouchListener getOnTouchListener() {
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                box.hideLayout();
                box.hideKeyboard(aty);
                return false;
            }
        };
    }

    /**
     * @return 聊天列表内存点击事件监听器
     */
    private OnChatItemClickListener getOnChatItemClickListener() {
        return new OnChatItemClickListener() {
            @Override
            public void onPhotoClick(int position) {
                KJLoger.debug(messages.get(position).getContent() + "点击图片的");
                ViewInject.toast(aty, messages.get(position).getContent() + "点击图片的");
            }

            @Override
            public void onTextClick(int position) {
            }

            @Override
            public void onFaceClick(int position) {
            }
        };
    }

    /**
     * 聊天列表中对内容的点击事件监听
     */
    public interface OnChatItemClickListener {
        void onPhotoClick(int position);

        void onTextClick(int position);

        void onFaceClick(int position);
    }

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), R.string.error_connect, Toast.LENGTH_LONG).show();
                }
            });
        }
    };

    private Emitter.Listener onBrowseMessages = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            JSONObject data = (JSONObject) args[0];
            JSONArray messages = null;
            try {
                messages = data.getJSONArray("response");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            System.out.println(messages);
            setMessagesList(messages);
        }
    };

    //display clickable a list of all Mymessages
    private void setMessagesList(JSONArray MyMessages) {
        // looping through all messages
        for (int i = 0; i < MyMessages.length(); i++) {
            try {
                JSONObject msg = MyMessages.getJSONObject(i);
                int type = Message.MSG_TYPE_TEXT;
                if(msg.getString("type") == "voice"){
                    type = Message.MSG_TYPE_VOICE;
                }
                else if(msg.getString("type") == "image"){
                    type = Message.MSG_TYPE_PHOTO;
                }

                Date date = new Date(Long.parseLong(msg.getString("timestamp")));

                Message m = new Message(type, Message.MSG_STATE_SUCCESS,
                                        msg.getString("from_id"), "avatar",
                                        msg.getString("to_chat"), "avatar",
                                        msg.getString("payload"), true, true, date);
                messages.add(m);
            } catch (JSONException e) {
                System.out.println("Get message error");
            }
        }
    }
}