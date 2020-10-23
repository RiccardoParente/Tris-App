package com.riccardo_parente.tris.databaseEngine;

public enum DatabaseStrings
{
    COMPUTER_WAR("Computer_war"),
    SINGLE_PLAYER("Single_player"),
    MULTIPLAYER("Multiplayer"),
    VITTORIE("Vittorie"),
    PAREGGI("Pareggi"),
    SCONFITTE("Sconfitte"),
    GIOCATORE("Giocatore"),
    GIOCATORE1("Giocatore1"),
    GIOCATORE2("Giocatore2"),
    VITTORIE1("Vittorie1"),
    VITTORIE2("Vittorie2"),
    FABRIZIO("Fabrizio"),
    NICOLA("Nicola");
    
    String desc;
    
    DatabaseStrings(String desc)
    {
        this.desc = desc;
    }
    
    public String getDesc()
    {
        return desc;
    }
}
