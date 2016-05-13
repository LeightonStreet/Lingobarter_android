package chat.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.st.leighton.lingobarterclient.ChatActivity;
import com.st.leighton.lingobarterclient.R;
import com.st.leighton.lingobarterclient.UserProfile;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import chat.bean.Contact;
import chat.common.ViewHolder;

public class ContactAdapter extends BaseAdapter implements SectionIndexer {
	private final Context mContext;
	private List<Contact> contacts;// 好友信息

	public ContactAdapter(Context mContext, List<Contact> contacts) {
		this.mContext = mContext;
		if (contacts == null) {
			contacts = new ArrayList<>(0);
		}
		this.contacts = contacts;
	}

	@Override
	public int getCount() {
		return contacts.size();
	}

	@Override
	public Object getItem(int position) {
		return contacts.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getPositionForSection(int section) {
		for (int i = 0; i < contacts.size(); i++) {
			Contact user = contacts.get(i);
			String l = user.getUserName();
			char firstChar = l.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}
		return 0;
	}

	@Override
	public int getSectionForPosition(int position) {
		return 0;
	}

	@Override
	public Object[] getSections() {
		return null;
	}

	public void refresh(List<Contact> contacts) {
		this.contacts = contacts;
		notifyDataSetChanged();
	}

	public View getView(final int position, View v, ViewGroup parent) {
		final ViewHolder holder;
		final Contact user = contacts.get(position);
		holder = new ViewHolder();
		v = View.inflate(mContext, R.layout.chats_list_contact, null);
		holder.img_avatar = (ImageView) v.findViewById(R.id.contact_item_avatar_iv);
		holder.user_name = (TextView) v.findViewById(R.id.txt_name);
//		holder.tagline = (TextView) v.findViewById(R.id.txt_content);
		holder.content_holder = (LinearLayout) v.findViewById(R.id.content_container);

		holder.img_avatar.setImageResource(R.drawable.head1);
		holder.user_name.setText(user.getUserName());
//		holder.tagline.setText(user.getTagline());
		v.setTag(holder);

		holder.img_avatar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, UserProfile.class);
				String username = user.getUserName();
				intent.putExtra("USER_NAME", username);
				intent.putExtra("IMAGE_URL", "https://raw.githubusercontent.com/motianhuo/wechat/master/WeChat/res/drawable-xhdpi/head.png");
				mContext.startActivity(intent);
			}
		});
		holder.content_holder.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, ChatActivity.class);
				intent.putExtra("IND", position);
				mContext.startActivity(intent);
			}
		});
		return v;
	}

	static class ViewHolder {
		ImageView img_avatar;
//		TextView tagline;
		TextView user_name;
		LinearLayout content_holder;
	}
}
