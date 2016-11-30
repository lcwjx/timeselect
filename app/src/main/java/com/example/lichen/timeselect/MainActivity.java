package com.example.lichen.timeselect;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private TextView selecttime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        selecttime = (TextView) findViewById(R.id.selecttime);
        selecttime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeSelector();
            }
        });
    }
    private String currentTime, currentDepart;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
    private void showTimeSelector() {

        TimeSelectFragment.TimeSelectConfig config = new TimeSelectFragment.TimeSelectConfig();
        config.IsCirculation = true;
        config.yearStart = 1980;
        config.timeSelectType = TimeSelectFragment.TimeSelectConfig.TimeType1;
        config.unitPlacesType = TimeSelectFragment.TimeSelectConfig.VerticalUnitType;
        TimeSelectFragment timeSelectFragment = new TimeSelectFragment(config);
        timeSelectFragment.setInitTime(new Date());
        timeSelectFragment.setTimeSelectInterface(new TimeSelectFragment.TimeSelectInterface() {
            @Override
            public void onTimeSelect(Date time) {
                currentTime = dateFormat.format(time);
                selecttime.setText(currentTime);
            }
        });
        addFragment(getSupportFragmentManager(), timeSelectFragment, Window.ID_ANDROID_CONTENT, null);

    }
    public static void addFragment(FragmentManager supportFragmentManager, Fragment baseDialogFragment, int content, String tag) {
        FragmentTransaction transaction = supportFragmentManager.beginTransaction();
        Fragment fragment = supportFragmentManager.findFragmentById(content);
        if (fragment != null) {
            transaction.hide(fragment);
        }
        transaction.add(content, baseDialogFragment, tag);
        transaction.addToBackStack(tag);
        transaction.commitAllowingStateLoss();
    }
}
