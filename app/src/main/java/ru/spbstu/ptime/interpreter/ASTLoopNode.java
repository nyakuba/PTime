package ru.spbstu.ptime.interpreter;

import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import ru.spbstu.ptime.R;
import ru.spbstu.ptime.constructor.ItemAdapter;
import ru.spbstu.ptime.constructor.items.ListItem;

public class ASTLoopNode extends ASTNode {
    protected ASTNode inner;  // Поддерево внутри цикла
    protected int iterations;

    public ASTLoopNode(ASTNode inner, int iterations) {
        super(R.layout.loop_start_item);
        this.inner = inner;
        this.iterations = iterations;
    }
    public ASTLoopNode(int iterations) {
        this(null, iterations);
    }

    public void setBody(ASTNode body) {
        this.inner = body;
    }
    public ASTNode getBody() {
        return inner;
    }
    public int getIterations() {
        return iterations;
    }

    @Override
    public ASTNode interpret(ASTInterpreter interpreter) {
        interpreter.runLoop(this);
        return next;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof ASTLoopNode) {
            ASTLoopNode node = (ASTLoopNode) other;
            return inner.equals(node.inner) && iterations == node.iterations;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return inner.hashCode()*31 + iterations;
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
        mTextView.setText(iterations + " iterations.");
    }

    @Override
    public ASTNode getASTNode() {
        return this;
    }
}
