package com.example.assignment2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.ibm.cloud.sdk.core.http.HttpMediaType;
import com.ibm.cloud.sdk.core.security.Authenticator;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.cloud.sdk.core.service.exception.NotFoundException;
import com.ibm.watson.developer_cloud.android.library.audio.StreamPlayer;
import com.ibm.watson.language_translator.v3.LanguageTranslator;
import com.ibm.watson.language_translator.v3.model.TranslateOptions;
import com.ibm.watson.language_translator.v3.model.TranslationResult;
import com.ibm.watson.language_translator.v3.util.Language;
import com.ibm.watson.text_to_speech.v1.TextToSpeech;
import com.ibm.watson.text_to_speech.v1.model.SynthesizeOptions;

import java.util.ArrayList;

public class TranslateActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private DragomanDBHelper dragomanDBHelper;
    private ListView listView;
    private String spinnerLabel;
    private TextView textTranslation;
    private LanguageTranslator translationService;
    private StreamPlayer player = new StreamPlayer();
    private TextToSpeech textService;
    private String phrase;
    private String code;
    private int languagePosition;
    Spinner languageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);

        dragomanDBHelper = new DragomanDBHelper(this);
        listView = findViewById(R.id.list_translate);

        addAllCheckedLanguages();
        displayAllPhrases();

        //To get the selected word
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        languagePosition = position;
                        phrase = (String) listView.getItemAtPosition(position);
                        textTranslation.setText("");
                }
            });

        textTranslation = findViewById(R.id.textView_translate);

        translationService = initLanguageTranslatorService();
        textService = initTextToSpeechService();

        if(savedInstanceState != null){
//            listView.setItemChecked(Integer.parseInt(savedInstanceState.getString("position")),true);
//            listView.setSelection(savedInstanceState.getString("position"));
            phrase = savedInstanceState.getString("phrase");
            textTranslation.setText( savedInstanceState.getString("text") ); //setting the saved value to the TextView
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("position", (String) listView.getItemAtPosition(languagePosition));
        outState.putString("phrase",phrase);
        outState.putString("text",textTranslation.getText().toString());
    }

    //To get the code of the language
    public void getLanguageCode(){
        Cursor cursor = dragomanDBHelper.getLanguageCode(spinnerLabel);
        while (cursor.moveToNext()){
            code = cursor.getString(2);
        }
    }

    //To display the words
    public void displayAllPhrases(){
        ArrayList<String> arrayList = new ArrayList<>();

        Cursor cursor = dragomanDBHelper.getAllData();
        if (cursor.getCount() == 0){
            Toast.makeText(TranslateActivity.this,"NO PHRASES AVAILABLE",Toast.LENGTH_LONG).show();
            return;
        }

        while (cursor.moveToNext()){
            arrayList.add(cursor.getString(1));
        }

        ListAdapter listAdapter = new ArrayAdapter<>(this,android.R.layout.simple_selectable_list_item,arrayList);
        listView.setAdapter(listAdapter);
    }

    //To display the checked languages from spinner
    public void addAllCheckedLanguages(){
        ArrayList<String> arrayList = new ArrayList<>();

        Cursor cursor = dragomanDBHelper.getAllCheckedLanguages();
        while (cursor.moveToNext()){
            arrayList.add(cursor.getString(1));
        }

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

    //To translate the word
    public void translate(View view){
        if (phrase == null){
            Toast.makeText(TranslateActivity.this,"SELECT A WORD OR A PHRASE",Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(TranslateActivity.this,"TRANSLATING",Toast.LENGTH_LONG).show();
            getLanguageCode();
            new DragomanTranslationTask().execute(phrase);
        }
    }

    //To get the language translator service from the api
    private LanguageTranslator initLanguageTranslatorService() {
        Authenticator authenticator = new IamAuthenticator("GYfWHFJQyqPfv3z6tb60Wi2pH1sLaBy4Rbsl9thl0AUt");
        LanguageTranslator service = new LanguageTranslator("2018-05-01", authenticator);

        service.setServiceUrl("https://api.us-south.language-translator.watson.cloud.ibm.com/instances/1ff84823-3c6c-4507-bae0-8de12c215b64");
        return service;
    }

    private class DragomanTranslationTask extends AsyncTask<String, Void, String> {

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
            textTranslation.setText(result);
        }
    }

    //To speech the translated word
    public void speech(View view) {
        if (textTranslation.getText().toString().equalsIgnoreCase("LANGUAGE NOT FOUND")){
            Toast.makeText(TranslateActivity.this,"TRANSLATE TO A ANOTHER LANGUAGE",Toast.LENGTH_LONG).show();

        }else if (textTranslation.getText().toString().equalsIgnoreCase("")){
            Toast.makeText(TranslateActivity.this,"TRANSLATE A WORD OR A PHRASE",Toast.LENGTH_LONG).show();

        }else{
            new SynthesisTask().execute(textTranslation.getText().toString());
        }
    }

    //To get the text to speech service from the api
    private TextToSpeech initTextToSpeechService() {
        Authenticator authenticator = new IamAuthenticator("c3OXLFe4uU5SUFyl4jTQfU91SRE-VifpShMlM0eSlX79");
        TextToSpeech service = new TextToSpeech(authenticator);
        service.setServiceUrl("https://api.us-south.text-to-speech.watson.cloud.ibm.com/instances/2b9e15c8-8d5a-4228-92a0-10251061a116");
        return service;
    }

    private class SynthesisTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            SynthesizeOptions synthesizeOptions = new
                    SynthesizeOptions.Builder()
                    .text(params[0])
                    .voice(SynthesizeOptions.Voice.EN_US_LISAVOICE)
                    .accept(HttpMediaType.AUDIO_WAV)
                    .build();
            player.playStream(textService.synthesize(synthesizeOptions).execute()
                    .getResult());
            return "Did synthesize";
        }
    }
}
