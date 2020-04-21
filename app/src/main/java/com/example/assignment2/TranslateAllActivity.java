package com.example.assignment2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.ibm.cloud.sdk.core.security.Authenticator;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.cloud.sdk.core.service.exception.NotFoundException;
import com.ibm.watson.language_translator.v3.LanguageTranslator;
import com.ibm.watson.language_translator.v3.model.TranslateOptions;
import com.ibm.watson.language_translator.v3.model.TranslationResult;
import com.ibm.watson.language_translator.v3.util.Language;

import java.util.ArrayList;

public class TranslateAllActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private DragomanDBHelper dragomanDBHelper;
    private String spinnerLabel;
    private LanguageTranslator translationService;
    private String code;
    private int index = 0;
    ListView listView;
    Spinner languageList;
    String textTranslation;
    String phrase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate_all);

        dragomanDBHelper = new DragomanDBHelper(this);
        displayTheLanguages();

        translationService = initLanguageTranslatorService();

    }

    //To get the code of the language
    public void getCode(){
        Cursor cursor = dragomanDBHelper.getLanguageCode(spinnerLabel);
        while (cursor.moveToNext()){
            code = cursor.getString(2);
        }
    }

    //To display the spinner with languages
    public void displayTheLanguages(){
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("German");
        arrayList.add("Italian");
        arrayList.add("Dutch");
        arrayList.add("Portuguese");
        arrayList.add("Russian");

        // Create the spinner.
        languageList = findViewById(R.id.spinner_languages);

        if (languageList != null) {
            languageList.setOnItemSelectedListener(this);
        }

        ListAdapter listAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,arrayList);

        // Apply the adapter to the spinner.
        if (languageList != null) {
            languageList.setAdapter((SpinnerAdapter) listAdapter);
        }
    }

    //To get the selected label from the spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        spinnerLabel = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    //To translate all available words into selected language
    public void translationAll(View view){
        Cursor cursor1 = dragomanDBHelper.getAllData();
        Cursor cursor2 = dragomanDBHelper.getAllTranslatedGerman();
        Cursor cursor3 = dragomanDBHelper.getAllTranslatedItalian();
        Cursor cursor4 = dragomanDBHelper.getAllTranslatedDutch();
        Cursor cursor5 = dragomanDBHelper.getAllTranslatedPortuguese();
        Cursor cursor6 = dragomanDBHelper.getAllTranslatedRussian();

        if (spinnerLabel.equalsIgnoreCase("German")){
            if (cursor1.getCount() == cursor2.getCount()){
                Toast.makeText(TranslateAllActivity.this,"WORDS ALREADY TRANSLATED",Toast.LENGTH_LONG).show();
            }else if (cursor1.getCount() > cursor2.getCount() || cursor1.getCount() < cursor2.getCount()){
                dragomanDBHelper.deleteAllGerman();
                getCode();
                ArrayList<String> arrayList = new ArrayList<>();
                Cursor cursor = dragomanDBHelper.getAllData();
                while (cursor.moveToNext()){
                    arrayList.add(cursor.getString(1));
                }
                for (int i = 0; i < arrayList.size(); i++){
                    phrase = arrayList.get(i);
                    new DragomanTranslationAllTask().execute(phrase);
                }
                Toast.makeText(TranslateAllActivity.this,"WORDS TRANSLATED",Toast.LENGTH_LONG).show();
                index = 0;
            }

        }else if (spinnerLabel.equalsIgnoreCase("Italian")){
            if (cursor1.getCount() == cursor3.getCount()){
                Toast.makeText(TranslateAllActivity.this,"WORDS ALREADY TRANSLATED",Toast.LENGTH_LONG).show();
            }else if (cursor1.getCount() > cursor3.getCount() || cursor1.getCount() < cursor3.getCount()){
                dragomanDBHelper.deleteAllItalian();
                getCode();
                ArrayList<String> arrayList = new ArrayList<>();
                Cursor cursor = dragomanDBHelper.getAllData();
                while (cursor.moveToNext()){
                    arrayList.add(cursor.getString(1));
                }
                for (int i = 0; i < arrayList.size(); i++){
                    phrase = arrayList.get(i);
                    new DragomanTranslationAllTask().execute(phrase);
                }
                Toast.makeText(TranslateAllActivity.this,"WORDS TRANSLATED",Toast.LENGTH_LONG).show();
                index = 0;
            }
        }else if (spinnerLabel.equalsIgnoreCase("Dutch")){
            if (cursor1.getCount() == cursor4.getCount()){
                Toast.makeText(TranslateAllActivity.this,"WORDS ALREADY TRANSLATED",Toast.LENGTH_LONG).show();
            }else if (cursor1.getCount() > cursor4.getCount() || cursor1.getCount() < cursor4.getCount()){
                dragomanDBHelper.deleteAllDutch();
                getCode();
                ArrayList<String> arrayList = new ArrayList<>();
                Cursor cursor = dragomanDBHelper.getAllData();
                while (cursor.moveToNext()){
                    arrayList.add(cursor.getString(1));
                }
                for (int i = 0; i < arrayList.size(); i++){
                    phrase = arrayList.get(i);
                    new DragomanTranslationAllTask().execute(phrase);
                }
                Toast.makeText(TranslateAllActivity.this,"WORDS TRANSLATED",Toast.LENGTH_LONG).show();
                index = 0;
            }
        }else if (spinnerLabel.equalsIgnoreCase("Portuguese")){
            if (cursor1.getCount() == cursor5.getCount()){
                Toast.makeText(TranslateAllActivity.this,"WORDS ALREADY TRANSLATED",Toast.LENGTH_LONG).show();
            }else if (cursor1.getCount() > cursor5.getCount() || cursor1.getCount() < cursor5.getCount()){
                dragomanDBHelper.deleteAllPortuguese();
                getCode();
                ArrayList<String> arrayList = new ArrayList<>();
                Cursor cursor = dragomanDBHelper.getAllData();
                while (cursor.moveToNext()){
                    arrayList.add(cursor.getString(1));
                }
                for (int i = 0; i < arrayList.size(); i++){
                    phrase = arrayList.get(i);
                    new DragomanTranslationAllTask().execute(phrase);
                }
                Toast.makeText(TranslateAllActivity.this,"WORDS TRANSLATED",Toast.LENGTH_LONG).show();
                index = 0;
            }
        }else if (spinnerLabel.equalsIgnoreCase("Russian")){
            if (cursor1.getCount() == cursor6.getCount()){
                Toast.makeText(TranslateAllActivity.this,"WORDS ALREADY TRANSLATED",Toast.LENGTH_LONG).show();
            }else if (cursor1.getCount() > cursor6.getCount() || cursor1.getCount() < cursor6.getCount()){
                dragomanDBHelper.deleteAllRussian();
                getCode();
                ArrayList<String> arrayList = new ArrayList<>();
                Cursor cursor = dragomanDBHelper.getAllData();
                while (cursor.moveToNext()){
                    arrayList.add(cursor.getString(1));
                }
                for (int i = 0; i < arrayList.size(); i++){
                    phrase = arrayList.get(i);
                    new DragomanTranslationAllTask().execute(phrase);
                }
                Toast.makeText(TranslateAllActivity.this,"WORDS TRANSLATED",Toast.LENGTH_LONG).show();
                index = 0;
            }
        }

    }

    //To view all translated words via a dialog box
    public void viewTranslated(View view){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        View viewList = getLayoutInflater().inflate(R.layout.list, null);

        alertDialog.setNegativeButton("Close",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                    }
                });
        listView = viewList.findViewById(R.id.listView_dialog);

        ArrayList<String> arrayList = new ArrayList<>();
        if (spinnerLabel.equalsIgnoreCase("German")){
            Cursor cursor = dragomanDBHelper.getAllTranslatedGerman();
            while (cursor.moveToNext()){
                arrayList.add(cursor.getString(0) + " - " + cursor.getString(1));
            }
        }else if (spinnerLabel.equalsIgnoreCase("Italian")){
            Cursor cursor = dragomanDBHelper.getAllTranslatedItalian();
            while (cursor.moveToNext()){
                arrayList.add(cursor.getString(0) + " - " + cursor.getString(1));
            }
        }else if (spinnerLabel.equalsIgnoreCase("Dutch")){
            Cursor cursor = dragomanDBHelper.getAllTranslatedDutch();
            while (cursor.moveToNext()){
                arrayList.add(cursor.getString(0) + " - " + cursor.getString(1));
            }
        }else if (spinnerLabel.equalsIgnoreCase("Portuguese")){
            Cursor cursor = dragomanDBHelper.getAllTranslatedPortuguese();
            while (cursor.moveToNext()){
                arrayList.add(cursor.getString(0) + " - " + cursor.getString(1));
            }
        }else if (spinnerLabel.equalsIgnoreCase("Russian")){
            Cursor cursor = dragomanDBHelper.getAllTranslatedRussian();
            while (cursor.moveToNext()){
                arrayList.add(cursor.getString(0) + " - " + cursor.getString(1));
            }
        }
        ListAdapter listAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(listAdapter);
        ((ArrayAdapter) listAdapter).notifyDataSetChanged();
        alertDialog.setView(viewList);
        AlertDialog dialog = alertDialog.create();
        dialog.show();
    }

    //To get the language translator service from the api
    private LanguageTranslator initLanguageTranslatorService() {
        Authenticator authenticator = new IamAuthenticator("GYfWHFJQyqPfv3z6tb60Wi2pH1sLaBy4Rbsl9thl0AUt");
        LanguageTranslator service = new LanguageTranslator("2018-05-01", authenticator);
        service.setServiceUrl("https://api.us-south.language-translator.watson.cloud.ibm.com/instances/1ff84823-3c6c-4507-bae0-8de12c215b64");
        return service;
    }

    private class DragomanTranslationAllTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String firstTranslation = "LANGUAGE NOT FOUND";
            try {
                TranslateOptions translateOptions = new TranslateOptions.Builder()
                        .addText(params[0])
                        .source(Language.ENGLISH)
                        .target(code)
                        .build();
                TranslationResult result = translationService.translate(translateOptions).execute().getResult();
                firstTranslation = result.getTranslations().get(0).getTranslation();
            }catch (NotFoundException e){
                System.out.println("NOT FOUND");
            }
            return firstTranslation;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            textTranslation = result;

            ArrayList<String> arrayList = new ArrayList<>();
            Cursor cursor = dragomanDBHelper.getAllData();
            while (cursor.moveToNext()){
                arrayList.add(cursor.getString(1));
            }

            if (spinnerLabel.equalsIgnoreCase("German")){
                dragomanDBHelper.insertTranslatedGerman(arrayList.get(index),spinnerLabel,textTranslation);
            }else if (spinnerLabel.equalsIgnoreCase("Italian")){
                dragomanDBHelper.insertTranslatedItalian(arrayList.get(index),spinnerLabel,textTranslation);
            }else if (spinnerLabel.equalsIgnoreCase("Dutch")){
                dragomanDBHelper.insertTranslatedDutch(arrayList.get(index),spinnerLabel,textTranslation);
            }else if (spinnerLabel.equalsIgnoreCase("Portuguese")){
                dragomanDBHelper.insertTranslatedPortuguese(arrayList.get(index),spinnerLabel,textTranslation);
            }else if (spinnerLabel.equalsIgnoreCase("Russian")){
                dragomanDBHelper.insertTranslatedRussian(arrayList.get(index),spinnerLabel,textTranslation);
            }
            index++;

        }
    }
}
