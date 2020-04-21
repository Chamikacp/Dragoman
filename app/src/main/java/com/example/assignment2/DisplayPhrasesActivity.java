package com.example.assignment2;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;

public class DisplayPhrasesActivity extends AppCompatActivity {

    private DragomanDBHelper dragomanDBHelper;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_phrases);

        dragomanDBHelper = new DragomanDBHelper(this);
        listView = findViewById(R.id.listView_display);

        displayAllData();
    }

    //To display the words stored in database
    public void displayAllData(){

        ArrayList<String> arrayList = new ArrayList<>();

        Cursor cursor = dragomanDBHelper.getAllData();

        int count = 1;

        if (cursor.getCount() == 0){
            Toast.makeText(DisplayPhrasesActivity.this,"NO PHRASES AVAILABLE",Toast.LENGTH_LONG).show();
            return;
        }

        while (cursor.moveToNext()){
            arrayList.add(count + ". " + cursor.getString(1));
            ListAdapter listAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,arrayList);
            listView.setAdapter(listAdapter);
            count++;
        }
    }
}
