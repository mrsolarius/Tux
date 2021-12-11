/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.litopia.game.core;

import java.util.ArrayList;

import com.jme3.system.AppSettings;
import fr.litopia.game.assets.movable.Letter;

/**
 *
 * @author zaettal
 */
public class LanceurDeJeu {

    private ArrayList<Letter> lettres;

    public static void main(String[] args) {
        /*
        Dico jeSuisUnePatate = new Dico("src/res/xml/dico.xml");
        try {
            jeSuisUnePatate.lireDictionnaireDOM("src/res/xml/", "dico.xml");
        } catch (SAXException ex) {
            Logger.getLogger(LanceurDeJeu.class.getName()).log(Level.parse("Ici"+ Level.SEVERE), null, ex);
        } catch (IOException ex) {
            Logger.getLogger(LanceurDeJeu.class.getName()).log(Level.parse("là"+ Level.SEVERE), null, ex);
        }
        System.out.println(jeSuisUnePatate.toString());

        /*
        // Declare un Jeu
        Jeu jeu;
        //Instancie un nouveau jeu
        jeu = new JeuDevineLeMotOrdre();
        //Execute le jeu
        jeu.execute();*/
        AppSettings settings = new AppSettings(true);
        settings.setTitle("Tux Letter Game");
        settings.setResolution(1920,1080);
        settings.setFrameRate(120);
        settings.setVSync(true);
        settings.setBitsPerPixel(32);

        GameLoop testSimple = new GameLoop();
        testSimple.setShowSettings(false);
        testSimple.setSettings(settings);
        testSimple.start();
    }
}
