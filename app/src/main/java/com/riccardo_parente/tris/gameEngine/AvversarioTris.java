package com.riccardo_parente.tris.gameEngine;

public class AvversarioTris extends GiocatoreTris
{
    private char segnoGiocatore;
    private String casellaBloccante = "";
    private GiocoTris game;
    
    /**
     * Costruttore.
     * @param segno
     */
    public AvversarioTris(char segno, GiocoTris game)
    {
        super(segno);
        if (segno=='X') segnoGiocatore = 'O';
        else segnoGiocatore = 'X';
        this.game = game;
    }
    
    /**
     * Fa una mossa.
     * @param scacchiera ScacchieraTris
     */
    public int[] turno(ScacchieraTris scacchiera)
    {
        char[][] tabella = scacchiera.getScacchiera();
        int[] mossa = new int[2];
        int result = -2;
        int best = -2;
        for (int i=0; i<3; i++)
            for (int j=0; j<3; j++)
            {
                if (tabella[i][j]==' ')
                {
                    tabella[i][j] = segno;
                    game.addMossa();
                    best = minimax(tabella, false);
                    if (best>result)
                    {
                        result = best;
                        mossa[0] = i;
                        mossa[1] = j;
                    }
                    tabella[i][j] = ' ';
                    game.subMossa();
                }
            }
        scacchiera.occupa(mossa[0], mossa[1], segno);
        return mossa;
    }
    
    private int minimax(char[][] scacchiera, boolean isMax)
    {
        int win = game.controlloVittoria(scacchiera, segno);
        if (win!=0 || game.getMosse()==9)
        {
            if (win==1)
                return 1;
            else if (win==2)
                return -1;
            else
                return 0;
        }
        if (isMax)
        {
            int result = -2;
            for (int i=0; i<3; i++)
                for (int j=0; j<3; j++)
                {
                    if (scacchiera[i][j]==' ')
                    {
                        scacchiera[i][j] = segno;
                        game.addMossa();
                        result = Math.max(result, minimax(scacchiera, false));
                        scacchiera[i][j] = ' ';
                        game.subMossa();
                    }
                }
            return result;
        }
        else
        {
            int result = 2;
            for (int i=0; i<3; i++)
                for (int j=0; j<3; j++)
                {
                    if (scacchiera[i][j]==' ')
                    {
                        scacchiera[i][j] = segnoGiocatore;
                        game.addMossa();
                        result = Math.min(result, minimax(scacchiera, true));
                        scacchiera[i][j] = ' ';
                        game.subMossa();
                    }
                }
            return result;
        }
    }
}
