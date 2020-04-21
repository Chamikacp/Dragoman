package com.example.assignment2;

import androidx.appcompat.app.AppCompatActivity;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.language_translator.v3.LanguageTranslator;
import com.ibm.watson.language_translator.v3.model.IdentifiableLanguage;
import com.ibm.watson.language_translator.v3.model.IdentifiableLanguages;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SubscriptionActivity extends AppCompatActivity {

    private DragomanDBHelper dragomanDBHelper;
    private ListView listViewLanguages;
    private ListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);

        dragomanDBHelper = new DragomanDBHelper(this);
        listViewLanguages = findViewById(R.id.listView_languages);

        //To checked the languages already inserted or not
        Cursor cursor = dragomanDBHelper.getAllLanguages();
        if (cursor.getCount() == 0){
            Toast.makeText(SubscriptionActivity.this,"LANGUAGES ADDING",Toast.LENGTH_LONG).show();
            Languages process = new Languages();
            process.execute();
            displayAllLanguages();

        }else {
            Toast.makeText(SubscriptionActivity.this,"LANGUAGES ALREADY ADDED",Toast.LENGTH_LONG).show();
            displayAllLanguagesWithChecked();
        }

    }

    //To display all  the languages available
    public void displayAllLanguages(){
        ArrayList<String> arrayList = new ArrayList<>();

        Cursor cursor = dragomanDBHelper.getAllLanguages();
        while (cursor.moveToNext()){
            arrayList.add(cursor.getString(1));
        }

        listAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_multiple_choice,arrayList);
        listViewLanguages.setAdapter(listAdapter);
    }

    //To display the checked languages
    public void displayAllLanguagesWithChecked(){

        displayAllLanguages();
        ArrayList<String> arrayList = new ArrayList<>();

        Cursor cursor = dragomanDBHelper.getAllCheckedLanguages();
        while (cursor.moveToNext()){
            arrayList.add(cursor.getString(1));
        }

        for (int i = 0; i < listAdapter.getCount();i++){
            String word = (String) listViewLanguages.getItemAtPosition(i);
            boolean found = arrayList.contains(word);
            if (found){
                listViewLanguages.setItemChecked(i,true);
            }
        }
    }

    //To update the language list with checked languages
    public void updateCheckedLanguages(View view){
        SparseBooleanArray checked = listViewLanguages.getCheckedItemPositions();

        for (int i = 0; i < listAdapter.getCount();i++){
            if (checked.get(i)){
                String word = (String) listViewLanguages.getItemAtPosition(i);
                dragomanDBHelper.updateCheckedLanguages(1,word);

            }else {
                String word = (String) listViewLanguages.getItemAtPosition(i);
                dragomanDBHelper.updateCheckedLanguages(0,word);
            }

            Toast.makeText(SubscriptionActivity.this,"SUBSCRIBED LANGUAGES UPDATED",Toast.LENGTH_LONG).show();
        }
    }


    //the background task with Async
    private class Languages extends AsyncTask<Void,Void, List<IdentifiableLanguage>> {
        private List<IdentifiableLanguage> allLanguages;

        //runs on a background thread
        @Override
        protected List<IdentifiableLanguage> doInBackground(Void... voids) {

            //To get the available languages and there codes to a list from the IBM API
            IamAuthenticator authenticator = new IamAuthenticator("GYfWHFJQyqPfv3z6tb60Wi2pH1sLaBy4Rbsl9thl0AUt");
            LanguageTranslator languageTranslator = new LanguageTranslator("2018-05-01", authenticator);
            languageTranslator.setServiceUrl("https://api.us-south.language-translator.watson.cloud.ibm.com/instances/1ff84823-3c6c-4507-bae0-8de12c215b64");
            IdentifiableLanguages languages = languageTranslator.listIdentifiableLanguages().execute().getResult();
            allLanguages = languages.getLanguages();
            return allLanguages;
        }

        //runs on main thread when work done
        @Override
        protected void onPostExecute(List<IdentifiableLanguage> list) {
            super.onPostExecute(list);

            //To add to the database
            for (Iterator<IdentifiableLanguage> iterator = list.iterator(); iterator.hasNext(); ) {
                IdentifiableLanguage allLanguages = iterator.next();

                String name = allLanguages.getName();
                String code = allLanguages.getLanguage();

                boolean inserted = dragomanDBHelper.insertLanguage(name, code, 0);
                if (inserted) {
                    displayAllLanguages();
                    Toast.makeText(SubscriptionActivity.this, "LANGUAGES ADDED TO THE DATABASE", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(SubscriptionActivity.this, "LANGUAGES NOT ADDED TO THE DATABASE", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

}
