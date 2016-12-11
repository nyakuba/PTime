package ru.spbstu.ptime.interpreter;

import android.support.annotation.LayoutRes;
import android.widget.TextView;

import ru.spbstu.ptime.R;
import ru.spbstu.ptime.constructor.items.ListItem;

abstract public class ASTNode implements ListItem {
    private static final int hash = (int) (Math.random() * Long.MAX_VALUE);
    protected ASTNode next;

    protected final @LayoutRes int mLayoutId;
    protected TextView mTextView;

    public ASTNode(int mLayoutId) {
        this.mLayoutId = mLayoutId;
        this.next = null;
    }

    public void setNext(ASTNode next) {
        this.next = next;
    }

    abstract public ASTNode interpret(ASTInterpreter interpreter);

    @Override
    public boolean equals(Object other) {
        if (other instanceof ASTNode) {
            return next.equals(other);
        }
        return false;
    }

    @Override
    public int hashCode() {
        if (next != null) {
            return next.hashCode();
        }
        return hash;
    }
}
