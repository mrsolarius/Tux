/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import com.jme3.system.AppSettings;
import env3d.Env;
import org.xml.sax.SAXException;

import java.awt.*;
import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 *
 * @author zaettal
 */
public abstract class Jeu {
    private Env env;
    private Room room;
    private Profil profil;
    private Tux tux;
    private ArrayList<Letter> lettres;
    private Dico dico;
    
    public Jeu() {
        // Crée un nouvel environnement
        env = new Env();

        // Instancie une Room
        room = new Room("/textures/stone_granite.png","/textures/stonebrick.png","/textures/stonebrick.png","/textures/stonebrick.png");

        // Règle la camera
        env.setCameraXYZ(room.getWidth()/2.0, room.getHeight()/1.6, room.getDepth()*1.25);
        env.setDefaultControl(true);
        env.setCameraPitch(-45);
        // Désactive les contrôles par défaut
        env.setDefaultControl(false);

        GraphicsDevice[] ge = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
        //env.setResolution(ge[0].getDisplayMode().getHeight(), ge[0].getDisplayMode().getHeight()/2,32);
        env.setResolution(1920, 1080,32);
        env.setDisplayStr(ge[0].getIDstring());


        // Instancie un profil par défaut
        profil = new Profil();

        // Instancie des lettres
        lettres = new ArrayList<Letter>();

        dico = new Dico("xml/dico.xml");
        try {
            dico.lireDictionnaireDOM("src/xml/", "dico.xml");
        } catch (SAXException ex) {
            Logger.getLogger(LanceurDeJeu.class.getName()).log(Level.parse("Ici"+ Level.SEVERE), null, ex);
        } catch (IOException ex) {
            Logger.getLogger(LanceurDeJeu.class.getName()).log(Level.parse("là"+ Level.SEVERE), null, ex);
        }
    }
    
    public void execute() {
        // pour l'instant, nous nous contentons d'appeler la méthode joue comme cela
        // et nous créons une partie vide, juste pour que cela fonctionne
        joue(new Partie((new Date().toString()),"Joueur",0));
         
        // Détruit l'environnement et provoque la sortie du programme 
        env.exit();
    }

    private void loadWord(){
        String word = dico.getMotDepuisListeNiveau(4);
        int x = 10;
        for(char c : normalize(word.toLowerCase()).toCharArray()){
            lettres.add(new Letter(c,x,50));
            x=x+10;
        }
        for(Letter let : lettres){
            env.addObject(let);
        }
    }

    public void joue(Partie partie) {
        // TEMPORAIRE : on règle la room de l'environnement. Ceci sera à enlever lorsque vous ajouterez les menus.
        env.setRoom(room);
        // Instancie un Tux
        tux = new Tux(room,env);

        loadWord();

        env.addObject(tux);
         
        // Ici, on peut initialiser des valeurs pour une nouvelle partie
        démarrePartie(partie);
         
        // Boucle de jeu
        Boolean finished;
        finished = false;
        while (!finished) {
 
            // Contrôles globaux du jeu (sortie, ...)
            //1 is for escape key
            if (env.getKey() == 1) {
                finished = true;
            }
 
            // Contrôles des déplacements de Tux (gauche, droite, ...)
            // ... (sera complété plus tard) ...
            tux.déplace();
            // Ici, on applique les regles
            appliqueRegles(partie);
 
            // Fait avancer le moteur de jeu (mise à jour de l'affichage, de l'écoute des événements clavier...)
            env.advanceOneFrame();
        }
 
        // Ici on peut calculer des valeurs lorsque la partie est terminée
        terminePartie(partie);
    }
    
    protected abstract void démarrePartie(Partie partie);
    
    protected abstract void appliqueRegles(Partie partie);
    
    protected abstract void terminePartie(Partie partie);

    protected double distance(Letter letter) {
        return Math.sqrt(Math.pow(getTux().getX()-letter.getX(),2)+Math.pow(getTux().getY()-letter.getY(),2));
    }

    protected Boolean collision(Letter letter) {
        return distance(letter)<=getTux().getScale();
    }

    //Remplacer les caractères accentués par leur équivalent sans accent
    public String normalize(String str) {
        String normalized = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalized).replaceAll("");
    }

    public Tux getTux() {
        return tux;
    }

    public ArrayList<Letter> getLettres() {
        return lettres;
    }
}
