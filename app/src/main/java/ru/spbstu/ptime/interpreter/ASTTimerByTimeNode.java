package ru.spbstu.ptime.interpreter;

import java.util.Date;

import ru.spbstu.ptime.R;
import ru.spbstu.ptime.constructor.ItemAdapter;

public class ASTTimerByTimeNode extends ASTNode {
    protected Date date;

    public ASTTimerByTimeNode(Date date) {
        super(R.layout.timer_by_interval_item);
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public ASTNode interpret(ASTInterpreter interpreter) {
        interpreter.runTimer(this);
        return next;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof ASTTimerByTimeNode) {
            return date.equals(((ASTTimerByTimeNode) other).date);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return date.hashCode();
    }

    @Override
    public void initializeLayout(final Long id, final ItemAdapter.ViewHolder holder, final ItemAdapter adapter) {}

    @Override
    public ASTNode getASTNode() {
        return this;
    }
}
