package com.st.leighton.lingobarterclient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.EditText;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.gigamole.library.NavigationTabBar;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * Created by vicky on 06.05.2016.
 */
public class MainActivity extends Activity {

    Context baseContext;

    private ArrayList<MyChat> MyChats = new ArrayList<>();
    private ArrayList<String> partners = new ArrayList<>();
    private ArrayList<String> searches = new ArrayList<>();
    private ArrayList<String> myProfile = new ArrayList<>();

    private Socket mSocket;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horizontal_ntb);

        baseContext = this;

        String authToken = "ChFmVw.4h15p7BS2UGnk2FxDCFP7J3oDv4";
        IO.Options opts = new IO.Options();
        opts.forceNew = false;
        opts.reconnection = false;
        opts.query = "auth_token=" + authToken;
        try {
            mSocket = IO.socket("http://192.168.0.9:8080", opts);
            mSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                public void call(Object... args) {
                    System.out.println("connected!");
                    JSONObject object = new JSONObject();
                    try {
                        object.put("from_id", "572e68ad1d41c8588f50ddb3");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mSocket.emit("browse chats");

                    mSocket.on("ret:browse chats", onBrowseChats);
//        mSocket.on("ret:browse partners", onBrowsePartner);
                    mSocket.on("ret:add partner", onAddPartner);
                }
            });
        } catch (URISyntaxException e){
            e.printStackTrace();
        }
        mSocket.connect();

        partners.add("Qi");
        partners.add("Andy");

        searches.add("find vicky");

        myProfile.add("8)");
        initUI();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mSocket.disconnect();
        mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.off("ret:add partner", onAddPartner);
//        mSocket.off("ret:browse partners", onBrowsePartners);
        mSocket.off("ret:browse chats", onBrowseChats);

        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
    }

    private void initUI() {
        final ViewPager viewPager = (ViewPager) findViewById(R.id.vp_horizontal_ntb);
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return 4;
            }

            @Override
            public boolean isViewFromObject(final View view, final Object object) {
                return view.equals(object);
            }

            @Override
            public void destroyItem(final View container, final int position, final Object object) {
                ((ViewPager) container).removeView((View) object);
            }

            @Override
            public Object instantiateItem(final ViewGroup container, final int position) {
                View view = LayoutInflater.from(
                        getBaseContext()).inflate(R.layout.activity_main, null, false);

                switch (position) {
                    case 0:
                        view = LayoutInflater.from(
                                getBaseContext()).inflate(R.layout.activity_main, null, false);
                        ListView chatList = (ListView)view.findViewById(R.id.my_chats_list);
                        chatList.setAdapter(new ArrayAdapter<>(MainActivity.this, R.layout.chats_list_item, MyChats));
                        chatList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> a, View v, int i, long l) {
                                openChat(MyChats, i);
                                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                                intent.putExtra("USER1_ID", "vicky");
                                intent.putExtra("USER2_ID", "Qi");
                                startActivity(intent);
                            }
                        });
                        break;

                    case 1:
                        view = LayoutInflater.from(
                                getBaseContext()).inflate(R.layout.activity_main, null, false);
                        ListView partnerList = (ListView) view.findViewById(R.id.my_chats_list);
                        partnerList.setAdapter(
                                new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, partners));
                        break;

                    case 2:
//                        view = LayoutInflater.from(
//                                getBaseContext()).inflate(R.layout.activity_main, null, false);
//                        ListView searchList = (ListView) view.findViewById(R.id.MyChatsListView);
//                        searchList.setAdapter(
//                                new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, searches));
                        setSearches(view);
                        break;

                    case 3:
//                        view = LayoutInflater.from(
//                                getBaseContext()).inflate(R.layout.activity_main, null, false);
//                        ListView profileList = (ListView) view.findViewById(R.id.MyChatsListView);
//                        profileList.setAdapter(
//                                new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, myProfile));
                        setSettingList(view);
                        break;
                    default:
                        break;
                }
                container.addView(view);
                return view;
            }
        });

        final String[] colors = getResources().getStringArray(R.array.default_preview);

        final NavigationTabBar navigationTabBar = (NavigationTabBar) findViewById(R.id.ntb_horizontal);
        final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
        models.add(new NavigationTabBar.Model(
                getResources().getDrawable(R.drawable.ic_message_black_36dp), Color.parseColor(colors[0]), "Chats"));
        models.add(new NavigationTabBar.Model(
                getResources().getDrawable(R.drawable.ic_people_outline_black_36dp), Color.parseColor(colors[1]), "Partners"));
        models.add(new NavigationTabBar.Model(
                getResources().getDrawable(R.drawable.ic_search_black_36dp), Color.parseColor(colors[3]), "Search"));
        models.add(new NavigationTabBar.Model(
                getResources().getDrawable(R.drawable.ic_face_black_36dp), Color.parseColor(colors[4]), "Me"));

        navigationTabBar.setModels(models);
        navigationTabBar.setViewPager(viewPager, 0);

        navigationTabBar.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
                navigationTabBar.getModels().get(position).hideBadge();
            }

            @Override
            public void onPageScrollStateChanged(final int state) {

            }
        });

        navigationTabBar.post(new Runnable() {
            @Override
            public void run() {
                final View bgNavigationTabBar = findViewById(R.id.bg_ntb_horizontal);
                bgNavigationTabBar.getLayoutParams().height = (int) navigationTabBar.getBarHeight();
                bgNavigationTabBar.requestLayout();
            }
        });

        navigationTabBar.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < navigationTabBar.getModels().size(); i++) {
                    final NavigationTabBar.Model model = navigationTabBar.getModels().get(i);
                    switch (i) {
                        case 0:
                            model.setBadgeTitle("1");
                            break;
                        case 1:
                            model.setBadgeTitle("2");
                            break;
                        default:
                            model.setBadgeTitle("0");
                            break;
                    }
                    navigationTabBar.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(model.getBadgeTitle() != "0")
                                model.showBadge();
                        }
                    }, i * 100);
                }
            }
        }, 500);
    }

    private void setSearches(View view) {
        ListView list = (ListView) view.findViewById(R.id.my_chats_list);
        ViewGroup root = ((ViewGroup)list.getParent());
        root.removeView(list);

        EditText searchET = new EditText(baseContext);
        searchET.setText(getResources().getString(R.string.hx_search_entrance_description));
        searchET.setTextColor(ContextCompat.getColor(baseContext, R.color.DarkSalmon));
        searchET.setGravity(Gravity.TOP);
        searchET.setWidth(10);
        searchET.setHeight(2);
        searchET.setFocusable(false);
        searchET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(baseContext, Search.class);
                startActivity(intent);
            }
        });

        root.addView(searchET);
    }

    private void setSettingList(View view) {
        final ListView settingList = (ListView) view.findViewById(R.id.my_chats_list);

        String[] settings = getResources().getStringArray(R.array.settings_array);
        ArrayAdapter<String> settingAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, settings);
        settingList.setAdapter(settingAdapter);

        settingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                switch (position) {
                    case 1:
                        intent = new Intent(baseContext, ProfileSettings.class);
                        startActivity(intent);
                        break;

                    case 2:
                        intent = new Intent(baseContext, SelfInformation.class);
                        startActivity(intent);
                        break;

                    case 3:
                        intent = new Intent(baseContext, ApplicationSettings.class);
                        startActivity(intent);
                        break;
                }
            }
        });
    }

    // open a specific chat
    public void openChat(ArrayList<MyChat> MyChats, int pos) {
        MyChat chat = MyChats.get(pos);
        JSONObject request = new JSONObject();
        try {
            request.put("page_size", "20");
            request.put("page_id", "0");
            request.put("to_chat", chat.id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mSocket.emit("browse messages", request);
    }

    // Listeners
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

    private Emitter.Listener onBrowseChats = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            JSONObject data = (JSONObject) args[0];
            JSONArray chats = null;
            try {
                chats = data.getJSONArray("response");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            System.out.println(chats);
            setChatsList(chats);
        }
    };

    //display clickable a list of all MyChats
    private void setChatsList(JSONArray chats) {
        // looping through all chats
        for (int i = 0; i < chats.length(); i++) {
            try {
                JSONObject chat = chats.getJSONObject(i);
                MyChat ct = new MyChat(chat.getString("id"),
                                       chat.getString("name"),
                                       chat.getJSONArray("members"),
                                       chat.getString("last_updated"));
                MyChats.add(ct);
            } catch (JSONException e) {
                System.out.println("Get chat error");
            }
        }
    }

    private Emitter.Listener onAddPartner = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    try {
                        username = data.getString("username");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                    addPartner(username);
                }
            });
        }
    };

    private Emitter.Listener onAddChat = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    try {
                        username = data.getString("username");
                    } catch (JSONException e) {
                        return;
                    }
//                    addChat(username);
                }
            });
        }
    };

    public class MyChat{
        private String id;
        private String name;
        private JSONArray members;
        private String last_updated;

        public MyChat(String id, String name, JSONArray members, String last_updated){
            this.id = id;
            this.name = name;
            this.members = members;
            this.last_updated = last_updated;
        }
    }
}
