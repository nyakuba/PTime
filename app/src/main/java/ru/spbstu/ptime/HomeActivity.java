package ru.spbstu.ptime;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TabHost;

import ru.spbstu.ptime.constructor.ConstructorActivity;
import ru.spbstu.ptime.constructor.TimeEngine;

public class HomeActivity extends TabActivity {

    private float lastX;
    public TabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tabHost = getTabHost();

        TabHost.TabSpec tabSpec;

        tabSpec = tabHost.newTabSpec("tag1");
        tabSpec.setIndicator("Stopwatch");
        tabSpec.setContent(new Intent(this, ChronoActivity.class));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag2");
        tabSpec.setIndicator("List");
        tabSpec.setContent(new Intent(this, ProgListActivity.class));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag3");
        tabSpec.setIndicator("Timer");
        tabSpec.setContent(new Intent(this, TimerActivity.class));
        tabHost.addTab(tabSpec);

        TimeEngine.prepare(); /* Initialize TimeEngine. */
    }

    @Override
    public boolean onTouchEvent(MotionEvent touchevent) {
        switch (touchevent.getAction()) {
            // when user first touches the screen to swap
            case MotionEvent.ACTION_DOWN: {
                lastX = touchevent.getX();
                break;
            }
            case MotionEvent.ACTION_UP: {
                float currentX = touchevent.getX();

                // if left to right swipe on screen
                if (lastX < currentX) {

                    switchTabs(false);
                }

                // if right to left swipe on screen
                if (lastX > currentX) {
                    switchTabs(true);
                }

                break;
            }
        }
        return false;
    }

    public void switchTabs(boolean direction) {
        if (!direction) // true = move left
        {
            if (tabHost.getCurrentTabTag().equals("tag3"))
                tabHost.setCurrentTabByTag("tag2");
            else if (tabHost.getCurrentTabTag().equals("tag2"))
                tabHost.setCurrentTabByTag("tag1");
            else if (tabHost.getCurrentTabTag().equals("tag1"))
                tabHost.setCurrentTabByTag("tag3");
        } else
        // move right
        {
            if (tabHost.getCurrentTabTag().equals("tag1"))
                tabHost.setCurrentTabByTag("tag2");
            else if (tabHost.getCurrentTabTag().equals("tag2"))
                tabHost.setCurrentTabByTag("tag3");
            else if (tabHost.getCurrentTabTag().equals("tag3"))
                tabHost.setCurrentTabByTag("tag1");
        }
    }
}
