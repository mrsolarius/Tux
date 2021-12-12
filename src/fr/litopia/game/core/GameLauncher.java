/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.litopia.game.core;

import com.jme3.system.AppSettings;

/**
 *
 * @author zaettal
 */
public class GameLauncher {

    public static void main(String[] args) {
        // Deffinition des options de l'application
        AppSettings settings = new AppSettings(true);
        settings.setTitle("Tux Letter Game");
        //definition de la taille de la fenetre à 1920x1080 avec un framerate de 120fps
        //et la vsync activée pour les écrans moins rapides
        settings.setResolution(1920,1080);
        settings.setFrameRate(120);
        settings.setVSync(true);
        //definition des couleurs en 32bits
        settings.setBitsPerPixel(32);

        //Instanciation du jeu
        GameLoop testSimple = new GameLoop();
        //Parametrage du jeu avec les options définies
        testSimple.setShowSettings(false);
        testSimple.setSettings(settings);

        //Lancement du jeu !!!
        testSimple.start();
    }
}
