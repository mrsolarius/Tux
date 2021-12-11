/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.litopia.game.model;

import java.io.File;
import java.util.ArrayList;
import org.w3c.dom.Document;
import fr.litopia.game.utils.XMLUtil;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.builder.XmlDocument;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

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
    public static final String PROFILE_PATH = "src/res/profil/save/";

    public Profil(String name, String birthdate) {
        this.name = name;
        this.birthdate = birthdate;
        /*this.avatar = avatar;*/
        this.parties = new ArrayList<>();
        //Initialisation du document
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            _doc = docBuilder.newDocument();
            // root elements
            Element rootElement = _doc.createElement("profil");
            _doc.appendChild(rootElement);

            // nom elements
            Element nom = _doc.createElement("nom");
            nom.appendChild(_doc.createTextNode(name));
            rootElement.appendChild(nom);

            // anniversaire elements
            Element anniversaire = _doc.createElement("anniversaire");
            anniversaire.appendChild(_doc.createTextNode(XMLUtil.profileDateToXmlDate(birthdate)));
            rootElement.appendChild(anniversaire);

            // avatar elements
            Element avatar = _doc.createElement("avatar");
            avatar.appendChild(_doc.createTextNode("avatar"));
            rootElement.appendChild(avatar);

            // parties elements
            Element parties = _doc.createElement("parties");
            rootElement.appendChild(parties);
        }catch (Exception ex) {
            Logger.getLogger(Profil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // Cree un DOM à partir d'un fichier XML
    public Profil(String nomFichier) {
        _doc = fromXML(PROFILE_PATH+nomFichier+".xml");
        // parsing à compléter
        this.name = _doc.getElementsByTagName("nom").item(0).getTextContent();
        this.avatar = _doc.getElementsByTagName("avatar").item(0).getTextContent();
        this.birthdate = XMLUtil.xmlDateToProfileDate(_doc.getElementsByTagName("anniversaire").item(0).getTextContent());
        int nbParties = _doc.getElementsByTagName("partie").getLength();
        this.parties = new ArrayList<>();
        for (int i = 0; i < nbParties; i++) {
            Element partieCourante = (Element) _doc.getElementsByTagName("partie").item(i);
            ajouterPartie(new Partie(partieCourante));
        }

    }
    
    public void ajouterPartie(Partie p) {
        _doc.getElementsByTagName("parties").item(0).appendChild(p.getPartie(_doc));
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
        toXML(PROFILE_PATH+filename+".xml");
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

    public String getName() {
        return name;
    }

    //static methode that return arraylist of string base on eatch file in the folder
    public static ArrayList<String> getAllProfileName() {
        ArrayList<String> profiles = new ArrayList<>();
        File folder = new File(PROFILE_PATH);
        File[] listOfFiles = folder.listFiles();
        for (File file : listOfFiles) {
            if (file.isFile()) {
                profiles.add(file.getName().replace(".xml", ""));
            }
        }
        return profiles;
    }

    public String getBirthdate() {
        return birthdate;
    }

    /*

    public void setName(String name) {
        this.name = name;
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
