package com.riccardo_parente.tris.gameEngine;

public class GiocatoreTris
{
    protected char segno;
    
    /**
     * Costruttore.
     * @param segno String, segno con cui gioca
     */
    public GiocatoreTris(char segno)
    {
        this.segno = segno;
    }
    
    /**
     * Ritrona il segno con cui sta giocando il giocatore.
     * @return String
     */
    public char getSegno() {return segno;}
}
