/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.font.BitmapText;
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
import com.jme3.texture.Texture;
import com.jme3.util.SkyFactory;
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
        this.guiNode.detachAllChildren();

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
        
        viewPort.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
        setUpLight();
        Texture west = getAssetManager().loadTexture("textures/skybox/holodeck/west.png");
        Texture east = getAssetManager().loadTexture("textures/skybox/holodeck/east.png");
        Texture north = getAssetManager().loadTexture("textures/skybox/holodeck/north.png");
        Texture south = getAssetManager().loadTexture("textures/skybox/holodeck/south.png");
        Texture up = getAssetManager().loadTexture("textures/skybox/holodeck/top.png");
        Texture down = getAssetManager().loadTexture("textures/skybox/holodeck/bottom.png");
        getRootNode().attachChild(SkyFactory.createSky(getAssetManager(), west, east, north, south, up, down));
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

    public void joue(Partie partie) throws InterruptedException {
        // TEMPORAIRE : on règle la room de l'environnement. Ceci sera à enlever lorsque vous ajouterez les menus.
        //setRoom(room);
        // Instancie un Tux
        bulletAppState.startPhysics();

        room = new Room(this,220,200,200,"/textures/floor_stone.png","/textures/1x1.png","/textures/1x1.png","/textures/1x1.png","/textures/1x1.png");

        tux = new Tux(this);

        sleep(1000);
         
        // Ici, on peut initialiser des valeurs pour une nouvelle partie
        démarrePartie(partie);
         
        // Boucle de jeu
        Boolean finished;
        finished = false;
        while (!finished) {
            appliqueRegles(partie);
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

    public Room getRoom() {
        return room;
    }
}
