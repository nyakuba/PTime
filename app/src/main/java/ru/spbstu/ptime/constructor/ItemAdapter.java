package ru.spbstu.ptime.constructor;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.woxthebox.draglistview.DragItemAdapter;
import com.woxthebox.draglistview.DragListView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ru.spbstu.ptime.constructor.items.AddItem;
import ru.spbstu.ptime.constructor.items.ListItem;
import ru.spbstu.ptime.constructor.items.LoopEndItem;
//import ru.spbstu.ptime.constructor.items.LoopStartItem;
import ru.spbstu.ptime.interpreter.ASTLoopNode;

/**
 * ItemAdapter represent a factory instance to create draggable list items from given dataset
 */
public class ItemAdapter extends DragItemAdapter<Pair<Long, ListItem>, ItemAdapter.ViewHolder> {
    public static final boolean DRAG_ON_LONG_PRESS = true;

    private static Long mItemId = 0L;
    private List<Pair<Long, ListItem>> mData = new ArrayList<>();
    private int mLayoutId;
    private int mGrabHandleId;

    /**
     * @param layoutId item layout id
     * @param grabHandleId layout to drag
     */
    public ItemAdapter(int layoutId, int grabHandleId) {
        mData.add(new Pair<Long, ListItem>(mItemId++, new AddItem()));
        mLayoutId = layoutId;
        mGrabHandleId = grabHandleId;
        setHasStableIds(true);
        setItemList(mData);
    }

    @Override
    public long getItemId(int position) {
        return mItemList.get(position).first;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(mLayoutId, parent, false);
        return new ViewHolder(view, inflater);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        Pair<Long, ListItem> item = mItemList.get(position);
        item.second.initializeLayout(item.first, holder, this);
    }

    public void addItem(ListItem item) {
        mData.add(mData.size() - 1, new Pair<Long, ListItem>(mItemId++, item));
    }

    public boolean removeItemByID(Long id) {
        boolean removed = false;
        Iterator<Pair<Long, ListItem>> it = mData.iterator();
        while (it.hasNext()) {
            Pair<Long, ListItem> pair = it.next();
            if (pair.first.compareTo(id) == 0) {
                it.remove();
                removed = true;
            }
        }
        return removed;
    }

    public int getIndentation(Long id) {
        Iterator<Pair<Long, ListItem>> iterator = mData.iterator();
        final int padding = 50;
        int level = 0;
        while (iterator.hasNext()) {
            Pair<Long, ListItem> pair = iterator.next();
            if (pair.second instanceof LoopEndItem) {
                --level;
            }
            if (pair.first.equals(id)) {
                break;
            }
            if (pair.second instanceof ASTLoopNode) {
                ++level;
            }
        }
        return Math.max(0, padding*level);
    }

    /**
     * Class that fills item layout with actual data
     */
    public class ViewHolder extends DragItemAdapter.ViewHolder {
        public View mItemLayout;
        public LayoutInflater mInflater;

        public ViewHolder(final View itemView, LayoutInflater inflater) {
            super(itemView, mGrabHandleId, DRAG_ON_LONG_PRESS);
            mItemLayout = itemView;
            mInflater = inflater;
        }
    }
}
