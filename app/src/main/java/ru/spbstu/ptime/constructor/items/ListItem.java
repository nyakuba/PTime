package ru.spbstu.ptime.constructor.items;

import android.view.View;

import ru.spbstu.ptime.constructor.ItemAdapter;

/**
 * ListItem interface
 */
public interface ListItem {

    void initializeLayout(final Long id, final View itemLayout, final ItemAdapter adapter);

}

