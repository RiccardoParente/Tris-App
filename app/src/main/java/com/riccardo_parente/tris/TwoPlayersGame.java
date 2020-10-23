package com.riccardo_parente.tris;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.riccardo_parente.tris.databaseEngine.DBManager;
import com.riccardo_parente.tris.gameEngine.GiocatoreTris;
import com.riccardo_parente.tris.gameEngine.GiocoTris;
import com.riccardo_parente.tris.gameEngine.ScacchieraTris;

import java.util.Arrays;

import static com.riccardo_parente.tris.databaseEngine.DatabaseStrings.MULTIPLAYER;
import static com.riccardo_parente.tris.databaseEngine.DatabaseStrings.PAREGGI;
import static com.riccardo_parente.tris.databaseEngine.DatabaseStrings.VITTORIE1;
import static com.riccardo_parente.tris.databaseEngine.DatabaseStrings.VITTORIE2;

public class TwoPlayersGame extends AppCompatActivity
{
    private final GiocoTris game = new GiocoTris();
    private int segnoGioc1;
    private int segnoGioc2;
    private TextView turno;
    private boolean fine = false;
    private ImageButton[][] caselle = new ImageButton[3][3];
    private int mosse = 0;
    private GiocatoreTris player1;
    private GiocatoreTris player2;
    private ScacchieraTris scacchiera;
    private boolean turno1 = true;
    private String nomeGioc1;
    private String nomeGioc2;
    private DBManager dbManager;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);
        Intent i = getIntent();
        dbManager = new DBManager(this);
        nomeGioc1 = i.getStringExtra("nome1");
        nomeGioc2 = i.getStringExtra("nome2");
        if (!dbManager.read(MULTIPLAYER, nomeGioc1, nomeGioc2).moveToFirst())
            dbManager.insert(MULTIPLAYER, nomeGioc1, nomeGioc2);
        turno = (TextView) findViewById(R.id.turno);
        Button back = findViewById(R.id.back_button);
        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
        start();
    }
    
    public void start(View view)
    {
        start();
    }
    
    private void start()
    {
        inizializzaCaselle();
        fine = false;
        mosse = 0;
        turno.setTypeface(null, Typeface.NORMAL);
        abilita(false);
        scacchiera = new ScacchieraTris();
        int iniziaGioc = (int)(Math.random()*2);
        if (iniziaGioc==1)
        {
            player1 = new GiocatoreTris('X');
            player2 = new GiocatoreTris('O');
            segnoGioc1 = R.drawable.tris_x;
            segnoGioc2 = R.drawable.tris_o;
            turno.setText("È il turno di "+nomeGioc1);
        }
        else
        {
            player1 = new GiocatoreTris('O');
            player2 = new GiocatoreTris('X');
            segnoGioc1 = R.drawable.tris_o;
            segnoGioc2 = R.drawable.tris_x;
            turno1 = false;
            turno.setText("È il turno di "+nomeGioc2);
        }
        abilita(true);
    }
    
    private void inizializzaCaselle()
    {
        for (int i=0; i<3; i++)
            for (int j=0; j<3; j++)
            {
                int id = getResources().getIdentifier("button"+i+j,"id",getPackageName());
                ImageButton button = (ImageButton) findViewById(id);
                button.setImageResource(android.R.color.transparent);
                button.setContentDescription(""+i+j);
                button.setOnClickListener(this::muovi);
                caselle[i][j] = button;
            }
    }
    
    private String[] caselleLibere()
    {
        String[] caselleLibere = new String[9];
        int cont = 0;
        for (int i=0; i<3; i++)
            for (int j = 0; j < 3; j++)
            {
                String contDesc = (String) caselle[i][j].getContentDescription();
                if (!contDesc.equals("X") && !contDesc.equals("O"))
                {
                    caselleLibere[cont] = "" + i + j;
                    cont++;
                }
            }
        caselleLibere = Arrays.copyOf(caselleLibere, cont);
        return caselleLibere;
    }
    
    private void abilita(boolean abilita)
    {
        String[] caselleLibere = caselleLibere();
        Arrays.stream(caselleLibere).forEach(System.out::println);
        for (int i=0; i<caselleLibere.length; i++)
        {
            String c = caselleLibere[i];
            int h = Integer.parseInt(String.valueOf(c.charAt(0)));
            int w = Integer.parseInt(String.valueOf(c.charAt(1)));
            caselle[h][w].setClickable(abilita);
        }
    }
    
    public void muovi(View view)
    {
        if (!fine)
        {
            abilita(false);
            GiocatoreTris giocatore;
            int segnoGioc;
            if (turno1)
            {
                giocatore = player1;
                segnoGioc = segnoGioc1;
            }
            else
            {
                giocatore = player2;
                segnoGioc = segnoGioc2;
            }
            ImageButton button = (ImageButton) view;
            button.setImageResource(segnoGioc);
            button.setClickable(false);
            mosse++;
            if (mosse == 9)
            {
                fine = true;
                dbManager.update(MULTIPLAYER, PAREGGI, nomeGioc1, nomeGioc2);
                turno.setTypeface(null, Typeface.ITALIC);
                turno.setText("Pareggio!\nL'unica mossa vincente è non giocare.");
            }
            int h = Integer.parseInt(String.valueOf(button.getContentDescription().charAt(0)));
            int w = Integer.parseInt(String.valueOf(button.getContentDescription().charAt(1)));
            scacchiera.occupa(h, w, giocatore.getSegno());
            button.setContentDescription(String.valueOf(giocatore.getSegno()));
            if (game.controlloVittoria(giocatore, scacchiera) == 1)
            {
                fine = true;
                turno.setTypeface(null, Typeface.ITALIC);
                if (giocatore.getSegno()=='X')
                {
                    dbManager.update(MULTIPLAYER, VITTORIE1, nomeGioc1, nomeGioc2);
                    turno.setText("Ha vinto "+nomeGioc1+"!");
                }
                else
                {
                    dbManager.update(MULTIPLAYER, VITTORIE2, nomeGioc1, nomeGioc2);
                    turno.setText("Ha vinto "+nomeGioc2+"!");
                }
            }
            else if (!fine)
            {
                abilita(true);
                if (turno1)
                {
                    turno1 = false;
                    turno.setText("È il turno di "+nomeGioc2);
                }
                else
                {
                    turno1 = true;
                    turno.setText("È il turno di "+nomeGioc1);
                }
            }
        }
    }
}