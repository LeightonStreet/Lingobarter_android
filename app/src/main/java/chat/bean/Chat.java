package chat.bean;

import org.json.JSONArray;

/**
 * Created by vicky on 5/11/16.
 */
public class Chat{
    private String id;
    private String name;
    private JSONArray members;
    private String last_updated;

    public Chat(String id, String name, JSONArray members, String last_updated){
        this.id = id;
        this.name = name;
        this.members = members;
        this.last_updated = last_updated;
    }

    public String getId(){
        return id;
    }

    public String getName() {return name;}

    public String getTime() {return last_updated;}

    public JSONArray getMembers(){
        return members;
    }
}
