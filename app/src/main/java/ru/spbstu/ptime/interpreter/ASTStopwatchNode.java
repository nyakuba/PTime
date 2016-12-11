package ru.spbstu.ptime.interpreter;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ru.spbstu.ptime.R;
import ru.spbstu.ptime.constructor.ItemAdapter;
import ru.spbstu.ptime.constructor.TimeController;
import ru.spbstu.ptime.constructor.ViewUpdater;
import ru.spbstu.ptime.constructor.items.ListItem;

public class ASTStopwatchNode extends ASTNode implements ViewUpdater<Long> {
    private static final int hash = (int) (Math.random() * Long.MAX_VALUE);

    public ASTStopwatchNode() {
        super(R.layout.stopwatch_item);
    }

    @Override
    public ASTNode interpret(ASTInterpreter interpreter) {
        interpreter.runStopwatch(this);
        return next;
    }

    @Override
    public boolean equals(Object other) {
        return true;
    }

    @Override
    public int hashCode() {
        return hash;
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
        updateView(0L);

        Button btnStart = (Button) layout.findViewById(R.id.btnStart);
        final TimeController stopwatch = new TimeController();
        final ViewUpdater<Long> updater = this;
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (!stopwatch.running()) {
//                    TimeEngine.startStopwatch(stopwatch, updater);
//                }
//                stopwatch.start();
            }
        });

        Button btnPause = (Button) layout.findViewById(R.id.btnPause);
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopwatch.pause();
            }
        });

        Button btnStop = (Button) layout.findViewById(R.id.btnStop);
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                stopwatch.stop();
            }
        });

        Button btnDel = (Button) layout.findViewById(R.id.btnDel);
        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopwatch.stop();
                boolean removed = adapter.removeItemByID(id);
                if (removed) {
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void updateView(Long time) {
        // TODO: get rid of manual time compensation
        // we need to compensate time for three hours
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss.SSS");
        String time_str = formatter.format(new Date(time - 3*3600000));
        mTextView.setText(time_str);
    }

    @Override
    public ASTNode getASTNode() {
        return this;
    }
}
