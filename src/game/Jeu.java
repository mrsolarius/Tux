/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import env3d.Env;
import java.util.ArrayList;

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
    }
    
    public void execute() {
        // pour l'instant, nous nous contentons d'appeler la méthode joue comme cela
        // et nous créons une partie vide, juste pour que cela fonctionne
        joue(new Partie());
         
        // Détruit l'environnement et provoque la sortie du programme 
        env.exit();
    }
    
    public void joue(Partie partie) {
        // TEMPORAIRE : on règle la room de l'environnement. Ceci sera à enlever lorsque vous ajouterez les menus.
        env.setRoom(room);
        // Instancie un Tux
        tux = new Tux(room,env);
        Letter a = new Letter('a',10,50);
        Letter l = new Letter('l',20,50);
        Letter e = new Letter('e',30,50);
        Letter d = new Letter('d',40,50);
        lettres.add(a);
        lettres.add(l);
        lettres.add(e);
        lettres.add(d);
        
        env.addObject(tux);
        for(Letter let : lettres){
            env.addObject(let);
        }
         
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
}
