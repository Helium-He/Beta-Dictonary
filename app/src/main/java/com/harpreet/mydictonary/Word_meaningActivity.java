package com.harpreet.mydictonary;

import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.harpreet.mydictonary.fragment.FragmenSynonyms;
import com.harpreet.mydictonary.fragment.FragmentAntonyms;
import com.harpreet.mydictonary.fragment.FragmentDefination;
import com.harpreet.mydictonary.fragment.FragmentExample;

import java.util.ArrayList;
import java.util.List;

public class Word_meaningActivity extends AppCompatActivity {
    private ViewPager viewPager;
    TextView textview;
    ImageView rose;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_meaning);
        Toolbar toolbar = findViewById(R.id.mToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar();
        toolbar.setNavigationIcon(R.drawable.back_btn);
        textview = findViewById(R.id.textv);
        rose = findViewById(R.id.rose);
        rose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Word_meaningActivity.this, "like it"  , Toast.LENGTH_SHORT).show();
            }
        });
        textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Toast.makeText(Word_meaningActivity.this, "oh yeah", Toast.LENGTH_SHORT).show();
                Snackbar snackbar = Snackbar.make(v,textview.getText().toString(),Snackbar.LENGTH_LONG);
                snackbar.show();

            }
        });


        viewPager = findViewById(R.id.tab_viewpager);
        if(viewPager!=null)
        {
            setupViewPager(viewPager);
        }

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }

    private class viewpagerAdapter extends FragmentPagerAdapter{
        private final  List<Fragment> mFragmentList  = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();
        viewpagerAdapter(FragmentManager manager)
        {
            super(manager);
        }
        void addFrag(Fragment fragment, String title)
        {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);

        }

        @Override
        public Fragment getItem(int i) {
            return mFragmentList.get(i);

        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private  void setupViewPager(ViewPager viewPager)
    {
        viewpagerAdapter  adapter = new viewpagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new FragmentDefination(),"Definition");
        adapter.addFrag(new FragmenSynonyms(),"Synonyms");
        adapter.addFrag(new FragmentAntonyms(),"Antonyms");
        adapter.addFrag(new FragmentExample(),"Example");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}
