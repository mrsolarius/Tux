/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.litopia.game.model;

import fr.litopia.game.core.LanceurDeJeu;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author zaettal
 */
public class Partie {
    private String date;
    private String mot;
    private Profil profil;
    private int niveau;
    private int trouve;
    private int temps;
    private int score;
    private Dico dico;

    public Partie(int niveau, Profil profil) {
        dico = new Dico("src/res/xml/dico.xml");
        try {
            dico.lireDictionnaireDOM("src/res/xml/", "dico.xml");
        } catch (SAXException ex) {
            Logger.getLogger(LanceurDeJeu.class.getName()).log(Level.parse("Ici"+ Level.SEVERE), null, ex);
        } catch (IOException ex) {
            Logger.getLogger(LanceurDeJeu.class.getName()).log(Level.parse("là"+ Level.SEVERE), null, ex);
        }
        this.profil = profil;
        this.date = DateFormat.getDateTimeInstance().format(new Date());
        this.niveau = niveau;
        this.mot = dico.getMotDepuisListeNiveau(niveau);
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

    public String getMot() {
        return mot;
    }

    @Override
    public String toString(){
        return "Date : "+date+" Mot : "+mot+" Niveau : "+niveau+" Trouve : "+trouve+" Temps : "+temps;
    }

    public void setScore(int score) {
        this.score=score;
    }
}
