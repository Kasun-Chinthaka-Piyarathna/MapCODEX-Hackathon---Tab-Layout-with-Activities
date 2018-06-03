package com.example.kasunchinthaka.mapcodex;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

public class TabBarActivity extends TabActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        TabHost tabHost = getTabHost();
        TabHost.TabSpec spec;
        Intent intent;

        intent = new Intent().setClass(this, FirstActivity.class);
        spec = tabHost.newTabSpec("MAP").setIndicator("MAP")
                .setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, SecondActivity.class);
        spec = tabHost.newTabSpec("SEARCH").setIndicator("SEARCH")
                .setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, ThirdActivity.class);
        spec = tabHost.newTabSpec("ALL").setIndicator("ALL")
                .setContent(intent);
        tabHost.addTab(spec);

    }

}
