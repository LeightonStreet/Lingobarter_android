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
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.ibm.watson.developer_cloud.http.HttpMediaType;
import com.ibm.watson.developer_cloud.language_translation.v2.model.Language;
import com.ibm.watson.developer_cloud.speech_to_text.v1.RecognizeOptions;
import com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechResults;
import com.lingobarter.socketclient.WebsocketClient;

import chat.OnOperationListener;
import chat.adapter.MessageAdapter;
import chat.bean.Emojicon;
import chat.bean.Faceicon;
import chat.bean.Message;
import chat.emoji.DisplayRules;
import chat.widget.KJChatKeyboard;
import io.socket.emitter.Emitter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.ui.ViewInject;
import org.kymjs.kjframe.utils.FileUtils;
import org.kymjs.kjframe.utils.KJLoger;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;


/**
 * Chat
 */
public class ChatActivity extends KJActivity {
    private String chat_id;
    private HashMap<Integer, Language> langs;

    public static final int REQUEST_CODE_GETIMAGE_BYSDCARD = 0x1;

    private KJChatKeyboard box;
    private ListView mRealListView;
    private boolean STT_flag = false;

    private WebsocketClient mSocket;

    private Message message;
    ArrayList<Message> messages = new ArrayList<>();
    private MessageAdapter adapter;

    private int ind;

    SpeechToText service = new SpeechToText();

    RecognizeOptions options = new RecognizeOptions.Builder().contentType(
            HttpMediaType.AUDIO_WAV).build();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ind = getIntent().getExtras().getInt("IND");
        langs = new HashMap<>();
        langs.put(0, Language.PORTUGUESE);
        langs.put(1, Language.PORTUGUESE);
        langs.put(2, Language.SPANISH);
        langs.put(3, Language.SPANISH);
        langs.put(4, Language.FRENCH);
        langs.put(5, Language.FRENCH);
        langs.put(6, Language.ITALIAN);
        langs.put(7, Language.ITALIAN);
        langs.put(8, Language.ARABIC);
        langs.put(9, Language.ARABIC);

        MyApplication app = (MyApplication) getApplication();
        mSocket = app.getSocket();

        service.setUsernameAndPassword(getString(R.string.STT_Username), getString(R.string.STT_Password));
        service.setEndPoint(getString(R.string.STT_TokenFactory));

        mSocket.on("ret:send message", new Emitter.Listener() {
            public void call(Object... args) {
                JSONObject obj = (JSONObject) args[0];
                System.out.println(obj);
                try{
                    int status = obj.getInt("status");
                    if(status == 200){
//                        messages.add(message);
                        System.out.println("send message successfully");
                    }
                }catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("fail to send message");
                }
            }
        });

        IntentFilter responseFilter = new IntentFilter("android.intent.action.BotChat");
        BroadcastReceiver responseReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String response = intent.getStringExtra("RESPONSE");
                Message m = new Message(Message.MSG_TYPE_TEXT, Message.MSG_STATE_SUCCESS,
                        chat_id, response, 0, true, new Date());
                createReplyMsg(m);
            }
        };
        this.registerReceiver(responseReceiver, responseFilter);
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
            public void send(final String content, String type) {
                JSONObject object = new JSONObject();
                try {
                    object.put("to_chat", chat_id);
                    object.put("type", "text");
                    object.put("payload", content);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println(object);
                mSocket.emit("send message", object);
                message = new Message(Message.MSG_TYPE_TEXT, Message.MSG_STATE_SUCCESS,
                                      chat_id, content, 1, true, new Date());
                messages.add(message);
                adapter.refresh(messages);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (ind % 2 == 1) {
                            Webservice.language_from(content, langs.get(ind));
                        } else {
                            Webservice.language_to(content, langs.get(ind));
                        }
                    }
                }).start();
            }

            @Override
            public void sendVoiceM(String fileName, final int length) {
                if(STT_flag) {
                    final File audio = new File(fileName);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                final SpeechResults transcript = service.recognize(audio, options).execute();
                                Thread.sleep(1000 * (new Random().nextInt(3) + 1));
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        String content = transcript.toString();
                                        JSONObject object = new JSONObject();
                                        try {
                                            object.put("to_chat", chat_id);
                                            object.put("type", "voice");
                                            object.put("payload", content);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        System.out.println(object);
                                        mSocket.emit("send message", object);
                                        message = new Message(Message.MSG_TYPE_TEXT, Message.MSG_STATE_SUCCESS,
                                                chat_id, content, 1, true, new Date());
                                        messages.add(message);
                                        message.setLength(length);
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
                else{
                    try {
                        FileInputStream fileInputStream = new FileInputStream(fileName);
                        JSONObject object = new JSONObject();
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
                        byte[] data = new byte[4096];
                        int count = fileInputStream.read(data);
                        while (count != -1) {
                            dataOutputStream.write(data, 0, count);
                            count = fileInputStream.read(data);
                        }
                        byte[] ret = byteArrayOutputStream.toByteArray();
                        object.put("to_chat", chat_id);
                        object.put("type", "voice");
                        object.put("payload", ret);
                        System.out.println(object);
                        mSocket.emit("send message", object);
                        message = new Message(Message.MSG_TYPE_VOICE, Message.MSG_STATE_SUCCESS,
                                chat_id, fileName, 1, true, new Date());
                        message.setLength(length);
                        messages.add(message);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
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
                    case 2:
                        STT_flag = !STT_flag;
                        if(STT_flag) {
                            ViewInject.toast("Turn on Speech to Text service");
                        }
                        else{
                            ViewInject.toast("Turn off Speech to Text service");
                        }
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
        adapter = new MessageAdapter(this, messages, getOnChatItemClickListener());
        mRealListView.setAdapter(adapter);
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
//                Message message = new Message(Message.MSG_TYPE_PHOTO, Message.MSG_STATE_SUCCESS,
//                        user1, "avatar", user2,
//                        "avatar", file.getAbsolutePath(), true, true, new Date());
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

    //display clickable a list of all MyMessages
    private void setMessagesList(JSONArray MyMessages) {
        // looping through all messages
        for (int i = 0; i < MyMessages.length(); i++) {
            try {
                JSONObject msg = MyMessages.getJSONObject(i);
                Date date = new Date(Long.parseLong(msg.getString("timestamp")));
                int flag = i % 2 + 1;
                Message m = new Message(msg.getString("type"), Message.MSG_STATE_SUCCESS,
                                        msg.getString("to_chat"),
                                        msg.getString("payload"), flag, true, date);
                messages.add(0, m);
            } catch (JSONException e) {
                System.out.println("Get message error");
            }
        }
    }

    private void createReplyMsg(final Message message) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000 * (new Random().nextInt(3) + 1));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            messages.add(message);
                            adapter.refresh(messages);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}