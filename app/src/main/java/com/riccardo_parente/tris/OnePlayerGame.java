package com.riccardo_parente.tris;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.riccardo_parente.tris.databaseEngine.DBManager;
import com.riccardo_parente.tris.gameEngine.AvversarioTris;
import com.riccardo_parente.tris.gameEngine.GiocatoreTris;
import com.riccardo_parente.tris.gameEngine.GiocoTris;
import com.riccardo_parente.tris.gameEngine.ScacchieraTris;

import java.util.Arrays;

import static com.riccardo_parente.tris.databaseEngine.DatabaseStrings.PAREGGI;
import static com.riccardo_parente.tris.databaseEngine.DatabaseStrings.SCONFITTE;
import static com.riccardo_parente.tris.databaseEngine.DatabaseStrings.SINGLE_PLAYER;
import static com.riccardo_parente.tris.databaseEngine.DatabaseStrings.VITTORIE;

public class OnePlayerGame extends AppCompatActivity
{
    private final GiocoTris game = new GiocoTris();
    private int segnoGioc;
    private int segnoComp;
    private TextView turno;
    private boolean fine = false;
    private ImageButton[][] caselle = new ImageButton[3][3];
    private GiocatoreTris player;
    private AvversarioTris computer;
    private ScacchieraTris scacchiera;
    private Handler handler = new Handler();
    private String nomeGioc;
    private DBManager dbManager;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);
        dbManager = new DBManager(this);
        Intent i = getIntent();
        nomeGioc = i.getStringExtra("nome");
        if (!dbManager.read(SINGLE_PLAYER, nomeGioc).moveToFirst())
            dbManager.insert(SINGLE_PLAYER, nomeGioc);
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
        game.resetMosse();
        turno.setTypeface(null, Typeface.NORMAL);
        abilita(false);
        scacchiera = new ScacchieraTris();
        int iniziaGioc = (int)(Math.random()*2);
        if (iniziaGioc==1)
        {
            player = new GiocatoreTris('X');
            computer = new AvversarioTris('O', game);
            segnoGioc = R.drawable.tris_x;
            segnoComp = R.drawable.tris_o;
            turno.setText("È il tuo turno");
            abilita(true);
        }
        else
        {
            player = new GiocatoreTris('O');
            computer = new AvversarioTris('X', game);
            segnoGioc = R.drawable.tris_o;
            segnoComp = R.drawable.tris_x;
            handler.postDelayed(this::turnoComp, 300);
        }
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
        {
            for (int j = 0; j < 3; j++)
            {
                String contDesc = (String) caselle[i][j].getContentDescription();
                if (!contDesc.equals("X") && !contDesc.equals("O"))
                {
                    caselleLibere[cont] = ""+i+j;
                    cont++;
                }
            }
        }
        caselleLibere = Arrays.copyOf(caselleLibere, cont);
        return caselleLibere;
    }
    
    private void abilita(boolean abilita)
    {
        String[] caselleLibere = caselleLibere();
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
            ImageButton button = (ImageButton) view;
            String contentD = (String) button.getContentDescription();
            if (contentD.equals("X") || contentD.equals("O"))
                return;
            button.setImageResource(segnoGioc);
            button.setClickable(false);
            game.addMossa();
            if (game.getMosse()==9)
            {
                fine = true;
                dbManager.update(SINGLE_PLAYER, PAREGGI, nomeGioc);
                turno.setTypeface(null, Typeface.ITALIC);
                turno.setText("Pareggio!\nL'unica mossa vincente è non giocare.");
            }
            int h = Integer.parseInt(String.valueOf(button.getContentDescription().charAt(0)));
            int w = Integer.parseInt(String.valueOf(button.getContentDescription().charAt(1)));
            scacchiera.occupa(h,w,player.getSegno());
            button.setContentDescription(String.valueOf(player.getSegno()));
            if (game.controlloVittoria(player, scacchiera)==1)
            {
                fine = true;
                dbManager.update(SINGLE_PLAYER, VITTORIE, nomeGioc);
                turno.setTypeface(null, Typeface.ITALIC);
                turno.setText("Hai vinto!");
            }
            if (!fine)
            {
                turno.setText("È il turno del computer");
                handler.postDelayed(this::turnoComp, 300);
            }
        }
    }
    
    private void turnoComp()
    {
        abilita(false);
        int[] pos = computer.turno(scacchiera);
        ImageButton button =  caselle[pos[0]][pos[1]];
        button.setImageResource(segnoComp);
        button.setClickable(false);
        button.setContentDescription(String.valueOf(computer.getSegno()));
        game.addMossa();
        if (game.getMosse()==9)
        {
            fine = true;
            dbManager.update(SINGLE_PLAYER, PAREGGI, nomeGioc);
            turno.setTypeface(null, Typeface.ITALIC);
            turno.setText("Pareggio!\nL'unica mossa vincente è non giocare.");
        }
        else if (game.controlloVittoria(player, scacchiera)==2)
        {
            fine = true;
            dbManager.update(SINGLE_PLAYER, SCONFITTE, nomeGioc);
            turno.setTypeface(null, Typeface.ITALIC);
            turno.setText("Hai perso!");
        }
        else
        {
            turno.setText("È il turno del giocatore");
            abilita(true);
        }
    }
}