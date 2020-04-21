package com.example.assignment2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DragomanDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Dragoman.db";
    private static final String TABLE_NAME1 = "word_table";
    private static final String TABLE_NAME2 = "language_table";
    private static final String TABLE_NAME3 = "translated_german_table";
    private static final String TABLE_NAME4 = "translated_italian_table";
    private static final String TABLE_NAME5 = "translated_dutch_table";
    private static final String TABLE_NAME6 = "translated_portuguese_table";
    private static final String TABLE_NAME7 = "translated_russian_table";

    private static final String TABLE1_COLUMN_1 = "ID";
    private static final String TABLE1_COLUMN_2 = "WORD";

    private static final String TABLE2_COLUMN_1 = "ID";
    private static final String TABLE2_COLUMN_2 = "LANGUAGE";
    private static final String TABLE2_COLUMN_3 = "CODE";
    private static final String TABLE2_COLUMN_4 = "CHECKED";

    private static final String TABLE3_COLUMN_1 = "WORD";
    private static final String TABLE3_COLUMN_2 = "German";

    private static final String TABLE4_COLUMN_1 = "WORD";
    private static final String TABLE4_COLUMN_2 = "Italian";

    private static final String TABLE5_COLUMN_1 = "WORD";
    private static final String TABLE5_COLUMN_2 = "Dutch";

    private static final String TABLE6_COLUMN_1 = "WORD";
    private static final String TABLE6_COLUMN_2 = "Portuguese";

    private static final String TABLE7_COLUMN_1 = "WORD";
    private static final String TABLE7_COLUMN_2 = "Russian";


    public DragomanDBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME1 + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,WORD TEXT) ");
        db.execSQL("create table " + TABLE_NAME2 + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,LANGUAGE TEXT,CODE TEXT,CHECKED INTEGER) ");
        db.execSQL("create table " + TABLE_NAME3 + " (WORD TEXT,German TEXT) ");
        db.execSQL("create table " + TABLE_NAME4 + " (WORD TEXT,Italian TEXT) ");
        db.execSQL("create table " + TABLE_NAME5 + " (WORD TEXT,Dutch TEXT) ");
        db.execSQL("create table " + TABLE_NAME6 + " (WORD TEXT,Portuguese TEXT) ");
        db.execSQL("create table " + TABLE_NAME7 + " (WORD TEXT,Russian TEXT) ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    //To insert words into database
    public boolean insertPhrase(String word){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE1_COLUMN_2,word);
        long result = db.insert(TABLE_NAME1,null,contentValues);
        return result != -1;
    }

    //To get all the columns in word table
    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME1 + " order by " + TABLE1_COLUMN_2 + " collate NOCASE asc;" ,null);
        return cursor;
    }

    //To save an edited word
    public void editPhrase(String newPhrase,String currentPhrase){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME1 + " SET " + TABLE1_COLUMN_2 +
                " = '" + newPhrase + "' WHERE " + TABLE1_COLUMN_2 + " = '" + currentPhrase + "'";

        db.execSQL(query);
    }

    //To delete one row from word table
    public Integer deletePhrase(String word){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME1,"WORD = ?",new String[] { word });
    }

    //To insert the available languages
    public boolean insertLanguage(String language, String code, int checked){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE2_COLUMN_2,language);
        contentValues.put(TABLE2_COLUMN_3,code);
        contentValues.put(TABLE2_COLUMN_4,checked);
        long result = db.insert(TABLE_NAME2,null,contentValues);
        return result != -1;
    }

    //To select all the columns in language table
    public Cursor getAllLanguages(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME2,null);
        return cursor;
    }

    //To get all the checked languages from the language table
    public Cursor getAllCheckedLanguages(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME2 + " where " + TABLE2_COLUMN_4 + " = " + " 1 ",null);
        return cursor;
    }

    //To get the code of the language from the table
    public Cursor getLanguageCode(String language){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME2 + " WHERE " + TABLE2_COLUMN_2 + " = '" + language + "'",null);
        return cursor;
    }

    //To change the languages to unchecked
    public void updateCheckedLanguages(int newValue, String language){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME2 + " SET " + TABLE2_COLUMN_4 +
                " = '" + newValue + "' WHERE " + TABLE2_COLUMN_2 + " = '" + language + "'";

        db.execSQL(query);
    }


    //To delete all data in german table
    public void deleteAllGerman(){
        SQLiteDatabase db;
        db = getWritableDatabase();
        db.execSQL("delete from "+ TABLE_NAME3);
    }

    //To delete all data in Italian table
    public void deleteAllItalian(){
        SQLiteDatabase db;
        db = getWritableDatabase();
        db.execSQL("delete from "+ TABLE_NAME4);
    }

    //To delete all data in Dutch table
    public void deleteAllDutch(){
        SQLiteDatabase db;
        db = getWritableDatabase();
        db.execSQL("delete from "+ TABLE_NAME5);
    }

    //To delete all data in Portuguese table
    public void deleteAllPortuguese(){
        SQLiteDatabase db;
        db = getWritableDatabase();
        db.execSQL("delete from "+ TABLE_NAME6);
    }

    //To delete all data in Russian table
    public void deleteAllRussian(){
        SQLiteDatabase db;
        db = getWritableDatabase();
        db.execSQL("delete from "+ TABLE_NAME7);
    }

    //To insert translated german words into database
    public boolean insertTranslatedGerman(String preWord, String language, String word){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE3_COLUMN_1,preWord);
        contentValues.put(language,word);
        long result = db.insert(TABLE_NAME3,null,contentValues);
        return result != -1;
    }

    //To insert translated italian words into database
    public boolean insertTranslatedItalian(String preWord, String language, String word){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE4_COLUMN_1,preWord);
        contentValues.put(language,word);
        long result = db.insert(TABLE_NAME4,null,contentValues);
        return result != -1;
    }

    //To insert translated Dutch words into database
    public boolean insertTranslatedDutch(String preWord, String language, String word){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE5_COLUMN_1,preWord);
        contentValues.put(language,word);
        long result = db.insert(TABLE_NAME5,null,contentValues);
        return result != -1;
    }

    //To insert translated portuguese words into database
    public boolean insertTranslatedPortuguese(String preWord, String language, String word){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE6_COLUMN_1,preWord);
        contentValues.put(language,word);
        long result = db.insert(TABLE_NAME6,null,contentValues);
        return result != -1;
    }

    //To insert translated Russian words into database
    public boolean insertTranslatedRussian(String preWord, String language, String word){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE7_COLUMN_1,preWord);
        contentValues.put(language,word);
        long result = db.insert(TABLE_NAME7,null,contentValues);
        return result != -1;
    }



    //To get all the columns in german table
    public Cursor getAllTranslatedGerman(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME3 + " order by " + TABLE3_COLUMN_1 + " collate NOCASE asc;" ,null);
        return cursor;
    }

    //To get all the columns in italian table
    public Cursor getAllTranslatedItalian(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME4 + " order by " + TABLE4_COLUMN_1 + " collate NOCASE asc;" ,null);
        return cursor;
    }

    //To get all the columns in Dutch table
    public Cursor getAllTranslatedDutch(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME5 + " order by " + TABLE5_COLUMN_1 + " collate NOCASE asc;" ,null);
        return cursor;
    }

    //To get all the columns in Portuguese table
    public Cursor getAllTranslatedPortuguese(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME6 + " order by " + TABLE6_COLUMN_1 + " collate NOCASE asc;" ,null);
        return cursor;
    }

    //To get all the columns in Russian table
    public Cursor getAllTranslatedRussian(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME7 + " order by " + TABLE7_COLUMN_1 + " collate NOCASE asc;" ,null);
        return cursor;
    }
}
