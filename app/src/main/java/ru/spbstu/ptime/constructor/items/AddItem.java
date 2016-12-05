package ru.spbstu.ptime.constructor.items;

import android.content.DialogInterface;
import android.support.annotation.LayoutRes;
import android.support.v4.util.Pair;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.io.File;
import java.io.IOException;
import java.util.List;

import ru.spbstu.ptime.R;
import ru.spbstu.ptime.constructor.ItemAdapter;

/**
 * Created by nick_yakuba on 11/13/16.
 */

public class AddItem implements ListItem {
    private final @LayoutRes int mLayoutId = R.layout.add_item;

    @Override
    public void initializeLayout(final Long id, final ItemAdapter.ViewHolder holder, final ItemAdapter adapter) {
        LinearLayout item = (LinearLayout) holder.mItemLayout;
        // since we generate the ListItem content dynamically,
        // we will just recreate layout using LayoutInflater
        item.removeAllViews();
        LayoutInflater inflater = holder.mInflater;
        LinearLayout layout = (LinearLayout) inflater.inflate(mLayoutId, item, true);

        Button btnAddNewItem = (Button) layout.findViewById(R.id.btnAddNewItem);
        btnAddNewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.addItem(new StopwatchItem());
                adapter.notifyDataSetChanged();
            }
        });

        Button btnSave = (Button) layout.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(view -> {
            final EditText editText = new EditText(item.getContext()); /* TODO: добавить фильтр вводимых символов. */
            editText.setSingleLine();
            new AlertDialog.Builder(item.getContext())
                    .setTitle("Name")
                    .setMessage("Enter name for a new program:")
                    .setView(editText)
                    .setPositiveButton("OK", (dialog, which) -> { /* new DialogInterface.OnClickListener() { void onClick(dialog, which) }*/
                        if (which == DialogInterface.BUTTON_POSITIVE) {
                            final String name = editText.getText().toString();
                            File filesDir = new File(item.getContext().getFilesDir().getAbsolutePath() + "/programs");
                            try {
                                new File(filesDir.getAbsolutePath() + '/' + name + ".xml").createNewFile();
                            }
                            catch (IOException e) {
                                e.printStackTrace();
                            }
                            /* TODO: проверить, нет ли уже программы с таким именем. */
                        }
                    })
                    .show();
        });
    }
}
