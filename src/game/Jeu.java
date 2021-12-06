/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import env3d.Env;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
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
        room = new Room("/textures/stone.png","/textures/doty_happy.png","/textures/stone.png","/textures/stone.png");

        // Règle la camera
        env.setCameraXYZ(50, 60, 175);
        env.setCameraPitch(-20);
        // Désactive les contrôles par défaut
        env.setDefaultControl(false);

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
        joue(new Partie());
         
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
    
    public abstract void démarrePartie(Partie partie);
    
    public abstract void appliqueRegles(Partie partie);
    
    public abstract void terminePartie(Partie partie);

    //replace every accent character by its non-accented equivalent
    public String normalize(String str) {
        String normalized = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalized).replaceAll("");
    }
}
