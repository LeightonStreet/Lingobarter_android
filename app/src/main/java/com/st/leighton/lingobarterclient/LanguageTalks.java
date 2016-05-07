package com.st.leighton.lingobarterclient;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.gigamole.library.NavigationTabBar;

import java.util.ArrayList;

/**
 * Created by vicky on 06.05.2016.
 */
public class LanguageTalks extends Activity {
    private ArrayList<String> talks = new ArrayList<>();
    ArrayAdapter<String> talksArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, talks);

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horizontal_ntb);
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
        talks.add("vicky");
        talks.add("Qi");

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
                final ListView view = LayoutInflater.from(
                        getBaseContext()).inflate(R.layout.language_talks, null, false);
                view.setAdapter(talksArrayAdapter);

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
                        case 0:
                            model.setBadgeTitle("NTB");
                            break;
                        case 1:
                            model.setBadgeTitle("with");
                            break;
                        case 2:
                            model.setBadgeTitle("title");
                            break;
                        case 3:
                            model.setBadgeTitle("badge");
                            break;
                        case 4:
                            model.setBadgeTitle("777");
                            break;
                        default:
                            break;
                    }
                    navigationTabBar.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            model.showBadge();
                        }
                    }, i * 100);
                }
            }
        }, 500);
    }

    public void openConversation(ArrayList<String> talks, int pos) {
//        ParseQuery<ParseUser> query = ParseUser.getQuery();
//        query.whereEqualTo("username", talks.get(pos));
//        query.findInBackground(new FindCallback<ParseUser>() {
//            public void done(List<ParseUser> user, ParseException e) {
//                if (e == null) {
//                    //start the messaging activity
//                } else {
//                    Toast.makeText(getApplicationContext(),
//                            "Error finding that user",
//                            Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
    }
}
