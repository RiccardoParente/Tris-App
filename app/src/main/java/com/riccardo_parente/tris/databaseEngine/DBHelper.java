package com.riccardo_parente.tris.databaseEngine;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.riccardo_parente.tris.databaseEngine.DatabaseStrings.*;

public class DBHelper extends SQLiteOpenHelper
{
    private static final String DBNAME = "DB_STATISTICHE";
    
    public DBHelper(Context context)
    {
        super(context, DBNAME, null, 1);
    }
    
    public void onCreate(SQLiteDatabase db)
    {
        String comandi = "CREATE TABLE "+COMPUTER_WAR.getDesc()+ " ( "+
                FABRIZIO.getDesc()+" int NOT NULL DEFAULT 0, " +
                PAREGGI.getDesc()+" int NOT NULL DEFAULT 0, "+
                NICOLA.getDesc()+" int NOT NULL DEFAULT 0)";
        db.execSQL(comandi);
        comandi = "CREATE TABLE "+SINGLE_PLAYER.getDesc()+" ( "+
                GIOCATORE.getDesc()+" varchar(255) NOT NULL, "+
                VITTORIE.getDesc()+" int NOT NULL DEFAULT 0, "+
                PAREGGI.getDesc()+" int NOT NULL DEFAULT 0, "+
                SCONFITTE.getDesc()+" int NOT NULL DEFAULT 0 )";
        db.execSQL(comandi);
        comandi = "CREATE TABLE "+MULTIPLAYER.getDesc()+" ( "+
                GIOCATORE1.getDesc()+" varchar(255) NOT NULL, "+
                GIOCATORE2.getDesc()+" varchar(255) NOT NULL, "+
                VITTORIE1.getDesc()+" int NOT NULL DEFAULT 0, "+
                PAREGGI.getDesc()+" int NOT NULL DEFAULT 0, "+
                VITTORIE2.getDesc()+" int NOT NULL DEFAULT 0 )";
        db.execSQL(comandi);
        comandi = "INSERT INTO "+COMPUTER_WAR.getDesc()+" VALUES (0,0,0)";
        db.execSQL(comandi);
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
}
