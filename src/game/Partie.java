/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

/**
 *
 * @author zaettal
 */
public class Partie {
    private String date;
    private String mot;
    private int niveau;
    private int trouve;
    private int temps;

    public Partie(String date, String mot, int niveau) {
        this.date = date;
        this.mot = mot;
        this.niveau = niveau;
    }

    public void setTrouve(int trouve) {
        this.trouve = trouve;
    }

    public int getNiveau() {
        return niveau;
    }

    public void setTemps(int temps) {
        this.temps = temps;
    }

    @Override
    public String toString(){
        return "Date : "+date+" Mot : "+mot+" Niveau : "+niveau+" Trouve : "+trouve+" Temps : "+temps;
    }
}
