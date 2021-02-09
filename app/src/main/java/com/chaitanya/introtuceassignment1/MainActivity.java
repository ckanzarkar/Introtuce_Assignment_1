package com.chaitanya.introtuceassignment1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.chaitanya.introtuceassignment1.adapters.TabAdapter;
import com.chaitanya.introtuceassignment1.fragments.EnrollUserFragment;
import com.chaitanya.introtuceassignment1.fragments.UsersListFragment;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabAdapter tabAdapter;

    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        fragmentManager = getSupportFragmentManager();
        tabAdapter = new TabAdapter(fragmentManager);
        tabAdapter.addFragment(new UsersListFragment(),"Users");
        tabAdapter.addFragment(new EnrollUserFragment(),"Enroll");

        viewPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void init(){
        viewPager = findViewById(R.id.vpg_users);
        tabLayout = findViewById(R.id.tbl_users);
    }
}