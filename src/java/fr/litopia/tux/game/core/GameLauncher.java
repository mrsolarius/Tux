/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.litopia.tux.game.core;

import com.jme3.system.AppSettings;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author zaettal
 */
public class GameLauncher {

    public static void main(String[] args) throws IOException {
        String pathRes = System.getProperty("user.dir") + File.separator + "src" + File.separator + "res"+File.separator+"assets";
        
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

        //Mise en pleine écran
        settings.setFullscreen(true);

        //Chargement de l'icone de l'application
        BufferedImage[] icons = new BufferedImage[] {
            ImageIO.read(new FileInputStream(pathRes+File.separator+"ico"+File.separator+"ico-128.png")),
            ImageIO.read(new FileInputStream(pathRes+File.separator+"ico"+File.separator+"ico-64.png")),
            ImageIO.read(new FileInputStream(pathRes+File.separator+"ico"+File.separator+"ico-32.png")),
            ImageIO.read(new FileInputStream(pathRes+File.separator+"ico"+File.separator+"ico-16.png"))
        };
        settings.setIcons(icons);
        //Deactivation de l'image par défaut
        settings.setSettingsDialogImage("");
        
        //Instanciation du jeu
        GameLoop gameLoop = new GameLoop();
        //Parametrage du jeu avec les options définies
        gameLoop.setShowSettings(true);
        gameLoop.setSettings(settings);

        //Lancement du jeu !!!
        gameLoop.start();
    }
}
