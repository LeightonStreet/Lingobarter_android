package com.st.leighton.lingobarterclient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.gigamole.library.NavigationTabBar;
import com.lingobarter.socketclient.WebsocketClient;
import com.st.leighton.util.MyProperty;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;

import chat.adapter.ChatAdapter;
import chat.adapter.ContactAdapter;
import chat.bean.Chat;
import chat.bean.Contact;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by vicky on 06.05.2016.
 * @author Qi Wang
 */
public class MainActivity extends Activity {

    Context baseContext;

    private WebsocketClient mSocket = null;

    private ArrayList<Chat> MyChats = new ArrayList<>();
    private ArrayList<Contact> MyPartners = new ArrayList<>();

    private ContactAdapter contactAdapter = new ContactAdapter(MainActivity.this, MyPartners);
    private ChatAdapter chatAdapter = new ChatAdapter(MainActivity.this, MyChats);

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horizontal_ntb);

        MyApplication app = (MyApplication) getApplication();

        String str1 = "[{\"username\":\"POR to EN\",\"bio\":\"I do teach and learn!!! Haha\",\"teach_langs\":[{\"language_id\":\"Chinese Simplified\",\"level\":0},{\"language_id\":\"English\",\"level\":0}],\"birthday\":1340942400,\"nationality\":\"UK\",\"id\":\"57357a9b67b60b1083b09632\",\"current_login_at\":1463137088,\"name\":\"user2\",\"gender\":\"female\",\"avatar_url\":\"\\/mediafiles\\/default-avatar.png\",\"location\":{\"type\":\"Point\",\"coordinates\":[111.6,30.5]},\"tagline\":\"You know Portuguese??\",\"learn_langs\":[{\"language_id\":\"Cantonese\",\"level\":5}]}]";
        String str2 = "[{\"username\":\"EN to POR\",\"bio\":\"I do teach and learn!!! Haha\",\"teach_langs\":[{\"language_id\":\"Chinese Simplified\",\"level\":0},{\"language_id\":\"English\",\"level\":0}],\"birthday\":1340942400,\"nationality\":\"UK\",\"id\":\"57357a9b67b60b1083b09632\",\"current_login_at\":1463137088,\"name\":\"user2\",\"gender\":\"female\",\"avatar_url\":\"\\/mediafiles\\/default-avatar.png\",\"location\":{\"type\":\"Point\",\"coordinates\":[111.6,30.5]},\"tagline\":\"You know Portuguese??\",\"learn_langs\":[{\"language_id\":\"Cantonese\",\"level\":5}]}]";
        String str3 = "[{\"username\":\"ES to EN\",\"bio\":\"I do teach and learn!!! Haha\",\"teach_langs\":[{\"language_id\":\"Chinese Simplified\",\"level\":0},{\"language_id\":\"English\",\"level\":0}],\"birthday\":1340942400,\"nationality\":\"UK\",\"id\":\"57357a9b67b60b1083b09632\",\"current_login_at\":1463137088,\"name\":\"user2\",\"gender\":\"female\",\"avatar_url\":\"\\/mediafiles\\/default-avatar.png\",\"location\":{\"type\":\"Point\",\"coordinates\":[111.6,30.5]},\"tagline\":\"I'm soooo happy\",\"learn_langs\":[{\"language_id\":\"Cantonese\",\"level\":5}]}]";
        String str4 = "[{\"username\":\"EN to ES\",\"bio\":\"I do teach and learn!!! Haha\",\"teach_langs\":[{\"language_id\":\"Chinese Simplified\",\"level\":0},{\"language_id\":\"English\",\"level\":0}],\"birthday\":1340942400,\"nationality\":\"UK\",\"id\":\"57357a9b67b60b1083b09632\",\"current_login_at\":1463137088,\"name\":\"user2\",\"gender\":\"female\",\"avatar_url\":\"\\/mediafiles\\/default-avatar.png\",\"location\":{\"type\":\"Point\",\"coordinates\":[111.6,30.5]},\"tagline\":\"I'm soooo happy\",\"learn_langs\":[{\"language_id\":\"Cantonese\",\"level\":5}]}]";
        String str5 = "[{\"username\":\"FR to EN\",\"bio\":\"I do teach and learn!!! Haha\",\"teach_langs\":[{\"language_id\":\"Chinese Simplified\",\"level\":0},{\"language_id\":\"English\",\"level\":0}],\"birthday\":1340942400,\"nationality\":\"UK\",\"id\":\"57357a9b67b60b1083b09632\",\"current_login_at\":1463137088,\"name\":\"user2\",\"gender\":\"female\",\"avatar_url\":\"\\/mediafiles\\/default-avatar.png\",\"location\":{\"type\":\"Point\",\"coordinates\":[111.6,30.5]},\"tagline\":\"Bonjour my friend!\",\"learn_langs\":[{\"language_id\":\"Cantonese\",\"level\":5}]}]";
        String str6 = "[{\"username\":\"EN to FR\",\"bio\":\"I do teach and learn!!! Haha\",\"teach_langs\":[{\"language_id\":\"Chinese Simplified\",\"level\":0},{\"language_id\":\"English\",\"level\":0}],\"birthday\":1340942400,\"nationality\":\"UK\",\"id\":\"57357a9b67b60b1083b09632\",\"current_login_at\":1463137088,\"name\":\"user2\",\"gender\":\"female\",\"avatar_url\":\"\\/mediafiles\\/default-avatar.png\",\"location\":{\"type\":\"Point\",\"coordinates\":[111.6,30.5]},\"tagline\":\"Bonjour my friend!\",\"learn_langs\":[{\"language_id\":\"Cantonese\",\"level\":5}]}]";
        String str7 = "[{\"username\":\"IT to EN\",\"bio\":\"I do teach and learn!!! Haha\",\"teach_langs\":[{\"language_id\":\"Chinese Simplified\",\"level\":0},{\"language_id\":\"English\",\"level\":0}],\"birthday\":1340942400,\"nationality\":\"UK\",\"id\":\"57357a9b67b60b1083b09632\",\"current_login_at\":1463137088,\"name\":\"user2\",\"gender\":\"female\",\"avatar_url\":\"\\/mediafiles\\/default-avatar.png\",\"location\":{\"type\":\"Point\",\"coordinates\":[111.6,30.5]},\"tagline\":\"Ciao!\",\"learn_langs\":[{\"language_id\":\"Cantonese\",\"level\":5}]}]";
        String str8 = "[{\"username\":\"EN to IT\",\"bio\":\"I do teach and learn!!! Haha\",\"teach_langs\":[{\"language_id\":\"Chinese Simplified\",\"level\":0},{\"language_id\":\"English\",\"level\":0}],\"birthday\":1340942400,\"nationality\":\"UK\",\"id\":\"57357a9b67b60b1083b09632\",\"current_login_at\":1463137088,\"name\":\"user2\",\"gender\":\"female\",\"avatar_url\":\"\\/mediafiles\\/default-avatar.png\",\"location\":{\"type\":\"Point\",\"coordinates\":[111.6,30.5]},\"tagline\":\"Ciao!\",\"learn_langs\":[{\"language_id\":\"Cantonese\",\"level\":5}]}]";
        String str9 = "[{\"username\":\"AR to EN\",\"bio\":\"I do teach and learn!!! Haha\",\"teach_langs\":[{\"language_id\":\"Chinese Simplified\",\"level\":0},{\"language_id\":\"English\",\"level\":0}],\"birthday\":1340942400,\"nationality\":\"UK\",\"id\":\"57357a9b67b60b1083b09632\",\"current_login_at\":1463137088,\"name\":\"user2\",\"gender\":\"female\",\"avatar_url\":\"\\/mediafiles\\/default-avatar.png\",\"location\":{\"type\":\"Point\",\"coordinates\":[111.6,30.5]},\"tagline\":\"Learn English using Arabic!\",\"learn_langs\":[{\"language_id\":\"Cantonese\",\"level\":5}]}]";
        String str10 = "[{\"username\":\"EN to AR\",\"bio\":\"I do teach and learn!!! Haha\",\"teach_langs\":[{\"language_id\":\"Chinese Simplified\",\"level\":0},{\"language_id\":\"English\",\"level\":0}],\"birthday\":1340942400,\"nationality\":\"UK\",\"id\":\"57357a9b67b60b1083b09632\",\"current_login_at\":1463137088,\"name\":\"user2\",\"gender\":\"female\",\"avatar_url\":\"\\/mediafiles\\/default-avatar.png\",\"location\":{\"type\":\"Point\",\"coordinates\":[111.6,30.5]},\"tagline\":\"Learn English using Arabic!\",\"learn_langs\":[{\"language_id\":\"Cantonese\",\"level\":5}]}]";

        ArrayList<String> str = new ArrayList<String>();
        str.add(str1); str.add(str2); str.add(str3); str.add(str4); str.add(str5);
        str.add(str6); str.add(str7); str.add(str8); str.add(str9); str.add(str10);

        JSONArray test = null;
        JSONObject partner = null;
        for(int i = 0; i < str.size(); i++) {
            try {
                test = new JSONArray(str.get(i));
                partner = (JSONObject) test.get(0);
                Contact ct = new Contact(partner.getDouble("birthday") / 1000,
                        partner.getString("username"),
                        partner.getString("name"),
                        partner.getString("avatar_url"),
                        partner.getString("tagline"),
                        partner.getString("gender"),
                        partner.getString("bio"),
                        partner.getJSONObject("location"),
                        partner.getString("nationality"),
                        partner.getJSONArray("learn_langs"),
                        partner.getJSONArray("teach_langs"));
                ct.setAvatarUrl("head1");
                MyPartners.add(ct);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        try {
            String authToken = GlobalStore.getInstance().getToken();
            String url = MyProperty.getProperty("protocol") + "://" + MyProperty.getProperty("host") + ":" + MyProperty.getProperty("port");
            app.setSocket(new WebsocketClient(url, authToken, true));
            System.out.println("set socket");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        mSocket = app.getSocket();
        mSocket.connect();
        baseContext = this;
        mSocket.getSocket().on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            public void call(Object... args) {
                System.out.println("connected!");
                mSocket.emit("browse chats");
                mSocket.emit("browse partners");
            }
        }).on("ret:browse chats", onBrowseChats)
        .on("ret:browse partners", onBrowsePartners)
        .on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            public void call(Object... args) {
                System.out.println("You disconnect me");
            }
        }).on("ret:add partner", new Emitter.Listener() {
            public void call(Object... args) {
                JSONObject obj = (JSONObject) args[0];
                System.out.println(obj);
                try{
                    JSONObject response = obj.getJSONObject("response");
                    int status = response.getInt("status");
                    if(status == 200){
                        JSONObject user = response.getJSONObject("to_user");
                        double birthday = user.getDouble("birthday");
                        Contact new_partner = new Contact(birthday/1000.0,
                                user.getString("username"), user.getString("name"),
                                user.getString("avatar_url"), user.getString("tagline"),
                                user.getString("gender"), user.getString("bio"),
                                user.getJSONObject("location"), user.getString("nationality"),
                                user.getJSONArray("learn_langs"), user.getJSONArray("teach_langs"));
                        MyPartners.add(new_partner);
                        System.out.println("add partner successfully");
                    }
                }catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("fail to send message");
                }
            }
        });
        mSocket.connect();
        initUI();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mSocket.getSocket().disconnect();

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
                        final ListView chatList = (ListView) view.findViewById(R.id.my_chats_list);
                        chatList.setAdapter(chatAdapter);
                        break;

                    case 1:
                        view = LayoutInflater.from(
                                getBaseContext()).inflate(R.layout.activity_contact, null, false);
                        final ListView contactsList = (ListView) view.findViewById(R.id.my_contacts_list);
                        contactsList.setAdapter(contactAdapter);
                        LinearLayout add_friend = (LinearLayout) view.findViewById(R.id.layout_add_friend);
                        add_friend.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(baseContext, Search.class);
                                startActivity(intent);
                            }
                        });
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
        navigationTabBar.setViewPager(viewPager, 2);

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
//                        case 0:
//                            model.setBadgeTitle("1");
//                            break;
//                        case 1:
//                            model.setBadgeTitle("2");
//                            break;
                        default:
                            model.setBadgeTitle("0");
                            break;
                    }
                    navigationTabBar.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (model.getBadgeTitle() != "0")
                                model.showBadge();
                        }
                    }, i * 100);
                }
            }
        }, 500);
    }

    private void setSearches(View view) {
        ListView list = (ListView) view.findViewById(R.id.my_chats_list);
        ViewGroup root = ((ViewGroup) list.getParent());
        root.removeView(list);

        Button searchB = new Button(baseContext);
        searchB.setText(getResources().getString(R.string.hx_search_entrance_description));
        searchB.setBackgroundColor(ContextCompat.getColor(baseContext, R.color.DarkSalmon));
        searchB.setGravity(Gravity.CENTER);
        searchB.setWidth(10);
        searchB.setHeight(2);
        searchB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(baseContext, Search.class);
                startActivity(intent);
            }
        });
        searchB.setPaintFlags(searchB.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        root.addView(searchB);
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
    public void openChat(ArrayList<Chat> MyChats, int pos) {
        Chat chat = MyChats.get(pos);
        JSONObject request = new JSONObject();
        try {
            request.put("page_size", "20");
            request.put("page_id", "0");
            request.put("to_chat", chat.getId());
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
            chatAdapter = new ChatAdapter(MainActivity.this, MyChats);
        }
    };

    //display clickable a list of all MyChats
    private void setChatsList(JSONArray chats) {
        // looping through all chats
        for (int i = 0; i < chats.length(); i++) {
            try {
                JSONObject chat = chats.getJSONObject(i);
                Chat ct = new Chat(chat.getString("id"),
                        chat.getString("name"),
                        chat.getJSONArray("members"),
                        chat.getString("last_updated"));
                MyChats.add(ct);
            } catch (JSONException e) {
                System.out.println("Get chat error");
            }
        }
    }

    private Emitter.Listener onBrowsePartners = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            JSONObject data = (JSONObject) args[0];
            JSONArray partners = null;
            try {
                partners = data.getJSONArray("response");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            System.out.println(partners);
            setPartnersList(partners);
            contactAdapter = new ContactAdapter(MainActivity.this, MyPartners);
        }
    };

    //display clickable a list of all MyPartners
    private void setPartnersList(JSONArray partners) {
        // looping through all partners
        for (int i = 0; i < partners.length(); i++) {
            try {
                JSONObject partner = partners.getJSONObject(i);
                Contact pt = new Contact(
                        partner.getDouble("birthday")/1000,
                        partner.getString("username"),
                        partner.getString("name"),
                        partner.getString("avatar_url"),
                        partner.getString("tagline"),
                        partner.getString("gender"),
                        partner.getString("bio"),
                        partner.getJSONObject("location"),
                        partner.getString("nationality"),
                        partner.getJSONArray("learn_langs"),
                        partner.getJSONArray("teach_langs"));
//                MyPartners.add(pt);
            } catch (JSONException e) {
                System.out.println("Get partner error");
            }
        }
    }

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
}