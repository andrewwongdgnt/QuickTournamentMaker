package com.dgnt.quickTournamentMaker.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.dgnt.quickTournamentMaker.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Copyright (C) 2011 by Brad Greco <brad@bgreco.net>
 * <p/>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

public class DirectoryPickerActivity extends AppCompatActivity {

    public static final String START_DIR = "startDir";
    public static final String ONLY_DIRS = "onlyDirs";
    public static final String SHOW_HIDDEN = "showHidden";
    public static final String CHOSEN_DIRECTORY = "chosenDir";
    public static final String FILE_PICKER = "isFilePicker";
    public static final String OVERRIDING_REQUEST_CODE = "overridingRequestCode";
    public static final int DIRECTORY_PICKER_DEFAULT_REQUEST_CODE = 666;
    private File dir;
    private boolean showHidden = false;
    private boolean onlyDirs = false;
    private boolean filePicker = false;
    private int requestCode = DIRECTORY_PICKER_DEFAULT_REQUEST_CODE;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directory_picker);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        dir = Environment.getExternalStorageDirectory();

        //Test


        if (extras != null) {
            String preferredStartDir = extras.getString(START_DIR);
            showHidden = extras.getBoolean(SHOW_HIDDEN, false);
            onlyDirs = extras.getBoolean(ONLY_DIRS, false);
            filePicker = extras.getBoolean(FILE_PICKER, false);
            requestCode = extras.getInt(OVERRIDING_REQUEST_CODE, DIRECTORY_PICKER_DEFAULT_REQUEST_CODE);

            if (preferredStartDir != null) {
                File startDir = new File(preferredStartDir);
                if (startDir.isDirectory()) {
                    dir = startDir;
                }
            }
        }

        setTitle(dir.getAbsolutePath());
        Button btnChoose = (Button) findViewById(R.id.chooseDir_btn);
        if (filePicker)
            btnChoose.setVisibility(View.GONE);
        String name = dir.getName();
        if (name.length() == 0)
            name = "/";
        btnChoose.setText(getResources().getString(R.string.directoryChoose, name));
        btnChoose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                returnDir(dir.getAbsolutePath());
            }
        });

        ListView lv = (ListView) findViewById(R.id.dirList);
        lv.setTextFilterEnabled(true);

        if (!dir.canRead()) {
            Context context = getApplicationContext();
            String msg = getResources().getString(R.string.directoryCannotReadMsg);
            Toast toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
            toast.show();
            return;
        }

        final ArrayList<File> files = filter(dir.listFiles(), onlyDirs, showHidden);
        String[] names = names(files);
        lv.setAdapter(new ArrayAdapter<String>(this, R.layout.directory_list_item, names));


        lv.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String path = files.get(position).getAbsolutePath();

                if (!files.get(position).isDirectory()) {
                    if (filePicker)
                    {
                        returnDir(path);
                    }
                    return;
                }
                Intent intent = new Intent(DirectoryPickerActivity.this, DirectoryPickerActivity.class);
                intent.putExtra(DirectoryPickerActivity.START_DIR, path);
                intent.putExtra(DirectoryPickerActivity.SHOW_HIDDEN, showHidden);
                intent.putExtra(DirectoryPickerActivity.ONLY_DIRS, onlyDirs);
                intent.putExtra(DirectoryPickerActivity.FILE_PICKER, filePicker);
                startActivityForResult(intent, requestCode);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == requestCode && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            String path = (String) extras.get(DirectoryPickerActivity.CHOSEN_DIRECTORY);
            returnDir(path);
        }
    }

    private void returnDir(String path) {
        Intent result = new Intent();
        result.putExtra(CHOSEN_DIRECTORY, path);
        setResult(RESULT_OK, result);
        finish();
    }

    public ArrayList<File> filter(File[] file_list, boolean onlyDirs, boolean showHidden) {
        ArrayList<File> files = new ArrayList<File>();
        for (File file : file_list) {
            if (onlyDirs && !file.isDirectory())
                continue;
            if (!showHidden && file.isHidden())
                continue;
            files.add(file);
        }
        Collections.sort(files);
        return files;
    }

    public String[] names(ArrayList<File> files) {
        String[] names = new String[files.size()];
        int i = 0;
        for (File file : files) {
            names[i] = file.getName();
            i++;
        }
        return names;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

