package chat.bean;

import org.json.JSONArray;
import org.json.JSONObject;

public class User {
	private String birthday;
	private String userName;
	private String name;
	private String avatar_url;
	private String tagline;
	private String gender;
	private String bio;
	private JSONObject location;
	private String nationality;
	private JSONArray learn_langs;
	private JSONArray teach_langs;

	public User(String birthday, String userName, String name,
				String avatar_url, String tagline, String gender,
				String bio, JSONObject location, String nationality,
				JSONArray learn_langs, JSONArray teach_langs){
		this.birthday = birthday;
		this.userName = userName;
		this.name = name;
		this.avatar_url = avatar_url;
		this.tagline = tagline;
		this.gender = gender;
		this.bio = bio;
		this.location = location;
		this.nationality = nationality;
		this.learn_langs = learn_langs;
		this.teach_langs = teach_langs;
	}

	public String getBirthday() {
		return birthday;
	}

	public String getUserName() {
		return userName;
	}

	public String getName() {
		return name;
	}

	public String getAvatarUrl() {
		return avatar_url;
	}

	public String getTagline() {
		return tagline;
	}

	public String getGender() {
		return gender;
	}

	public String getBio() {return bio;}

	public JSONObject getLocation() {
		return location;
	}

	public String getNationality() { return nationality; }

	public JSONArray getLearnLangs() {return learn_langs;}

	public JSONArray getTeachLangs() {return teach_langs;}
}
