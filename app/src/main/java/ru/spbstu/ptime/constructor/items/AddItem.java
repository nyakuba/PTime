package ru.spbstu.ptime.constructor.items;

import android.content.DialogInterface;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import ru.spbstu.ptime.R;
import ru.spbstu.ptime.constructor.ItemAdapter;
import ru.spbstu.ptime.interpreter.ASTBuilderUI;
import ru.spbstu.ptime.interpreter.ASTInterpreter;
import ru.spbstu.ptime.interpreter.ASTInterpreterRunnable;
import ru.spbstu.ptime.interpreter.ASTInterpreterXML;
import ru.spbstu.ptime.interpreter.ASTLoopNode;
import ru.spbstu.ptime.interpreter.ASTNode;
import ru.spbstu.ptime.interpreter.ASTStopwatchNode;
import ru.spbstu.ptime.interpreter.ASTTimerByIntervalNode;
import ru.spbstu.ptime.interpreter.Program;

/**
 * Created by nick_yakuba on 11/13/16.
 */

public class AddItem implements ListItem {
    private final @LayoutRes int mLayoutId = R.layout.add_item;

    @Override
    public void initializeLayout(final Long id, final ItemAdapter.ViewHolder holder, final ItemAdapter adapter) {
        LinearLayout item = (LinearLayout) holder.mItemLayout;
        holder.mItemLayout.setPadding(adapter.getIndentation(id), 0, 0, 0);
        // since we generate the ListItem content dynamically,
        // we will just recreate layout using LayoutInflater
        item.removeAllViews();
        LayoutInflater inflater = holder.mInflater;
        LinearLayout layout = (LinearLayout) inflater.inflate(mLayoutId, item, true);

        Button btnAddNewItem = (Button) layout.findViewById(R.id.btnAddNewItem);
        final AlertDialog.Builder builder = new AlertDialog.Builder(item.getContext());
        builder.setItems(new CharSequence[]{"Timer", "Stopwatch", "Loop"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: // timer
                        adapter.addItem(new /*TimerByIntervalItem*/ASTTimerByIntervalNode(10L));
                        break;
                    case 1: // stopwatch
                        adapter.addItem(new /*StopwatchItem*/ASTStopwatchNode());
                        break;
                    case 2: // loop
                        adapter.addItem(new /*LoopStartItem*/ASTLoopNode(3));
                        adapter.addItem(new LoopEndItem());
                        break;
                    default: // do nothing
                        break;
                }
                adapter.notifyDataSetChanged();
            }
        });

        btnAddNewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.show();
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
                                File file = new File(filesDir.getAbsolutePath() + '/' + name + ".xml");
                                Program program = new ASTBuilderUI(adapter.getItemList()).getProgram();
                                ASTInterpreterXML interpreterXML = new ASTInterpreterXML(new PrintStream(file));
                                interpreterXML.run(program);
//                                new ASTInterpreterXML(new PrintStream(file)).run(program);
                            }
                            catch (IOException e) {
                                e.printStackTrace();
                            }
                            /* TODO: проверить, нет ли уже программы с таким именем. */
                        }
                    })
                    .show();
        });

        Button btnRun = (Button) layout.findViewById(R.id.btnRun);
        btnRun.setOnClickListener(view -> {
            Program program = new ASTBuilderUI(adapter.getItemList()).getProgram();
            if (null != program)
                new Thread(new ASTInterpreterRunnable(program)).start();
            else
                new AlertDialog.Builder(item.getContext()).setTitle(":C").show();
        });
    }

    @Override
    public ASTNode getASTNode() {
        return null;
    }
}
