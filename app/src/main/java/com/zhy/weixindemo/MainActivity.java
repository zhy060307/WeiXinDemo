package com.zhy.weixindemo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends FragmentActivity implements View.OnClickListener {

    private ViewPager viewPager;
    private static final String[] TITLES = {"微信", "通讯录", "发现", "我"};
    private List<ChangeColorIconWithTextView> tabIndicator = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getOverflowMenu();
        initView();
        initTabIndicator();

    }

    private void initTabIndicator() {
        ChangeColorIconWithTextView tabOne = (ChangeColorIconWithTextView) findViewById(R.id.tab_one);
        ChangeColorIconWithTextView tabTwo = (ChangeColorIconWithTextView) findViewById(R.id.tab_two);
        ChangeColorIconWithTextView tabThree = (ChangeColorIconWithTextView) findViewById(R.id.tab_three);
        ChangeColorIconWithTextView tabFour = (ChangeColorIconWithTextView) findViewById(R.id.tab_four);
        tabIndicator.add(tabOne);
        tabIndicator.add(tabTwo);
        tabIndicator.add(tabThree);
        tabIndicator.add(tabFour);

        for (ChangeColorIconWithTextView view : tabIndicator) {
            view.setOnClickListener(this);
        }
        tabOne.setIconAlpha(1.0f);
    }

    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.viewPager_content);
        viewPager.setAdapter(new TabViewPager(getSupportFragmentManager()));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (positionOffset > 0) {
                    ChangeColorIconWithTextView view = tabIndicator.get(position);
                    ChangeColorIconWithTextView nextView = tabIndicator.get(position + 1);
                    view.setIconAlpha(1 - positionOffset);
                    nextView.setIconAlpha(positionOffset);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void getOverflowMenu() {
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class
                    .getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public void onClick(View v) {
        resetTab();
        switch (v.getId()) {
            case R.id.tab_one:
                tabIndicator.get(0).setIconAlpha(1.0f);
                viewPager.setCurrentItem(0, false);
                break;
            case R.id.tab_two:
                tabIndicator.get(1).setIconAlpha(1.0f);
                viewPager.setCurrentItem(1, false);
                break;
            case R.id.tab_three:
                tabIndicator.get(2).setIconAlpha(1.0f);
                viewPager.setCurrentItem(2, false);
                break;
            case R.id.tab_four:
                tabIndicator.get(3).setIconAlpha(1.0f);
                viewPager.setCurrentItem(3, false);
                break;
            default:
                break;
        }
    }

    private void resetTab() {
        for (ChangeColorIconWithTextView view : tabIndicator) {
            view.setIconAlpha(0);
        }

    }

    private class TabViewPager extends FragmentPagerAdapter {


        public TabViewPager(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            TabFragment fragment = new TabFragment();
            Bundle bundle = new Bundle();
            bundle.putString(TabFragment.TITLE, TITLES[position]);
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }
    }
}
