package com.riccardo_parente.tris;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.riccardo_parente.tris.databaseEngine.DBManager;
import com.riccardo_parente.tris.databaseEngine.DatabaseStrings;

public class Statistiche extends AppCompatActivity
{
    private Spinner spinner;
    private DBManager dbManager;
    private Cursor cursor;
    private TextView vittorie, pareggi, sconfitte, nvittorie, npareggi, nsconfitte, title;
    private EditText nome, nome1, nome2;
    private Button cerca;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        dbManager = new DBManager(this);
        vittorie = (TextView) findViewById(R.id.vittorie);
        pareggi = (TextView) findViewById(R.id.pareggi);
        sconfitte = (TextView) findViewById(R.id.sconfitte);
        nvittorie = (TextView) findViewById(R.id.nvittorie);
        npareggi = (TextView) findViewById(R.id.npareggi);
        nsconfitte = (TextView) findViewById(R.id.nsconfitte);
        title = (TextView) findViewById(R.id.titolo_stat);
        nome = (EditText) findViewById(R.id.nome);
        nome1 = (EditText) findViewById(R.id.nome1);
        nome2 = (EditText) findViewById(R.id.nome2);
        cerca = (Button) findViewById(R.id.cerca);
        spinner = (Spinner) findViewById(R.id.spinner);
        Button back = findViewById(R.id.back_button);
        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.stat_choice, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                {
                    switch (position)
                    {
                        case 0:
                            cerca.setEnabled(true);
                            abilita(nome, true);
                            abilita(nome1, false);
                            abilita(nome2, false);
                            title.setText("1 Player");
                            break;
                        case 1:
                            cerca.setEnabled(true);
                            abilita(nome, false);
                            abilita(nome1, true);
                            abilita(nome2, true);
                            title.setText("2 Players");
                            break;
                        case 2:
                            cerca.setEnabled(false);
                            abilita(nome, false);
                            abilita(nome1, false);
                            abilita(nome2, false);
                            title.setText("Computer War");
                            vittorie.setText("Fabrizio:");
                            pareggi.setText("Pareggi:");
                            sconfitte.setText("Nicola:");
                            cursor = dbManager.read(DatabaseStrings.COMPUTER_WAR);
                            if (cursor!= null && cursor.moveToFirst())
                            {
                                int v = cursor.getInt(cursor.getColumnIndex(DatabaseStrings.FABRIZIO.getDesc()));
                                int p = cursor.getInt(cursor.getColumnIndex(DatabaseStrings.PAREGGI.getDesc()));
                                int s = cursor.getInt(cursor.getColumnIndex(DatabaseStrings.NICOLA.getDesc()));
                                nvittorie.setText(""+v);
                                npareggi.setText(""+p);
                                nsconfitte.setText(""+s);
                            }
                            break;
                    }
                }
        
                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
                
                public void abilita(EditText text, boolean a)
                {
                    text.setEnabled(a);
                    text.setText("");
                    vittorie.setText("");
                    pareggi.setText("");
                    sconfitte.setText("");
                    nvittorie.setText("");
                    npareggi.setText("");
                    nsconfitte.setText("");
                    if (a)
                        text.setVisibility(View.VISIBLE);
                    else
                        text.setVisibility(View.INVISIBLE);
                }
        });
    }
    
    public void reset(View view)
    {
        switch ((int)spinner.getSelectedItemId())
        {
            case 0:
                if (!nome.getEditableText().toString().equals(""))
                    dbManager.reset(DatabaseStrings.SINGLE_PLAYER, nome.getEditableText().toString());
                break;
            case 1:
                if (!nome1.getEditableText().toString().equals("") && !nome2.getEditableText().toString().equals(""))
                    dbManager.reset(DatabaseStrings.MULTIPLAYER, nome1.getEditableText().toString(), nome2.getEditableText().toString());
                break;
            case 2:
                dbManager.reset(DatabaseStrings.COMPUTER_WAR);
                cursor = dbManager.read(DatabaseStrings.COMPUTER_WAR);
                if (cursor!= null && cursor.moveToFirst())
                {
                    int v = cursor.getInt(cursor.getColumnIndex(DatabaseStrings.FABRIZIO.getDesc()));
                    int p = cursor.getInt(cursor.getColumnIndex(DatabaseStrings.PAREGGI.getDesc()));
                    int s = cursor.getInt(cursor.getColumnIndex(DatabaseStrings.NICOLA.getDesc()));
                    nvittorie.setText(""+v);
                    npareggi.setText(""+p);
                    nsconfitte.setText(""+s);
                }
                cerca.setEnabled(false);
                break;
        }
        cerca(cerca);
    }
    
    public void cerca(View view)
    {
        int v = 0;
        int p = 0;
        int s = 0;
        switch ((int)spinner.getSelectedItemId())
        {
            case 0:
                String n = nome.getEditableText().toString();
                cursor = dbManager.read(DatabaseStrings.SINGLE_PLAYER, n);
                if (cursor!= null && cursor.moveToFirst())
                {
                    vittorie.setText(n+":");
                    pareggi.setText("Pareggi:");
                    sconfitte.setText("Computer:");
                    v = cursor.getInt(cursor.getColumnIndex(DatabaseStrings.VITTORIE.getDesc()));
                    p = cursor.getInt(cursor.getColumnIndex(DatabaseStrings.PAREGGI.getDesc()));
                    s = cursor.getInt(cursor.getColumnIndex(DatabaseStrings.SCONFITTE.getDesc()));
                    nvittorie.setText(""+v);
                    npareggi.setText(""+p);
                    nsconfitte.setText(""+s);
                }
                break;
            case 1:
                String n1 = nome1.getEditableText().toString();
                String n2 = nome2.getEditableText().toString();
                cursor = dbManager.read(DatabaseStrings.MULTIPLAYER, n1, n2);
                if (cursor!= null && cursor.moveToFirst())
                {
                    vittorie.setText(n1+":");
                    pareggi.setText("Pareggi:");
                    sconfitte.setText(n2+":");
                    v = cursor.getInt(cursor.getColumnIndex(DatabaseStrings.VITTORIE1.getDesc()));
                    p = cursor.getInt(cursor.getColumnIndex(DatabaseStrings.PAREGGI.getDesc()));
                    s = cursor.getInt(cursor.getColumnIndex(DatabaseStrings.VITTORIE2.getDesc()));
                    nvittorie.setText(""+v);
                    npareggi.setText(""+p);
                    nsconfitte.setText(""+s);
                }
                break;
        }
    }
}