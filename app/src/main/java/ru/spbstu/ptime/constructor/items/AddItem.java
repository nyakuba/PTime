package ru.spbstu.ptime.constructor.items;

import android.support.v4.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.List;

import ru.spbstu.ptime.constructor.ItemAdapter;

/**
 * Created by nick_yakuba on 11/13/16.
 */

public class AddItem implements ListItem {

    @Override
    public void initializeLayout(final Long id, final ItemAdapter.ViewHolder holder, final ItemAdapter adapter) {
        LinearLayout item = (LinearLayout) holder.mItemLayout;
//        if (!AddItem.class.equals(item.getTag())) {
            item.removeAllViews();
            Button button = new Button(item.getContext());
            button.setText("Add new item");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapter.addItem(new StopwatchItem());
                    adapter.notifyDataSetChanged();
                }
            });
            item.addView(button);
//            item.setTag(AddItem.class);
//        }
    }
}
