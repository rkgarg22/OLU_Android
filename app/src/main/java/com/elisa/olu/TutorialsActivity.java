package com.elisa.olu;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.elisa.olu.TutorialFragment.TutorialFragment1;
import com.elisa.olu.TutorialFragment.TutorialFragment2;
import com.elisa.olu.TutorialFragment.TutorialFragment3;
import com.elisa.olu.TutorialFragment.TutorialFragment4;
import com.elisa.olu.TutorialFragment.TutorialFragment5;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import infrastructure.AppCommon;

/**
 * Created by Ankit Chhabra on 4/29/2018.
 */

public class TutorialsActivity extends AppCompatActivity {

    @BindView(R.id.tabs)
    TabLayout tabLayoutPackage;

    @BindView(R.id.viewpager)
    ViewPager viewPagerPackage;

    ViewPagerAdapter adapter;

    private final List<Fragment> mFragmentList = new ArrayList<>();


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorials);
        ButterKnife.bind(this);
        setupViewPager(viewPagerPackage);
        tabLayoutPackage.setupWithViewPager(viewPagerPackage);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new TutorialFragment1(), "");
        adapter.addFragment(new TutorialFragment2(), "");
        adapter.addFragment(new TutorialFragment3(), "");
        adapter.addFragment(new TutorialFragment4(), "");
        adapter.addFragment(new TutorialFragment5(), "");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @OnClick(R.id.skipBtn)
    void skipBtn(){
        if (AppCommon.getInstance(TutorialsActivity.this).getCurrentUser() == 2) {
            Intent intent = new Intent(TutorialsActivity.this, HomeActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(TutorialsActivity.this, TrainerHomeActivity.class);
            startActivity(intent);
        }
    }
}