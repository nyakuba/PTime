package ru.spbstu.ptime;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import ru.spbstu.ptime.constructor.ConstructorActivity;

public class ProgListActivity extends Activity {
    private ListView progListView;
    private ArrayAdapter<File> progListAdapter;
    private File filesDir;

    private void refreshFileList() {
        progListAdapter.clear();
        progListAdapter.addAll(Arrays.asList(filesDir.listFiles()));
        progListAdapter.notifyDataSetChanged();
    }

    private void goToConstructorActivity(int purpose, final String filepath) {
        Intent intent = new Intent(ProgListActivity.this, ConstructorActivity.class);
        intent.putExtra("purpose", purpose);
        if (purpose != ConstructorActivity.PURPOSE_NEW)
            intent.putExtra("filepath", filepath);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proglist);
        filesDir = new File(getApplicationContext().getFilesDir().getAbsolutePath() + "/programs");
        if (!filesDir.exists())
            filesDir.mkdirs(); /* TODO: обработать ошибку, если не создается директория '/programs' */
        progListAdapter = new ArrayAdapter<>(this, R.layout.proglist_item, R.id.progListItemTextView,
                new ArrayList<>(Arrays.asList(filesDir.listFiles())));
        progListView = (ListView) findViewById(R.id.progListView);
        progListView.setAdapter(progListAdapter);
        progListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(progListView.getContext());
                final File file = progListAdapter.getItem(position);
                if (file != null) {
                    builder.setTitle(file.getName());
                    builder.setItems(new CharSequence[]{"Run", "Edit", "Delete"}, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent;
                            switch (which) {
                                case 0: /* Run program */
                                    goToConstructorActivity(ConstructorActivity.PURPOSE_RUN, file.getAbsolutePath());
                                    break;
                                case 1: /* Edit program. Create a new ConstructorActivity and pass file to edit. */
                                    goToConstructorActivity(ConstructorActivity.PURPOSE_EDIT, file.getAbsolutePath());
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
                }
                return true;
                /* Returning true after performing onItemLongClick prevents firing onItemClick event after onItemLongClick. */
            }
        });
        progListView.setOnItemClickListener((adapterView, view, position, id) -> {
            final File file = progListAdapter.getItem(position);
            if (file != null)
                goToConstructorActivity(ConstructorActivity.PURPOSE_EDIT, file.getAbsolutePath());
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshFileList();
    }

    public void onAddProgramClick(View v) {
        goToConstructorActivity(ConstructorActivity.PURPOSE_NEW, null);
    }
}