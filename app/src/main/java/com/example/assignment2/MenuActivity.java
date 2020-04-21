package com.example.assignment2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

    }

    //To launch add activity
    public void launchAddPhrase(View view){
        Intent intent = new Intent(this,AddPhraseActivity.class);
        startActivity(intent);
    }

    //To launch display activity
    public void launchDisplayPhrase(View view){
        Intent intent = new Intent(this,DisplayPhrasesActivity.class);
        startActivity(intent);
    }

    //To launch edit activity
    public void launchEditPhrase(View view){
        Intent intent = new Intent(this,EditPhrasesActivity.class);
        startActivity(intent);
    }

    //To launch subscription activity
    public void launchSubscription(View view){
        Intent intent = new Intent(this,SubscriptionActivity.class);
        startActivity(intent);
    }

    //To launch translate activity
    public void launchTranslate(View view){
        Intent intent = new Intent(this,TranslateActivity.class);
        startActivity(intent);
    }

    //To launch translate all activity
    public void launchTranslateAll(View view){
        Intent intent = new Intent(this,TranslateAllActivity.class);
        startActivity(intent);
    }

    //To exit the app
    public void ExitTheApp(View view) {
        finish();
        System.exit(0);
    }
}
