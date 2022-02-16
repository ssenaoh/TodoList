package com.example.todolist;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.TextView;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.example.Model.Task;
import com.example.SQL.SQLClient;
import com.example.todolist.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    ActionBar actionBar;
    SQLClient sqlClient;

    List<Task> taskList = new ArrayList<>();
    List<Task> taskListComplete = new ArrayList<>();
    List<Task> taskListInComplete = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        sqlClient = new SQLClient(binding.getRoot().getContext());

        //ActionBar
        actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.action_bar_title);
        TextView actionBarTitle = actionBar.getCustomView().findViewById(R.id.action_bar_title);
        actionBarTitle.setText(R.string.title_fragment_all);

        //BottomNavigation
        AHBottomNavigationItem nav_complete = new AHBottomNavigationItem(R.string.bot_nav_complete, R.drawable.none_icon, R.color.gray);
        AHBottomNavigationItem nav_all = new AHBottomNavigationItem(R.string.bot_nav_all, R.drawable.none_icon, R.color.gray);
        AHBottomNavigationItem nav_incomplete = new AHBottomNavigationItem(R.string.bot_nav_incomplete, R.drawable.none_icon, R.color.gray);

        binding.homeBottomNav.addItem(nav_complete);
        binding.homeBottomNav.addItem(nav_all);
        binding.homeBottomNav.addItem(nav_incomplete);

        binding.homeBottomNav.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);

        binding.homeBottomNav.setAccentColor(Color.parseColor("#"+Integer.toHexString(getResources().getColor(R.color.blue_light))));
        binding.homeBottomNav.setInactiveColor(Color.parseColor("#"+Integer.toHexString(getResources().getColor(R.color.gray))));
        binding.homeBottomNav.setTitleTextSizeInSp(18, 14);

        //DATA
        taskList = sqlClient.getAllTask();

        for (Task task: taskList) {
            if(task.getTaskCompleteYN() == 1) taskListComplete.add(task);
            else taskListInComplete.add(task);
        }

        //DefaultFragment
        Bundle bundleData = new Bundle();
        bundleData.putParcelableArrayList("taskList", (ArrayList<? extends Parcelable>) taskList);
        binding.homeBottomNav.setCurrentItem(1);
        getFragment(FragmentAll.newInstance(bundleData));

        binding.homeBottomNav.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                if(position == 0) {
                    actionBarTitle.setText(R.string.title_fragment_complete);
                    getFragment(FragmentComplete.newInstance(bundleData));
                }
                if(position == 1) {
                    actionBarTitle.setText(R.string.title_fragment_all);
                    getFragment(FragmentAll.newInstance(bundleData));
                }
                if(position == 2) {
                    actionBarTitle.setText(R.string.title_fragment_incomplete);
                    getFragment(FragmentInComplete.newInstance(bundleData));
                }
                return true;
            }
        });
    }

    public void getFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.home_fragment, fragment)
                .commit();
    }
}