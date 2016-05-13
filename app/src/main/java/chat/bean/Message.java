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
package chat.bean;

import java.util.Date;

/**
 * 聊天消息javabean
 *
 * @author kymjs (http://www.kymjs.com/)
 */
public class Message {
    public final static String MSG_TYPE_PHOTO = "image";
    public final static String MSG_TYPE_FACE = "face";
    public final static String MSG_TYPE_TEXT = "text";
    public final static String MSG_TYPE_VOICE = "voice";

    public final static int MSG_STATE_SUCCESS = 1;
    public final static int MSG_STATE_FAIL = 2;
    public final static int MSG_STATE_SENDING = 3;

    private String chat_id;
    private String type;
    private int state; // 1-success | 2-fail | 3-sending
//    private String fromUserName;
//    private String fromUserAvatar;
//    private String toUserName;
//    private String toUserAvatar;
    private String content;

    private Boolean isSend;
    private Boolean sendSuccess;
    private Date time;
    private int length; // audio message >= 0

    public Message(String type, int state, String chat_id,
                   String content, Boolean isSend, Boolean sendSuccess, Date time) {
        super();
        this.type = type;
        this.state = state;
//        this.fromUserName = fromUserName;
//        this.fromUserAvatar = fromUserAvatar;
//        this.toUserName = toUserName;
//        this.toUserAvatar = toUserAvatar;
        this.content = content;
        this.isSend = isSend;
        this.sendSuccess = sendSuccess;
        this.time = time;
        this.chat_id = chat_id;
    }

    public String getId() {
        return chat_id;
    }

    public void setId(String id) {
        this.chat_id = chat_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

//    public String getFromUserName() {
//        return fromUserName;
//    }
//
//    public void setFromUserName(String fromUserName) {
//        this.fromUserName = fromUserName;
//    }
//
//    public String getFromUserAvatar() {
//        return fromUserAvatar;
//    }
//
//    public void setFromUserAvatar(String fromUserAvatar) {
//        this.fromUserAvatar = fromUserAvatar;
//    }
//
//    public String getToUserName() {
//        return toUserName;
//    }
//
//    public void setToUserName(String toUserName) {
//        this.toUserName = toUserName;
//    }
//
//    public String getToUserAvatar() {
//        return toUserAvatar;
//    }
//
//    public void setToUserAvatar(String toUserAvatar) {
//        this.toUserAvatar = toUserAvatar;
//    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getIsSend() {
        return isSend;
    }

    public void setIsSend(Boolean isSend) {
        this.isSend = isSend;
    }

    public Boolean getSendSucces() {
        return sendSuccess;
    }

    public void setSendSucces(Boolean sendSucces) {
        this.sendSuccess = sendSucces;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public void setLength(int length) {this.length = length;}

    public int getLength() {return length;}
}
