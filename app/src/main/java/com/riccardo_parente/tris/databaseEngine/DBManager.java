package com.riccardo_parente.tris.databaseEngine;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import static com.riccardo_parente.tris.databaseEngine.DatabaseStrings.*;

public class DBManager
{
    private DBHelper dbHelper;
    SQLiteDatabase db;
    
    public DBManager(Context context)
    {
        dbHelper = new DBHelper(context);
    }
    
    public void reset(DatabaseStrings table, String... nomi)
    {
        db = dbHelper.getWritableDatabase();
        switch (table)
        {
            case COMPUTER_WAR:
                db.execSQL("UPDATE "+COMPUTER_WAR.getDesc()+" SET "+FABRIZIO.getDesc()+"=0, "+PAREGGI.getDesc()+"=0, "+NICOLA.getDesc()+"=0");
                break;
            case SINGLE_PLAYER:
                db.execSQL("UPDATE "+SINGLE_PLAYER.getDesc()+" SET "+VITTORIE.getDesc()+"=0, "+PAREGGI.getDesc()+"=0, "+SCONFITTE.getDesc()+"=0 WHERE "+GIOCATORE.getDesc()+"=\'"+nomi[0]+"\'");
                break;
            case MULTIPLAYER:
                db.execSQL("UPDATE "+MULTIPLAYER.getDesc()+" SET "+VITTORIE1.getDesc()+"=0, "+PAREGGI.getDesc()+"=0, "+VITTORIE2.getDesc()+"=0 WHERE "+GIOCATORE1.getDesc()+"=\'"+nomi[0]+"\' AND "+GIOCATORE2.getDesc()+"=\'"+nomi[1]+"\'");
                break;
        }
    }
    
    public void update(DatabaseStrings table, DatabaseStrings result, String... nomi)
    {
        db = dbHelper.getWritableDatabase();
        switch (table)
        {
            case COMPUTER_WAR:
                db.execSQL("UPDATE "+COMPUTER_WAR.getDesc()+" SET "+result.getDesc()+"="+result.getDesc()+"+1");
                break;
            case SINGLE_PLAYER:
                db.execSQL("UPDATE "+SINGLE_PLAYER.getDesc()+" SET "+result.getDesc()+"="+result.getDesc()+"+1 WHERE "+GIOCATORE.getDesc()+"=\'"+nomi[0]+"\'");
                break;
            case MULTIPLAYER:
                db.execSQL("UPDATE "+MULTIPLAYER.getDesc()+" SET "+result.getDesc()+"="+result.getDesc()+"+1 WHERE "+GIOCATORE1.getDesc()+"=\'"+nomi[0]+"\' AND "+GIOCATORE2.getDesc()+"=\'"+nomi[1]+"\'");
                break;
        }
    }
    
    public void insert(DatabaseStrings table, String... nomi)
    {
        db = dbHelper.getWritableDatabase();
        switch (table)
        {
            case COMPUTER_WAR:
                db.execSQL("INSERT INTO "+COMPUTER_WAR.getDesc()+" VALUES (0,0,0)");
                break;
            case SINGLE_PLAYER:
                db.execSQL("INSERT INTO "+SINGLE_PLAYER.getDesc()+" VALUES (\'"+nomi[0]+"\',0,0,0)");
                break;
            case MULTIPLAYER:
                db.execSQL("INSERT INTO "+MULTIPLAYER.getDesc()+" VALUES (\'"+nomi[0]+"\',\'"+nomi[1]+"\',0,0,0)");
                break;
        }
    }
    
    public Cursor read(DatabaseStrings table, String... nomi)
    {
        db = dbHelper.getReadableDatabase();
        switch (table)
        {
            case COMPUTER_WAR:
                return db.rawQuery("SELECT * FROM "+COMPUTER_WAR.getDesc(), null);
            case SINGLE_PLAYER:
                return db.rawQuery("SELECT * FROM "+SINGLE_PLAYER.getDesc()+" WHERE "+GIOCATORE.getDesc()+"=\'"+nomi[0]+"\'", null);
            case MULTIPLAYER:
                return db.rawQuery("SELECT * FROM "+MULTIPLAYER.getDesc()+" WHERE "+GIOCATORE1.getDesc()+"=\'"+nomi[0]+"\' AND "+GIOCATORE2.getDesc()+"=\'"+nomi[1]+"\'", null);
        }
        return null;
    }
}
