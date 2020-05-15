package com.cvdariuta305id.Notite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class NotesDataBase extends SQLiteOpenHelper {
    // declare require values
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "dbName";
    private static final String TABLE_NAME = "dbtalbe";

    public NotesDataBase(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_CONTENT = "content";
    private static final String KEY_DATE = "date";
    private static final String KEY_TIME = "time";





    // creating tables
    @Override
    public void onCreate(SQLiteDatabase db) {
         String createDb = "CREATE TABLE "+TABLE_NAME+" ("+
                 KEY_ID+" INTEGER PRIMARY KEY,"+
                 KEY_TITLE+" TEXT,"+
                 KEY_CONTENT+" TEXT,"+
                 KEY_DATE+" TEXT,"+
                 KEY_TIME+" TEXT"
                 +" )";
         db.execSQL(createDb);
    }

    // upgrade db if older version exists
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion >= newVersion)
            return;

        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public long addNote(Notes notes){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(KEY_TITLE, notes.getTitle());
        v.put(KEY_CONTENT, notes.getContent());
        v.put(KEY_DATE, notes.getDate());
        v.put(KEY_TIME, notes.getTime());

        // inserting data into db
        long ID = db.insert(TABLE_NAME,null,v);
        return  ID;
    }

    public Notes getNote(long id){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] query = new String[] {KEY_ID,KEY_TITLE,KEY_CONTENT,KEY_DATE,KEY_TIME};
       Cursor cursor=  db.query(TABLE_NAME,query,KEY_ID+"=?",new String[]{String.valueOf(id)},null,null,null,null);
        if(cursor != null)
            cursor.moveToFirst();

        return new Notes(
                Long.parseLong(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4));
    }

    public List<Notes> getAllNotes(){
        List<Notes> allNotes = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME+" ORDER BY "+KEY_ID+" DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToFirst()){
            do{
                Notes notes = new Notes();
                notes.setId(Long.parseLong(cursor.getString(0)));
                notes.setTitle(cursor.getString(1));
                notes.setContent(cursor.getString(2));
                notes.setDate(cursor.getString(3));
                notes.setTime(cursor.getString(4));
                allNotes.add(notes);
            }while (cursor.moveToNext());
        }

        return allNotes;

    }

    public int editNote(Notes notes){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues c = new ContentValues();

        c.put(KEY_TITLE, notes.getTitle());
        c.put(KEY_CONTENT, notes.getContent());
        c.put(KEY_DATE, notes.getDate());
        c.put(KEY_TIME, notes.getTime());
        return db.update(TABLE_NAME,c,KEY_ID+"=?",new String[]{String.valueOf(notes.getId())});
    }



    void deleteNote(long id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME,KEY_ID+"=?",new String[]{String.valueOf(id)});
        db.close();
    }





}
