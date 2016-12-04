package ru.spbstu.ptime.constructor.items;

import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import ru.spbstu.ptime.R;
import ru.spbstu.ptime.constructor.ItemAdapter;
import ru.spbstu.ptime.constructor.TimeController;
import ru.spbstu.ptime.constructor.TimeEngine;
import ru.spbstu.ptime.constructor.ViewUpdater;

/**
 * Timer that is defined by interval
 */
public class TimerByIntervalItem extends TimeController implements ListItem, ViewUpdater<Long> {
    private final @LayoutRes int mLayoutId = R.layout.timer_by_interval_item;
    private long mSeconds = 0;
    private TextView mTextView;
    private ItemAdapter mAdapter;

    public TimerByIntervalItem(long seconds) {
        mSeconds = seconds;
    }

    @Override
    public void initializeLayout(final Long id, final ItemAdapter.ViewHolder holder, final ItemAdapter adapter) {
        mAdapter = adapter;
        LinearLayout item = (LinearLayout) holder.mItemLayout;
        LayoutInflater inflater = holder.mInflater;
        LinearLayout layout = (LinearLayout) inflater.inflate(mLayoutId, item, true);
        mTextView = (TextView) layout.findViewById(R.id.text);
        mTextView.setText(mSeconds + " sec. finished");

        Button btnStart = (Button) layout.findViewById(R.id.btnStart);
        final TimeController controller = this;
        final ViewUpdater<Long> updater = this;
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!running()) {
                    TimeEngine.startIntervalTimer(controller, updater, mSeconds);
                }
                start(); // start timer
            }
        });

        Button btnPause = (Button) layout.findViewById(R.id.btnPause);
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pause(); // pause timer
            }
        });

        Button btnStop = (Button) layout.findViewById(R.id.btnStop);
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop(); // stop timer
            }
        });

        Button btnDel = (Button) layout.findViewById(R.id.btnDel);
        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean removed = adapter.removeItemByID(id);
                if (removed) {
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void updateView(Long currentTime) {
        if (mTextView != null) {
            if (currentTime > 0) {
                mTextView.setText(currentTime + " sec");
            } else {
                mTextView.setText(mSeconds + " sec. finished");
            }
        }
    }
}
