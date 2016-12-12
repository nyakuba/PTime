package ru.spbstu.ptime.constructor.items;

import android.view.View;

import ru.spbstu.ptime.constructor.ItemAdapter;
import ru.spbstu.ptime.interpreter.ASTNode;

/**
 * ListItem interface
 */
public interface ListItem {
    void initializeLayout(final Long id, final ItemAdapter.ViewHolder holder, final ItemAdapter adapter);
    ASTNode getASTNode();
}

