package com.example.assignment2;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class AddPhraseActivity extends AppCompatActivity {

    private DragomanDBHelper dragomanDBHelper;
    private EditText editTextAdd;
    Button buttonAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_phrase);

        dragomanDBHelper = new DragomanDBHelper(this);

        editTextAdd = findViewById(R.id.editText_add);
        buttonAdd = findViewById(R.id.button_save);
    }

    //To add a phrase to the database
    public void addData(View view){
        String word = String.valueOf(editTextAdd.getText());

        ArrayList<String> arrayList = new ArrayList<>();

        Cursor cursor = dragomanDBHelper.getAllData();
        while (cursor.moveToNext()){
            arrayList.add(cursor.getString(1));
        }

        String availableWord = editTextAdd.getText().toString();

        boolean found = arrayList.contains(availableWord);
        if (found){
            Toast.makeText(AddPhraseActivity.this,"ENTERED WORD ALREADY AVAILABLE",Toast.LENGTH_LONG).show();

        }else if (word.equalsIgnoreCase("")){
            Toast.makeText(AddPhraseActivity.this,"ENTER A WORD OR PHRASE",Toast.LENGTH_LONG).show();

        }else {
            boolean  isInserted = dragomanDBHelper.insertPhrase(editTextAdd.getText().toString());
            if (isInserted){
                editTextAdd.setText("");
                Toast.makeText(AddPhraseActivity.this,"SAVED SUCCESSFULLY",Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(AddPhraseActivity.this,"NOT SAVED",Toast.LENGTH_LONG).show();
            }
        }
    }

}
