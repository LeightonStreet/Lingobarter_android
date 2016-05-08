package com.st.leighton.lingobarterclient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.gigamole.library.NavigationTabBar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vicky on 06.05.2016.
 */
public class MainActivity extends Activity {
    private ArrayList<String> talks = new ArrayList<>();
    private ArrayList<String> partners = new ArrayList<>();
    private ArrayList<String> searches = new ArrayList<>();
    private ArrayList<String> myProfile = new ArrayList<>();

    Context baseContext;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horizontal_ntb);

        baseContext = this;
        initUI();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
    }

    private void initUI() {
        partners.add("Qi");
        partners.add("Andy");

        searches.add("find vicky");

        myProfile.add("8)");

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
                        setConversationsList(view);
                        break;

                    case 1:
                        view = LayoutInflater.from(
                                getBaseContext()).inflate(R.layout.activity_main, null, false);
                        ListView partnerList = (ListView) view.findViewById(R.id.talksListView);
                        partnerList.setAdapter(
                                new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, partners));
                        break;

                    case 2:
                        setSearches(view);
//                        view = LayoutInflater.from(
//                                getBaseContext()).inflate(R.layout.activity_main, null, false);
//                        ListView searchList = (ListView) view.findViewById(R.id.talksListView);
//                        searchList.setAdapter(
//                                new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, searches));
                        break;

                    case 3:
                        setSettingList(view);
//                        view = LayoutInflater.from(
//                                getBaseContext()).inflate(R.layout.activity_main, null, false);
//                        ListView profileList = (ListView) view.findViewById(R.id.talksListView);
//                        profileList.setAdapter(
//                                new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, myProfile));
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
                            model.setBadgeTitle("-1");
                            break;
                    }
                    navigationTabBar.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(model.getBadgeTitle() != "-1")
                                model.showBadge();
                        }
                    }, i * 100);
                }
            }
        }, 500);
    }

    private void setSearches(View view) {
        ListView list = (ListView) view.findViewById(R.id.talksListView);
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
        final ListView settingList = (ListView) view.findViewById(R.id.talksListView);

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

    //display clickable a list of all talks
    private void setConversationsList(View view) {
        final ListView talkList = (ListView)view.findViewById(R.id.talksListView);
//        currentUserId = ParseUser.getCurrentUser().getObjectId();

//        ParseQuery<ParseUser> query = ParseUser.getQuery();
//        query.whereNotEqualTo("objectId", currentUserId);
//        query.findInBackground(new FindCallback<ParseUser>() {
//            public void done(com.parse.ParseException e) {
//                if (e == null) {
//                    for (int i=0; i<userList.size(); i++) {
//                        talks.add(userList.get(i).getUsername().toString());
//                    }
        talks.add("vicky");
        talks.add("hello world");

        talkList.setAdapter(
                new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, talks));

        talkList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int i, long l) {
                openConversation(talks, i);
            }
        });

//                } else {
//                    Toast.makeText(getApplicationContext(),
//                            "Error loading user list",
//                            Toast.LENGTH_LONG).show();
//                }
//            }
//        });
    }

    //open a conversation with one person
    public void openConversation(ArrayList<String> talks, int pos) {
//        ParseQuery<ParseUser> query = ParseUser.getQuery();
//        query.whereEqualTo("username", talks.get(pos));
//        query.findInBackground(new FindCallback<ParseUser>() {
//            public void done(List<ParseUser> user, com.parse.ParseException e) {
//                if (e == null) {
//                    Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
//                    intent.putExtra("RECIPIENT_ID", user.get(0).getObjectId());
//                    startActivity(intent);
//                } else {
//                    Toast.makeText(getApplicationContext(),
//                            "Error finding that user",
//                            Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
        intent.putExtra("USER1_ID", "vicky");
        intent.putExtra("USER2_ID", "Qi");
        startActivity(intent);
    }
}
