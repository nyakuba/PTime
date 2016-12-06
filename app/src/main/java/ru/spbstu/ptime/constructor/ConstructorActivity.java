package ru.spbstu.ptime.constructor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.woxthebox.draglistview.DragListView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;

import ru.spbstu.ptime.R;
import ru.spbstu.ptime.constructor.items.LoopEndItem;
import ru.spbstu.ptime.constructor.items.LoopStartItem;
import ru.spbstu.ptime.constructor.items.StopwatchItem;
import ru.spbstu.ptime.constructor.items.TimerByIntervalItem;
import ru.spbstu.ptime.interpreter.Program;

/**
 * Timer program constructor
 * Relies on https://github.com/woxblom/DragListView
 *
 * To compile successfully add

 repositories {
    mavenCentral()
 }

 dependencies {
    compile 'com.github.woxthebox:draglistview:1.3'
    compile 'com.android.support:recyclerview-v7:24.+'
 }

 * to build.gradle and

  -keep class com.woxthebox.draglistview.** { *; }

 * to proguard-rules.pro
 */


public class ConstructorActivity extends Activity {
    private ItemAdapter mAdapter;
    private TimeEngine mTimeEngine;

    public static final int PURPOSE_NEW = 0;
    public static final int PURPOSE_EDIT = 1;
    public static final int PURPOSE_RUN = 2;
    private int purpose = PURPOSE_NEW;
    private String filepath;

    private void runProgram(Program p) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_constructor);

        Intent intent = getIntent();
        purpose = intent.getIntExtra("purpose", PURPOSE_NEW);
        if (purpose != PURPOSE_NEW)
            filepath = intent.getStringExtra("filepath");

        final DragListView mDragListView = (DragListView) findViewById(R.id.drag_list_view);
        mDragListView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new ItemAdapter(R.layout.constructor_item, R.id.constructor_item);
        makeSampleItems(mAdapter);
        mDragListView.setAdapter(mAdapter, false);
        mDragListView.setDragEnabled(true);
        mDragListView.setCanDragHorizontally(false);
        mDragListView.setCanNotDragBelowBottomItem(true);
        mDragListView.getRecyclerView().setVerticalScrollBarEnabled(true);
//        mDragListView.setCustomDragItem(new DragItem(this, R.layout.constructor_item) {
//            @Override
//            public void onBindDragView(View clickedView, View dragView) {
//                CharSequence text = ((TextView) clickedView.findViewById(R.id.textView1)).getText();
//                ((TextView) dragView.findViewById(R.id.textView1)).setText(text);
//                dragView.setBackgroundColor(Color.LTGRAY);
//            }
//        });
    }

    @Override
    protected void onDestroy() { // состояние Activity удален
        super.onDestroy();
    }

    @Override
    protected void onPause() { // состояние Activity приостановлен
        super.onPause();
    }

    @Override
    protected void onRestart() { // состояние Activity перезапущен
        super.onRestart();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onResume() { // состояние Activity возобновлен
        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) { // сохранение данных при перезапуске Activity
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStart() { // состояние запуска Activity
//        Toast.makeText(this, String.valueOf(purpose) + " | " + filepath, Toast.LENGTH_LONG).show();
        super.onStart();
    }

    @Override
    protected void onStop() { // состояние остановки работы Activity
        super.onStop();
    }

    private void makeSampleItems(ItemAdapter adapter) {
        adapter.addItem(new TimerByIntervalItem(10L));
        adapter.addItem(new StopwatchItem());
        adapter.addItem(new LoopStartItem(3L));
        adapter.addItem(new TimerByIntervalItem(5L));
        adapter.addItem(new LoopEndItem());
    }
}
