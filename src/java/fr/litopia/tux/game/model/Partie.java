/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.litopia.tux.game.model;

import fr.litopia.tux.game.utils.XMLUtil;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author zaettal
 */
public class Partie {
    private final String date;
    private final String mot;
    private int niveau;
    private int trouve;
    private float temps;
    private int score;
    
    public Partie(String date, String mot, int niveau) {
        this.date = date;
        this.mot = mot;
        this.niveau = niveau;
    }
    
    public Partie(Element partieElt) {
        this.date = XMLUtil.xmlDateToProfileDate(partieElt.getAttribute("date"));
        try{
            if(partieElt.getElementsByTagName("trouvé").getLength()>0)
                this.trouve = Integer.parseInt(partieElt.getAttribute("trouvé").replace("%", ""));
            if(partieElt.getElementsByTagName("temps").getLength()>0)
                this.temps = Float.parseFloat(partieElt.getElementsByTagName("temps").item(0).getTextContent());
            this.score = Integer.parseInt(partieElt.getElementsByTagName("score").item(0).getTextContent());
            this.niveau = Integer.parseInt(partieElt.getElementsByTagName("mot").item(0).getAttributes().item(0).getTextContent());
        }
        catch (NumberFormatException|NullPointerException ex ){
            ex.printStackTrace();
        }
        this.mot = partieElt.getElementsByTagName("mot").item(0).getTextContent();
    }
    
    public Element getPartie(Document doc) {
        Element nouvellePartie = doc.createElement("partie");
        nouvellePartie.setAttribute("date", XMLUtil.profileDateToXmlDate(date));
        // si le joueur n'a pas tout trouvé, alors on affiche son % de lettres trouvées
        if(trouve!=100) {
            nouvellePartie.setAttribute("trouvé", trouve +"%");
        }
        // Sinon, on ajoute un element temps qui indiquera en cb de temps le mot a été trouvé
        else 
        {
            Element temps = doc.createElement("temps");
            temps.setTextContent(String.valueOf(this.temps));
            nouvellePartie.appendChild(temps);
        }
        Element mot = doc.createElement("mot");
        mot.setAttribute("niveau", String.valueOf(niveau));
        mot.setTextContent(this.mot);
        nouvellePartie.appendChild(mot);

        Element score = doc.createElement("score");
        score.setTextContent(String.valueOf(this.score));
        nouvellePartie.appendChild(score);

        //doc.getElementsByTagName("parties").item(0).appendChild(nouvellePartie);
        return nouvellePartie;
    }

    public void setTrouve(int nbLettresRestantes) {
        int tailleMot = mot.length();
        double ratio = (double) (tailleMot-nbLettresRestantes)/tailleMot;
        this.trouve = (int) (ratio * 100);
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

    public int getMaxTime(){
        return (20 + (mot.length() * 8 + niveau * 10));
    }

    public String getMot() {
        return mot;
    }

    public void setScore(int score) {
        this.score=score;
    }

    public int getScore() {
        return score;
    }

    public int getTrouve() {
        return trouve;
    }

    public float getTemps() {
        return temps;
    }
}
