package com.riccardo_parente.tris.gameEngine;

public class GiocoTris
{
    private int mosse;
    
    public void addMossa() { mosse++; }
    
    public void subMossa() { mosse--; }
    
    public int getMosse() { return mosse; }
    
    public void resetMosse() { mosse = 0; }
    
    public int controlloVittoria(GiocatoreTris player, ScacchieraTris griglia)
    {
        char[][] scacchiera = griglia.getScacchiera();
        char segno = player.getSegno();
        return controlloVittoria(scacchiera, segno);
    }
    public int controlloVittoria(char[][] scacchiera, char segno)
    {
        char s = this.controlloOrizzontale(scacchiera);
        if (s!=' ')
        {
            if (s==segno) return 1;
            else return 2;
        }
        s = this.controlloVerticale(scacchiera);
        if (s!=' ')
        {
            if (s==segno) return 1;
            else return 2;
        }
        s = this.controlloDiagonale(scacchiera);
        if (s!=' ')
        {
            if (s==segno) return 1;
            else return 2;
        }
        else return 0;
    }
    
    /**
     * Controlla se c'e' stato un tris orizzontale.
     * @param scacchiera char[][]
     * @return char, segno del vincitore
     */
    public char controlloOrizzontale(char[][] scacchiera)
    {
        for (int h=0;h<3;h++)
        {
            if (scacchiera[h][0]==scacchiera[h][1]&&scacchiera[h][1]==scacchiera[h][2]) return scacchiera[h][0];
        }
        return ' ';
    }
    
    /**
     * Controlla se c'e' stato un tris verticale.
     * @param scacchiera char[][]
     * @return char, segno del vincitore
     */
    public char controlloVerticale(char[][] scacchiera)
    {
        for (int w=0;w<3;w++)
        {
            if (scacchiera[0][w]==scacchiera[1][w]&&scacchiera[1][w]==scacchiera[2][w]) return scacchiera[0][w];
        }
        return ' ';
    }
    
    /**
     * Controlla se c'e' stato un tris diagonale.
     * @param scacchiera char[][]
     * @return char, segno del vincitore
     */
    public char controlloDiagonale(char[][] scacchiera)
    {
        if (scacchiera[0][0]==scacchiera[1][1]&&scacchiera[1][1]==scacchiera[2][2]) return scacchiera[0][0];
        else if (scacchiera[0][2]==scacchiera[1][1]&&scacchiera[1][1]==scacchiera[2][0]) return scacchiera[0][2];
        return ' ';
    }
}
