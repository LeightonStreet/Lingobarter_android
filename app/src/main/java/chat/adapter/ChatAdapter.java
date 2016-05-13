package chat.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.st.leighton.lingobarterclient.ChatActivity;
import com.st.leighton.lingobarterclient.R;
import com.st.leighton.lingobarterclient.UserProfile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
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
        holder.img_avatar = (ImageView) v.findViewById(R.id.contact_item_avatar_iv);
        holder.content_holder = (LinearLayout) v.findViewById(R.id.content_container);
        holder.tv_chat_content = (TextView) v.findViewById(R.id.txt_content);
        holder.tv_time = (TextView) v.findViewById(R.id.txt_time);

        Date date = new Date();
        holder.tv_time.setText(date.toString());
        holder.tv_name.setText("Chat with ADÈLE from French");

        URL url = null;
        try {
            JSONArray members = chat.getMembers();
            JSONObject friend = members.getJSONObject(0);
            String str = friend.getString("avatar_url");
            url = new URL("http://192.168.0.4/8080" + str);
            System.out.print(url);
//            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//            holder.img_avatar.setImageBitmap(bmp);
            holder.img_avatar.setImageResource(R.drawable.default_head);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (java.io.IOException e){
            e.printStackTrace();
        } catch (org.json.JSONException e){
            e.printStackTrace();
        }
        v.setTag(holder);

        holder.content_holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("IND", 5);
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
        ImageView img_avatar;
        TextView tv_chat_content;
        LinearLayout content_holder;
    }
}
