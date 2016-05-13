package chat.common;

import java.util.Comparator;

import chat.bean.Contact;

public class PinyinComparator implements Comparator {

	@Override
	public int compare(Object arg0, Object arg1) {
		// 按照名字排序
		Contact user0 = (Contact) arg0;
		Contact user1 = (Contact) arg1;
		String catalog0 = "";
		String catalog1 = "";

		if (user0 != null && user0.getUserName() != null
				&& user0.getUserName().length() > 1)
			catalog0 = user0.getUserName();

		if (user1 != null && user1.getUserName() != null
				&& user1.getUserName().length() > 1)
			catalog1 = user1.getUserName();
		int flag = catalog0.compareTo(catalog1);
		return flag;

	}

}
