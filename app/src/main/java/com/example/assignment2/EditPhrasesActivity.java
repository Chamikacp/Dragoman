package com.example.assignment2;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;


public class EditPhrasesActivity extends AppCompatActivity {

    private DragomanDBHelper dragomanDBHelper;
    private ListView listViewEdit;
    private EditText editText;
    ListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_phrases);

        dragomanDBHelper = new DragomanDBHelper(this);
        listViewEdit = findViewById(R.id.listView_edit);
        editText = findViewById(R.id.editText_edit);
        displayAllData();

    }

    //To view the all available words
    public void displayAllData(){

        ArrayList<String> arrayList = new ArrayList<>();
        Cursor cursor = dragomanDBHelper.getAllData();

        if (cursor.getCount() == 0){
            Toast.makeText(EditPhrasesActivity.this,"NO DATA AVAILABLE",Toast.LENGTH_LONG).show();
            return;
        }

        while (cursor.moveToNext()){
            arrayList.add(cursor.getString(1));
        }
        listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice,arrayList);
        listViewEdit.setAdapter(listAdapter);
    }

    //To get the selected word to the edit text
    public void editButton(View view){

        int position = listViewEdit.getCheckedItemPosition();
        String word = (String) listViewEdit.getItemAtPosition(position);

        if (position > -1){
            editText.setText(word);
        }else {
            Toast.makeText(EditPhrasesActivity.this,"SELECT A PHRASE",Toast.LENGTH_LONG).show();
        }

        //To get the item in list view
        listViewEdit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                position  = listViewEdit.getCheckedItemPosition();
                String word = (String) listViewEdit.getItemAtPosition(position);
                if (position > -1) {
                    editText.setText(word);
                }
            }
        });

    }

    //To update the edited word in database
    public void saveButton(View view){
        int position = listViewEdit.getCheckedItemPosition();

        String word = (String) listViewEdit.getItemAtPosition(position);

        String newPhrase = editText.getText().toString();

        if (position > -1){
            if (newPhrase.equals(word)){
                Toast.makeText(EditPhrasesActivity.this,"EDIT THE PHRASE",Toast.LENGTH_LONG).show();

            }else if (newPhrase.equals("")){
                Toast.makeText(EditPhrasesActivity.this,"ENTER A PHRASE",Toast.LENGTH_LONG).show();

            }else {
                dragomanDBHelper.editPhrase(newPhrase,word);
                editText.setText("");
                Toast.makeText(EditPhrasesActivity.this,"SAVED SUCCESSFULLY",Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(EditPhrasesActivity.this,"SELECT A PHRASE",Toast.LENGTH_LONG).show();
        }

        //To refresh the list view
        displayAllData();
    }

    //To delete a word from database
    public void deleteButton(View view){
        int position = listViewEdit.getCheckedItemPosition();

        String word = (String) listViewEdit.getItemAtPosition(position);

         if (position > -1){
             dragomanDBHelper.deletePhrase(word);
             Toast.makeText(EditPhrasesActivity.this,"DELETED SUCCESSFULLY",Toast.LENGTH_LONG).show();
         }else {
             Toast.makeText(EditPhrasesActivity.this,"SELECT A PHRASE",Toast.LENGTH_LONG).show();
         }

         //To refresh the list view
         displayAllData();

         editText.setText("");
    }
}
