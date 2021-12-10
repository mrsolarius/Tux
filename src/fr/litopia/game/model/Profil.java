/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.litopia.game.model;

import java.util.ArrayList;
import org.w3c.dom.Document;
import fr.litopia.game.utils.XMLUtil;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author zaettal
 */
public class Profil {
    private String name;
    private String birthdate;
    private String avatar;
    private ArrayList<Partie> parties;
    public Document _doc;

    public Profil(String name, String birthdate) {
        this.name = name;
        this.birthdate = birthdate;
        /*this.avatar = avatar;
        this.parties = new ArrayList<>();*/
    }
    
    // Cree un DOM à partir d'un fichier XML
    public Profil(String nomFichier) {
        _doc = fromXML(nomFichier);
        // parsing à compléter
        this.name = _doc.getElementsByTagName("nom").item(0).getTextContent();
        this.avatar = _doc.getElementsByTagName("avatar").item(0).getTextContent();
        this.birthdate = xmlDateToProfileDate(_doc.getElementsByTagName("anniversaire").item(0).getTextContent());
        int nbParties = _doc.getElementsByTagName("partie").getLength();
        this.parties = new ArrayList<>();
        for (int i = 0; i < nbParties; i++) {
            Element partieCourante = (Element) _doc.getElementsByTagName("partie").item(i);
            ajouterPartie(new Partie(partieCourante));
        }

    }
    
    public void ajouterPartie(Partie p) {
        this.parties.add(p);
    }
    
    public int getDernierNiveau() {
        Partie lastPartie = parties.get(parties.size()-1);
        return lastPartie.getNiveau();
    }
    
    @Override
    public String toString() {
        String s = "Nom : "+name+" Date de naissance : "+birthdate+" Avatar : "+avatar+" parties :";
        for (Partie party : parties) {
            s+= "\n\t"+party.toString();
        }
        return s;
    }
    
    public void sauvegarder(String filename) {
        toXML(filename);
    }

    // Cree un DOM à partir d'un fichier XML
    public Document fromXML(String nomFichier) {
        try {
            return XMLUtil.DocumentFactory.fromFile(nomFichier);
        } catch (Exception ex) {
            Logger.getLogger(Profil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    // Sauvegarde un DOM en XML
    public void toXML(String nomFichier) {
        try {
            XMLUtil.DocumentTransform.writeDoc(_doc, nomFichier);
        } catch (Exception ex) {
            Logger.getLogger(Profil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /// Takes a date in XML format (i.e. ????-??-??) and returns a date
    /// in profile format: dd/mm/yyyy
    public static String xmlDateToProfileDate(String xmlDate) {
        String date;
        // récupérer le jour
        date = xmlDate.substring(xmlDate.lastIndexOf("-") + 1, xmlDate.length());
        date += "/";
        // récupérer le mois
        date += xmlDate.substring(xmlDate.indexOf("-") + 1, xmlDate.lastIndexOf("-"));
        date += "/";
        // récupérer l'année
        date += xmlDate.substring(0, xmlDate.indexOf("-"));

        return date;
    }

    /// Takes a date in profile format: dd/mm/yyyy and returns a date
    /// in XML format (i.e. ????-??-??)
    public static String profileDateToXmlDate(String profileDate) {
        String date;
        // Récupérer l'année
        date = profileDate.substring(profileDate.lastIndexOf("/") + 1, profileDate.length());
        date += "-";
        // Récupérer  le mois
        date += profileDate.substring(profileDate.indexOf("/") + 1, profileDate.lastIndexOf("/"));
        date += "-";
        // Récupérer le jour
        date += profileDate.substring(0, profileDate.indexOf("/"));

        return date;
    }

    
    /*
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public ArrayList<Partie> getParties() {
        return parties;
    }

    public void setParties(ArrayList<Partie> parties) {
        this.parties = parties;
    }
    */

}
