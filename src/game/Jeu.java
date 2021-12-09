/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.input.ChaseCamera;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
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

import static env3d.GameObjectAdapter.assetManager;
import static java.lang.Thread.sleep;

/**
 *
 * @author zaettal
 */
public abstract class Jeu extends Env{
    private BulletAppState bulletAppState;

    public BulletAppState getBulletAppState() {
        return bulletAppState;
    }

    private Room room;
    private Profil profil;
    private Tux tux;
    private ArrayList<Letter> lettres;
    private Dico dico;
    protected float speed = 1f;
    protected boolean paused = false;

    public Jeu() {
        this.rootNode.detachAllChildren();

        //inisialisation de la pysique
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);

        // Instancie une Room
        flyCam.setEnabled(false);
        // Règle la camera
        //setCameraXYZ(50, 30, 150);
        //setDefaultControl(true);
        // Désactive les contrôles par défaut
        //setDefaultControl(false);

        GraphicsDevice[] ge = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
        //env.setResolution(ge[0].getDisplayMode().getHeight(), ge[0].getDisplayMode().getHeight()/2,32);
        setResolution(1920, 1080,32);
        setDisplayStr(ge[0].getIDstring());


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
        viewPort.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
        setUpLight();
    }

    public void execute() {
        // pour l'instant, nous nous contentons d'appeler la méthode joue comme cela
        // et nous créons une partie vide, juste pour que cela fonctionne
        try {
            joue(new Partie((new Date().toString()),"Joueur",0));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Détruit l'environnement et provoque la sortie du programme
        exit();
    }

    private void loadWord(){
        String word = dico.getMotDepuisListeNiveau(4);
        int x = 10;
        for(char c : normalize(word.toLowerCase()).toCharArray()){
            lettres.add(new Letter(this,c,x,50));
            x=x+10;
        }
    }

    public void joue(Partie partie) throws InterruptedException {
        // TEMPORAIRE : on règle la room de l'environnement. Ceci sera à enlever lorsque vous ajouterez les menus.
        //setRoom(room);
        // Instancie un Tux
        bulletAppState.startPhysics();

        room = new Room(this,200,200,200,"/textures/stone_granite.png","/textures/stonebrick.png","/textures/stonebrick.png","/textures/stonebrick.png","/textures/stonebrick.png");

        tux = new Tux(this);

        sleep(1000);
        loadWord();
         
        // Ici, on peut initialiser des valeurs pour une nouvelle partie
        démarrePartie(partie);
         
        // Boucle de jeu
        Boolean finished;
        finished = false;
        while (!finished) {

            tux.simpleUpdate();
            // Contrôles globaux du jeu (sortie, ...)
            //1 is for escape key
            if (getKey() == 1) {
                finished = true;
            }
            advanceOneFrame();
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

    private void setUpLight() {
        // We add light so we see the scene
        AmbientLight al = new AmbientLight();
        al.setColor(ColorRGBA.White.mult(1.3f));
        rootNode.addLight(al);

        DirectionalLight dl = new DirectionalLight();
        dl.setColor(ColorRGBA.White);
        dl.setDirection(new Vector3f(2.8f, -2.8f, -2.8f).normalizeLocal());
        rootNode.addLight(dl);
    }
}
