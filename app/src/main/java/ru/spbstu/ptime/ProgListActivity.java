package ru.spbstu.ptime;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
public class ProgListActivity extends Activity {
    ListView progListView;
    private ArrayAdapter<File> progListAdapter;
    private File filesDir;
    private static final String DEFAULT_PROGRAM =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>%n" +
            "<program name=\"%s\">%n" +
            "    <stopwatch/>%n" +
            "</program>%n";

    private void refreshFileList() {
        progListAdapter.clear();
        progListAdapter.addAll(Arrays.asList(filesDir.listFiles()));
        progListAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proglist);
        filesDir = getApplicationContext().getFilesDir();
        progListAdapter = new ArrayAdapter<>(this, R.layout.proglist_item, R.id.progListItemTextView,
                new ArrayList<>(Arrays.asList(filesDir.listFiles())));
        progListView = (ListView) findViewById(R.id.progListView);
        progListView.setAdapter(progListAdapter);
        progListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(progListView.getContext());
                final File file = progListAdapter.getItem(position);//files.get(position);
                builder.setTitle(file.getName());
                builder.setItems(new CharSequence[]{"Run", "Edit", "Delete"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                // Run program...
                                break;
                            case 1:
                                // Edit program...
                                break;
                            case 2: /* Delete program */
                                if (file.delete())
                                    refreshFileList();
                                break;
                            default:
                                break;
                        }
                    }
                });
                builder.show();
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshFileList();
    }

    public void onAddProgramClick(View v) {
        final EditText editText = new EditText(this); /* TO-DO: добавить фильтр вводимых символов. */
        editText.setSingleLine();
        new AlertDialog.Builder(this)
                .setTitle("Name")
                .setMessage("Enter name for a new program:")
                .setView(editText)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogInterface.BUTTON_POSITIVE) {
                            final String name = editText.getText().toString();
                            /* Здесь будет переход в ConstructorActivity. */
                            try {
                                PrintStream out = new PrintStream(filesDir.getPath() + '/' + name + ".xml");
                                out.format(DEFAULT_PROGRAM, name);
                                out.close();
                                refreshFileList();
                            }
                            catch (FileNotFoundException|SecurityException e) {
                                    /* TO-DO:
                                     * Попросить пользователя вбить другое имя, если не удается создать с этим. */
                                e.printStackTrace();
                            }
                        }
                    }
                })
                .show();
    }
}