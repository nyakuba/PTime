//package ru.spbstu.ptime.constructor.items;
//
//import android.support.annotation.LayoutRes;
//import android.view.LayoutInflater;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import ru.spbstu.ptime.R;
//import ru.spbstu.ptime.constructor.ItemAdapter;
//import ru.spbstu.ptime.interpreter.ASTLoopNode;
//import ru.spbstu.ptime.interpreter.ASTNode;
//
///**
// * Created by nick_yakuba on 12/5/16.
// */
//
//public class LoopStartItem extends ASTLoopNode implements ListItem {
//    private final @LayoutRes int mLayoutId = R.layout.loop_start_item;
//    private TextView mTextView;
////    private long mIterations;
//
//    public LoopStartItem(int iterations) {
//        super(null, iterations);
////        mIterations = iterations;
//    }
//
//    @Override
//    public void initializeLayout(Long id, ItemAdapter.ViewHolder holder, ItemAdapter adapter) {
//        LinearLayout item = (LinearLayout) holder.mItemLayout;
//        holder.mItemLayout.setPadding(adapter.getIndentation(id), 0, 0, 0);
//        // since we generate the ListItem content dynamically,
//        // we will just recreate layout using LayoutInflater
//        item.removeAllViews();
//        LayoutInflater inflater = holder.mInflater;
//        LinearLayout layout = (LinearLayout) inflater.inflate(mLayoutId, item, true);
//        mTextView = (TextView) layout.findViewById(R.id.text);
//        mTextView.setText(iterations + " iterations.");
//    }
//
////    @Override
////    public ASTNode getASTNode() {
////        return this;
////    }
//}
