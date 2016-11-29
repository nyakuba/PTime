package ru.spbstu.ptime.constructor.items;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ru.spbstu.ptime.constructor.ItemAdapter;

/**
 * Stopwatch list item factory
 */
public class StopwatchItem implements ListItem {

    @Override
    public void initializeLayout(final Long id, final View itemLayout, final ItemAdapter adapter) {
        LinearLayout item = (LinearLayout) itemLayout;
//        if (!StopwatchItem.class.equals(item.getTag())) {
            item.removeAllViews();
            TextView text = new TextView(item.getContext());
            text.setText("Stopwatch " + id);

            Button button = new Button(item.getContext());
            button.setText("Remove");
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            button.setLayoutParams(params);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean removed = adapter.removeItemByID(id);
                    if (removed) {
                        adapter.notifyDataSetChanged();
                    }
                }
            });

            RelativeLayout layout = new RelativeLayout(item.getContext());
            LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
            layout.setLayoutParams(params1);
            layout.addView(button);

            item.addView(text);
            item.addView(layout);
//            item.setTag(StopwatchItem.class);
//        }
    }
}
