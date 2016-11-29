package ru.spbstu.ptime.constructor;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.woxthebox.draglistview.DragListView;

import ru.spbstu.ptime.R;
import ru.spbstu.ptime.constructor.items.StopwatchItem;
import ru.spbstu.ptime.constructor.items.TimerByIntervalItem;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_constructor);


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

    public void onAddItemClick(View view) {
//        mAdapter.add("New item!", id++);
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
        super.onStart();
    }

    @Override
    protected void onStop() { // состояние остановки работы Activity
        super.onStop();
    }

    private void makeSampleItems(ItemAdapter adapter) {
        adapter.addItem(new TimerByIntervalItem(10L));
        adapter.addItem(new StopwatchItem());
        adapter.addItem(new TimerByIntervalItem(5L));
    }
}
