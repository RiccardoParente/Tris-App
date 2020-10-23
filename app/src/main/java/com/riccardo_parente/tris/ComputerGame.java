package com.riccardo_parente.tris;

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
import com.riccardo_parente.tris.gameEngine.GiocoTris;
import com.riccardo_parente.tris.gameEngine.ScacchieraTris;

import static com.riccardo_parente.tris.databaseEngine.DatabaseStrings.COMPUTER_WAR;
import static com.riccardo_parente.tris.databaseEngine.DatabaseStrings.FABRIZIO;
import static com.riccardo_parente.tris.databaseEngine.DatabaseStrings.NICOLA;
import static com.riccardo_parente.tris.databaseEngine.DatabaseStrings.PAREGGI;

public class ComputerGame extends AppCompatActivity
{
    private final GiocoTris game = new GiocoTris();
    private int segnoFabr;
    private int segnoNic;
    private TextView turno;
    private boolean fine = false;
    private ImageButton[][] caselle = new ImageButton[3][3];
    private AvversarioTris fabrizio;
    private AvversarioTris nicola;
    private ScacchieraTris scacchiera;
    private boolean turnoFabr = true;
    private Handler handler = new Handler();
    private Runnable r = new Runnable()
    {
        @Override
        public void run()
        {
            turno();
        }
    };
    private DBManager dbManager;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);
        dbManager = new DBManager(this);
        Button back = findViewById(R.id.back_button);
        turno = (TextView) findViewById(R.id.turno);
        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                handler.removeCallbacks(r);
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
        handler.removeCallbacks(r);
        scacchiera = new ScacchieraTris();
        int iniziaGioc = (int)(Math.random()*2);
        if (iniziaGioc==1)
        {
            fabrizio = new AvversarioTris('X', game);
            nicola = new AvversarioTris('O', game);
            segnoFabr = R.drawable.tris_x;
            segnoNic = R.drawable.tris_o;
            turno.setText("È il turno di Fabrizio");
        }
        else
        {
            fabrizio = new AvversarioTris('O', game);
            nicola = new AvversarioTris('X', game);
            segnoFabr = R.drawable.tris_o;
            segnoNic = R.drawable.tris_x;
            turnoFabr = false;
            turno.setText("È il turno di Nicola");
        }
        turno();
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
                button.setOnClickListener(null);
                caselle[i][j] = button;
            }
    }
    
    private void turno()
    {
        if (!fine)
        {
            turnoComp();
            handler.postDelayed(r,700);
        }
    }
    
    private void turnoComp()
    {
        AvversarioTris giocatore;
        int segno;
        if (turnoFabr)
        {
            giocatore = fabrizio;
            segno = segnoFabr;
        }
        else
        {
            giocatore = nicola;
            segno = segnoNic;
        }
        int[] pos = giocatore.turno(scacchiera);
        ImageButton button =  caselle[pos[0]][pos[1]];
        button.setImageResource(segno);
        button.setContentDescription(String.valueOf(giocatore.getSegno()));
        game.addMossa();
        if (game.getMosse()==9)
        {
            dbManager.update(COMPUTER_WAR, PAREGGI);
            fine = true;
            turno.setTypeface(null, Typeface.ITALIC);
            turno.setText("Pareggio!\nL'unica mossa vincente è non giocare.");
        }
        else if (game.controlloVittoria(giocatore, scacchiera)==1)
        {
            fine = true;
            turno.setTypeface(null, Typeface.ITALIC);
            if (turnoFabr)
            {
                dbManager.update(COMPUTER_WAR, FABRIZIO);
                turno.setText("Ha vinto Fabrizio!");
            }
            else
            {
                dbManager.update(COMPUTER_WAR, NICOLA);
                turno.setText("Ha vinto Nicola!");
            }
        }
        else
        {
            if (turnoFabr)
            {
                turnoFabr = false;
                turno.setText("È il turno di Nicola");
            }
            else
            {
                turnoFabr = true;
                turno.setText("È il turno di Fabrizio");
            }
        }
    }
}