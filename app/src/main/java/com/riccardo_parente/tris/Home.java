package com.riccardo_parente.tris;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;

import androidx.appcompat.app.AppCompatActivity;

import com.riccardo_parente.tris.databaseEngine.DBManager;

public class Home extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener, DialogListener
{
    DBManager dbManager;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        dbManager = new DBManager(getApplicationContext());
    }
    
    public void showSettings(View view)
    {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.settings_popup);
        popupMenu.show();
    }
    
    @Override
    public boolean onMenuItemClick(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.stats:
                startActivity(new Intent(this, Statistiche.class));
                return true;
            case R.id.regole:
                startActivity(new Intent(this, Regole.class));
                return true;
            default:
                return false;
        }
    }
    public void goToGame(View view)
    {
        switch (((Button) view).getId())
        {
            case R.id._1p:
                DataPopupSingleDialogFragment popupS = new DataPopupSingleDialogFragment();
                popupS.show(getSupportFragmentManager(), null);
                break;
            case R.id._2p:
                DataPopupMultiDialogFragment popupM = new DataPopupMultiDialogFragment();
                popupM.show(getSupportFragmentManager(),null);
                break;
            case R.id.comp:
                startActivity(new Intent(this, ComputerGame.class));
                break;
        }
    }
    
    
    @Override
    public void onDialogPositiveClick(String... nomi)
    {
        if (nomi.length==1)
        {
            Intent i = new Intent(this, OnePlayerGame.class);
            i.putExtra("nome", nomi[0]);
            startActivity(i);
        }
        else
        {
            Intent i = new Intent(this, TwoPlayersGame.class);
            i.putExtra("nome1", nomi[0]);
            i.putExtra("nome2", nomi[1]);
            startActivity(i);
        }
    }
}