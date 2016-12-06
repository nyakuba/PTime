package ru.spbstu.ptime.constructor.items;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import ru.spbstu.ptime.R;
import ru.spbstu.ptime.constructor.ItemAdapter;
import ru.spbstu.ptime.constructor.TimeController;
import ru.spbstu.ptime.constructor.TimeCounter;
import ru.spbstu.ptime.constructor.TimeEngine;
import ru.spbstu.ptime.constructor.ViewUpdater;
import ru.spbstu.ptime.interpreter.ASTInterpreter;
import ru.spbstu.ptime.interpreter.ASTInterpreterUI;
import ru.spbstu.ptime.interpreter.ASTNode;
import ru.spbstu.ptime.interpreter.ASTTimerByIntervalNode;

/**
 * Timer that is defined by interval
 */
public class TimerByIntervalItem extends ASTTimerByIntervalNode implements ListItem, ViewUpdater<Long> {
    private final @LayoutRes int mLayoutId = R.layout.timer_by_interval_item;
    private TextView mTextView;

    public TimerByIntervalItem(long seconds) {
        super(seconds);
    }

    @Override
    public void initializeLayout(final Long id, final ItemAdapter.ViewHolder holder, final ItemAdapter adapter) {
        LinearLayout item = (LinearLayout) holder.mItemLayout;
        // since we generate the ListItem content dynamically,
        // we will just recreate layout using LayoutInflater
        item.removeAllViews();
        LayoutInflater inflater = holder.mInflater;
        LinearLayout layout = (LinearLayout) inflater.inflate(mLayoutId, item, true);
        mTextView = (TextView) layout.findViewById(R.id.text);
        mTextView.setText(seconds + " sec. finished");

        Button btnStart = (Button) layout.findViewById(R.id.btnStart);
        final TimeController timer = new TimeController();
        final ViewUpdater<Long> updater = this;
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (!timer.running()) {
//                    TimeEngine.startIntervalTimer(timer, updater, seconds);
//                }
//                timer.start();
            }
        });

        Button btnPause = (Button) layout.findViewById(R.id.btnPause);
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.pause();
            }
        });

        Button btnStop = (Button) layout.findViewById(R.id.btnStop);
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                timer.stop();
            }
        });

        Button btnDel = (Button) layout.findViewById(R.id.btnDel);
        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.stop();
                boolean removed = adapter.removeItemByID(id);
                if (removed) {
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public ASTNode interpret(ASTInterpreter interpreter) {
        final TimeController timer = new TimeController();
        final ViewUpdater<Long> viewUpdater = this;
        TimeEngine.startIntervalTimer(timer, viewUpdater, seconds, (ASTInterpreterUI) interpreter);
        super.interpret(interpreter); /* == interpreter.runTimer(seconds); */
        timer.start();
        return next;
    }

    @Override
    public void updateView(Long currentTime) {
        if (mTextView != null) {
            if (currentTime > 0) {
                mTextView.setText(currentTime + " sec");
            } else {
                mTextView.setText(seconds + " sec. finished");
            }
        }
    }

    @Override
    public ASTNode getASTNode() {
        return this;
    }
}
