package com.example.timakiv.notesapp;

import android.app.Activity;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.timakiv.notesapp.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends Activity {

    private List<String> noteList;
    private ArrayAdapter<String> arrayAdapter;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        final DBHelper db = new DBHelper(this);

        final ListView lv = (ListView) findViewById(R.id.lv);
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        String[] items = new String[]{};
        noteList = new ArrayList<String>(Arrays.asList(items));

        arrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_multiple_choice, noteList);
        lv.setAdapter(arrayAdapter);

        binding.buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if (!binding.editText.getText().toString().isEmpty()) {
                        noteList.add(binding.editText.getText().toString());
                        arrayAdapter.notifyDataSetChanged();

                        db.insertNote(binding.editText.getText().toString());
                    }
                    binding.editText.setText("");
            }
        });

        binding.buttonSee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noteList.clear();
                arrayAdapter.notifyDataSetChanged();

                Cursor resultSet = db.getData();
                String array[] = new String[resultSet.getCount()];
                int i = 0;

                resultSet.moveToFirst();
                while (!resultSet.isAfterLast()) {
                    array[i] = resultSet.getString(0);
                    noteList.add(array[i]);
                    i++;
                    resultSet.moveToNext();
                }
                arrayAdapter.notifyDataSetChanged();
            }
        });

        binding.buttonRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noteList.clear();
                arrayAdapter.notifyDataSetChanged();
                db.deleteAll();
            }
        });

        binding.buttonRemoveSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SparseBooleanArray checkedItemPositions = lv.getCheckedItemPositions();
                int itemCount = lv.getCount();

                if (checkedItemPositions.size() != 0) {
                    for (int i = itemCount - 1; i >= 0; i--) {
                        if (checkedItemPositions.get(i)) {
                            db.deleteNote(noteList.get(i));
                            arrayAdapter.remove(noteList.get(i));
                        }
                    }
                }
                checkedItemPositions.clear();
                arrayAdapter.notifyDataSetChanged();
            }
        });
    }
}
