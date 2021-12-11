package fr.litopia.game.core;

import com.jme3.app.state.VideoRecorderAppState;
import com.jme3.bullet.BulletAppState;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.system.Timer;
import com.jme3.texture.Texture;
import com.jme3.util.SkyFactory;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.elements.events.NiftyMousePrimaryClickedEvent;
import de.lessvoid.nifty.screen.Screen;
import fr.litopia.game.assets.factory.LettersFactory;
import fr.litopia.game.assets.listeners.LettersListener;
import fr.litopia.game.assets.movable.Letter;
import fr.litopia.game.assets.movable.Tux;
import fr.litopia.game.assets.scene.LetterPlot;
import fr.litopia.game.assets.scene.Room;
import fr.litopia.game.gui.hud.PreGame;
import fr.litopia.game.model.Dico;
import fr.litopia.game.model.Partie;
import fr.litopia.game.model.Profil;

import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameFindWord extends GameLifeCycle implements LettersListener {
    private Profil profil;
    public static Partie partie;
    private Dico dico;
    private ArrayList<Integer> letterFound;
    private LettersFactory lettersFactory;
    private int lastSecond=0;
    private int chrono;
    private int score;
    private Room room;
    private Tux tux;
    private PreGame pregame;

    public GameFindWord(String profileName, int level){
        constructorInit(profileName,level);
    }

    private void constructorInit(String profileName, int level) {
        letterFound = new ArrayList<>();
        dico = new Dico("src/res/xml/dico.xml");
        try {
            dico.lireDictionnaireDOM();
        }catch (Exception e){
            Logger.getLogger(Profil.class.getName()).log(Level.SEVERE, null, e);
        }
        profil = new Profil(profileName);
        partie = new Partie("11/12/2013",dico.getMotDepuisListeNiveau(level),level);
        lettersFactory = new LettersFactory(this, partie.getMot());
    }

    @Override
    public void init(GameLoop app){
        super.init(app);
        intPhysic();
        initGameScene();
        app.getTimer().reset();
        app.getNifty().subscribeAnnotations(this);
        app.getNifty().gotoScreen("PreGame");
        state=GameState.CINEMATIC;
    }

    @Override
    public void update(float fps) {
        if(state == GameState.GAME_RUNNING || state == GameState.CINEMATIC){
            chrono = (int) app.getTimer().getTimeInSeconds();
            tux.simpleUpdate();
            if (chrono <= 10 && state == GameState.CINEMATIC) {
                cinematicNumber();
            }
            if (chrono >= 10) {
                if (!lettersFactory.isLetterSpawned()) {
                    lettersFactory.spawnLetters();
                    app.getTimer().reset();
                    app.getNifty().gotoScreen("HUD");
                }
            }
            if ((chrono >= (20 + (partie.getMot().length() * 5 + partie.getNiveau() * 10))) ||
                    letterFound.size() == partie.getMot().length()) {
                state = GameState.MENU;
                app.getInputManager().setCursorVisible(true);
                save();
                app.getNifty().gotoScreen("EndGame");

            }
        }
    }

    @Override
    public void cleanup() {
        LetterPlot.resetCount();
        Letter.resetCount();

        app.getRootNode().detachAllChildren();
        app.getStateManager().detach(getBulletAppState());
        app.getViewPort().setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
        app.getCamera().setFrustumPerspective(45.0F, (float)app.getCamera().getWidth() / (float)app.getCamera().getHeight(), 1.0F, 1000.0F);
        app.getCamera().setLocation(new Vector3f(0.0F, 0.0F, 10.0F));
        app.getCamera().lookAt(new Vector3f(0.0F, 0.0F, 0.0F), Vector3f.UNIT_Y);
    }

    public void save(){
        partie.setTemps(chrono);
        partie.setScore(score);
        partie.setTrouve(partie.getMot().length()-letterFound.size());
        profil.ajouterPartie(partie);
        profil.sauvegarder(profil.getName());
    }

    private void cinematicNumber(){
        if(chrono==2 && chrono!=lastSecond){
            app.getNifty().gotoScreen("PreGame1");
        }else if(chrono==3 && chrono!=lastSecond){
            app.getNifty().gotoScreen("PreGame2");
        }else if(chrono==4 && chrono!=lastSecond){
            app.getNifty().gotoScreen("PreGame3");
        }else if(chrono==5 && chrono!=lastSecond){
            app.getNifty().gotoScreen("PreGame4");
        }else if(chrono==6 && chrono!=lastSecond){
            app.getNifty().gotoScreen("PreGame5");
        }else if(chrono==7 && chrono!=lastSecond){
            app.getNifty().gotoScreen("PreGame6");
            state = GameState.GAME_RUNNING;
        }
        lastSecond=chrono;
    }

    private void intPhysic(){
        BulletAppState bulletAppState = new BulletAppState();
        bulletAppState.setSpeed(8f);
        app.getStateManager().attach(bulletAppState);
        getBulletAppState().startPhysics();
    }

    private void initGameScene(){
        app.getViewPort().setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
        setUpLight();
        setUpSkyBox();
        room = new Room(this,220,200,200,"/textures/floor_stone.png","/textures/1x1.png","/textures/1x1.png","/textures/1x1.png","/textures/1x1.png");
        tux = new Tux(this);
        lettersFactory.spawnPlots();
    }

    private void setUpSkyBox(){
        // Préparation de la skybox
        Texture west = app.getAssetManager().loadTexture("textures/skybox/holodeck/west.png");
        Texture east = app.getAssetManager().loadTexture("textures/skybox/holodeck/east.png");
        Texture north = app.getAssetManager().loadTexture("textures/skybox/holodeck/north.png");
        Texture south = app.getAssetManager().loadTexture("textures/skybox/holodeck/south.png");
        Texture up = app.getAssetManager().loadTexture("textures/skybox/holodeck/top.png");
        Texture down = app.getAssetManager().loadTexture("textures/skybox/holodeck/bottom.png");
        app.getRootNode().attachChild(SkyFactory.createSky(app.getAssetManager(), west, east, north, south, up, down));
    }

    private void setUpLight(){
        // Mise en place des lumières
        AmbientLight al = new AmbientLight();
        al.setColor(ColorRGBA.White.mult(1.3f));
        app.getRootNode().addLight(al);

        DirectionalLight dl = new DirectionalLight();
        dl.setColor(ColorRGBA.White);
        dl.setDirection(new Vector3f(2.8f, -2.8f, -2.8f).normalizeLocal());
        app.getRootNode().addLight(dl);
    }

    public Room getRoom() {
        return room;
    }

    @Override
    public void updateLetter(LetterPlot lettrePlot) {
        if(lettrePlot.isCorrect()){
            if(!letterFound.contains(lettrePlot.getWordPosition())){
                letterFound.add(lettrePlot.getWordPosition());
                score+=100;
            }else{
                score-=10;
            }
        }else{
            score-=5;
            if(letterFound.contains(lettrePlot.getWordPosition())) {
                try {
                    letterFound.remove(lettrePlot.getWordPosition());
                }   catch (Exception e){
                    System.out.println("Erreur");
                    e.printStackTrace();
                }
            }
        }
    }

    @NiftyEventSubscriber(id="EndGameButtonReplay")
    public void onReplayGameClick(String id, NiftyMousePrimaryClickedEvent event) {
        save();
        cleanup();
        int level = partie.getNiveau();
        constructorInit(this.profil.getName(),level);
        init(this.app);
    }

    @NiftyEventSubscriber(id="EndGameButtonEnd")
    public void onQuitGameClick(String id, NiftyMousePrimaryClickedEvent event) {
        save();
        cleanup();
        app.getTimer().reset();
        app.endGame();
    }
}
