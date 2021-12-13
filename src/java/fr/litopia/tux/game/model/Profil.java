/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.litopia.tux.game.model;

import java.io.File;
import java.util.ArrayList;
import org.w3c.dom.Document;
import fr.litopia.tux.game.utils.XMLUtil;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 *
 * @author zaettal
 */
public class Profil {
    private final String name;
    private final String birthdate;
    private String avatar;
    private final ArrayList<Partie> parties;
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
        this.parties.add(p);
    }

    public void addNewPartie(Partie p) {
        this.parties.add(p);
        _doc.getElementsByTagName("parties").item(0).appendChild(p.getPartie(_doc));
    }
    
    public int getDernierNiveau() {
        Partie lastPartie = parties.get(parties.size()-1);
        return lastPartie.getNiveau();
    }
    
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("Nom : " + name + " Date de naissance : " + birthdate + " Avatar : " + avatar + " parties :");
        for (Partie party : parties) {
            s.append("\n\t").append(party.toString());
        }
        return s.toString();
    }
    
    public void sauvegarder(String filename) {
        //On verifie que le dossier save existe
        //Si il n'existe pas, on le crée
        File f = new File(PROFILE_PATH);
        if (!f.exists()) {
            f.mkdir();
        }
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

    //Methode qui retourne les fichiers XML du dossier
    public static ArrayList<String> getAllProfileName() {
        ArrayList<String> profiles = new ArrayList<>();
        File folder = new File(PROFILE_PATH);
        if (!folder.exists()) {
            folder.mkdir();
        }
        File[] listOfFiles = folder.listFiles();
        assert listOfFiles != null;
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

}
