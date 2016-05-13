package chat.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.st.leighton.lingobarterclient.R;
import com.st.leighton.lingobarterclient.UserProfile;

import java.util.ArrayList;
import java.util.List;

import chat.bean.Contact;

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

	public void refresh(ArrayList<Contact> contacts) {
		this.contacts = contacts;
		notifyDataSetChanged();
	}

	public View getView(final int position, View v, ViewGroup parent) {
		final Contact user = contacts.get(position);
		final ViewHolder holder;
		holder = new ViewHolder();
		v = View.inflate(mContext, R.layout.contact_item, null);
		holder.img_avatar = (ImageView) v.findViewById(R.id.contact_item_avatar_iv);
		holder.user_name = (TextView) v.findViewById(R.id.contactitem_nick);
		v.setTag(holder);
		holder.img_avatar.setImageResource(R.drawable.default_head);
		holder.user_name.setText(user.getUserName());
		holder.img_avatar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, UserProfile.class);
				String username = user.getUserName();
				intent.putExtra("USER_NAME", username);
				mContext.startActivity(intent);
			}
		});
		return v;
	}

	static class ViewHolder {
		ImageView img_avatar;
		TextView user_name;
//		LinearLayout layout_content;
	}
}
