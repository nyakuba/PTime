package ru.spbstu.ptime.interpreter;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import ru.spbstu.ptime.R;
import ru.spbstu.ptime.constructor.ItemAdapter;
import ru.spbstu.ptime.constructor.TimeController;
import ru.spbstu.ptime.constructor.ViewUpdater;
import ru.spbstu.ptime.constructor.items.ListItem;

public class ASTTimerByIntervalNode extends ASTNode implements ViewUpdater<Long> {
    private long seconds;
    private final TimeController timeController;

    public ASTTimerByIntervalNode(long seconds) {
        super(R.layout.timer_by_interval_item);
        timeController = new TimeController();
        this.seconds = seconds;
    }

    public TimeController getTimeController() {
        return timeController;
    }

    public long getSeconds() {
        return seconds;
    }

    @Override
    public ASTNode interpret(ASTInterpreter interpreter) {
        interpreter.runTimer(this);
        return next;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof ASTTimerByIntervalNode) {
            return seconds == ((ASTTimerByIntervalNode) other).seconds;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (int) seconds;
    }

    @Override
    public void initializeLayout(final Long id, final ItemAdapter.ViewHolder holder, final ItemAdapter adapter) {
        LinearLayout item = (LinearLayout) holder.mItemLayout;
        holder.mItemLayout.setPadding(adapter.getIndentation(id), 0, 0, 0);
        // since we generate the ListItem content dynamically,
        // we will just recreate layout using LayoutInflater
        item.removeAllViews();
        LayoutInflater inflater = holder.mInflater;
        LinearLayout layout = (LinearLayout) inflater.inflate(mLayoutId, item, true);
        mTextView = (TextView) layout.findViewById(R.id.text);
        mTextView.setText(seconds + " sec. finished");

        Button btnStart = (Button) layout.findViewById(R.id.btnStart);
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
                timeController.pause();
            }
        });

        Button btnStop = (Button) layout.findViewById(R.id.btnStop);
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeController.stop();
            }
        });

        Button btnDel = (Button) layout.findViewById(R.id.btnDel);
        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeController.stop();
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
                mTextView.setText(seconds + " sec. finished");
            }
        }
    }

    @Override
    public ASTNode getASTNode() {
        return this;
    }
}
