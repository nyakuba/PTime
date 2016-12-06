package ru.spbstu.ptime.constructor.items;

import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import ru.spbstu.ptime.R;
import ru.spbstu.ptime.constructor.ItemAdapter;

/**
 * Created by nick_yakuba on 12/5/16.
 */

public class LoopStartItem implements ListItem {
    private final @LayoutRes int mLayoutId = R.layout.loop_start_item;
    private TextView mTextView;
    private long mIterations;

    public LoopStartItem(long iterations) {
        mIterations = iterations;
    }

    @Override
    public void initializeLayout(Long id, ItemAdapter.ViewHolder holder, ItemAdapter adapter) {
        LinearLayout item = (LinearLayout) holder.mItemLayout;
        holder.mItemLayout.setPadding(adapter.getIndentation(id), 0, 0, 0);
        // since we generate the ListItem content dynamically,
        // we will just recreate layout using LayoutInflater
        item.removeAllViews();
        LayoutInflater inflater = holder.mInflater;
        LinearLayout layout = (LinearLayout) inflater.inflate(mLayoutId, item, true);
        mTextView = (TextView) layout.findViewById(R.id.text);
        mTextView.setText(mIterations + " iterations.");
    }
}
