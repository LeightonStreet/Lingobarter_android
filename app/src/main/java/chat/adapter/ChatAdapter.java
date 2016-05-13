package chat.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.st.leighton.lingobarterclient.ChatActivity;
import com.st.leighton.lingobarterclient.R;
import com.st.leighton.lingobarterclient.UserProfile;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import chat.bean.Chat;

/**
 * Created by vicky on 5/11/16.
 */
public class ChatAdapter extends BaseAdapter{
    private final Context context;
    private List<Chat> chats = null;

    public ChatAdapter(Context context, List<Chat> chats) {
        this.context = context;
        if (chats == null) {
            chats = new ArrayList<>(0);
        }
        this.chats = chats;
    }

    public void refresh(List<Chat> chats) {
        this.chats = chats;
        notifyDataSetChanged();
    }

    public int getCount() {
        return chats.size();
    }

    public Object getItem(int position) {
        return chats.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View v, ViewGroup parent) {
        final ViewHolder holder;
        final Chat chat = chats.get(position);
        holder = new ViewHolder();
        v = View.inflate(context, R.layout.chats_list_contact, null);
        holder.tv_name = (TextView) v.findViewById(R.id.txt_name);
        holder.tv_unread_msg_num = (TextView) v.findViewById(R.id.unread_msg_number);
        holder.img_avatar = (ImageView) v.findViewById(R.id.contact_item_avatar_iv);
        holder.layout_content = (RelativeLayout) v.findViewById(R.id.contact_item_layout);
        holder.tv_chat_content = (TextView) v.findViewById(R.id.txt_content);
        holder.tv_time = (TextView) v.findViewById(R.id.txt_time);

        holder.tv_time.setText(chat.getTime());
        holder.tv_name.setText(chat.getName());
        holder.img_avatar.setImageResource(R.drawable.default_head);
        v.setTag(holder);

        holder.layout_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                String id = chat.getId();
                intent.putExtra("CHAT_ID", id);
                context.startActivity(intent);
            }
        });
        holder.img_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UserProfile.class);
                String username = null;
                try {
                    JSONArray members = chat.getMembers();
                    username = members.getJSONObject(1).getString("username");
                } catch (JSONException e){
                    System.out.println("Get friend name error");
                }
                intent.putExtra("username", username);
                context.startActivity(intent);
            }
        });
        return v;
    }

    static class ViewHolder {
        TextView tv_time;
        TextView tv_name;
        TextView tv_unread_msg_num;
        ImageView img_avatar;
        TextView tv_chat_content;
        RelativeLayout layout_content;
    }
}
