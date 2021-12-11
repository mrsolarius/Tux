/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.litopia.game.model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author zaettal
 */
public class Partie {
    private String date;
    private String mot;
    //private Profil profil;
    private int niveau;
    private int trouve;
    private int temps;
    //private int score;
    //private Dico dico;

    /*public Partie(int niveau, Profil profil) {
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
    }*/
    
    public Partie(String date, String mot, int niveau) {
        this.date = date;
        this.mot = mot;
        this.niveau = niveau;
    }
    
    public Partie(Element partieElt) {
        this.date = partieElt.getAttribute("date");
        try{
            // On enlève le % afin que le parseInt puisse fonctionner correctement
            String trouveSansLastChar = partieElt.getAttribute("trouvé").replace("%", "");
            this.trouve = Integer.parseInt(trouveSansLastChar);
        }
        catch (NumberFormatException ex){
            //ex.printStackTrace();
            this.trouve = 100;
        }
        try{
            this.temps = Integer.parseInt(partieElt.getElementsByTagName("temps").item(0).getTextContent());
        }
        catch (NumberFormatException ex){
            //si le temps n'est pas un int valide, c'est qu'il est soit invalide, soit rentré sous forme de double
            try {
                // on vérifie s'il est rentré sous forme de double et on le convertit en int
                this.temps = (int) Double.parseDouble(partieElt.getElementsByTagName("temps").item(0).getTextContent());
            }
            catch (NumberFormatException ex2){
                // sinon c'est que le nombre est rentré sous un format invalide, on le laisse donc par défaut à 0
            }
        }
        catch (NullPointerException nu) {
            //s'il n'y a pas de temps, on ne modifie pas sa valeur
        }
        this.mot = partieElt.getElementsByTagName("mot").item(0).getTextContent();
        try{
            this.niveau = Integer.parseInt(partieElt.getElementsByTagName("mot").item(0).getAttributes().item(0).getTextContent());
        }
        catch (NumberFormatException ex){
            ex.printStackTrace();
        }
        
    }
    
    public Element getPartie(Document doc) {
        Element nouvellePartie = doc.createElement("partie");
        nouvellePartie.setAttribute("date", date);
        // si le joueur n'a pas tout trouvé, alors on affiche son % de lettres trouvées
        if(trouve!=100) {
            nouvellePartie.setAttribute("trouvé", String.valueOf(trouve)+"%");
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
        doc.getElementsByTagName("parties").item(0).appendChild(nouvellePartie);
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

    /*public String getMot() {
        return mot;
    }*/

    @Override
    public String toString(){
        return "Date : "+date+" Mot : "+mot+" Niveau : "+niveau+" Trouve : "+trouve+" Temps : "+temps;
    }

    /*public void setScore(int score) {
        this.score=score;
    }*/
}
