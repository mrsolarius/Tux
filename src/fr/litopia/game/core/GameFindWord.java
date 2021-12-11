package fr.litopia.game.core;

import com.jme3.bullet.BulletAppState;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.texture.Texture;
import com.jme3.util.SkyFactory;
import fr.litopia.game.assets.factory.LettersFactory;
import fr.litopia.game.assets.listeners.LettersListener;
import fr.litopia.game.assets.movable.Tux;
import fr.litopia.game.assets.scene.LetterPlot;
import fr.litopia.game.assets.scene.Room;
import fr.litopia.game.model.Partie;
import fr.litopia.game.model.Profil;

import java.util.ArrayList;

public class GameFindWord extends GameLifeCycle implements LettersListener {
    private Profil profil;
    private Partie partie;
    private ArrayList<Integer> letterFound;
    private LettersFactory lettersFactory;
    private int chrono;
    private int score;
    private Room room;
    private Tux tux;

    public GameFindWord(String profileName, boolean profileIsNew, int level){
        letterFound = new ArrayList<>();
        profil = new Profil(profileName, "1999/25/10", "default");
        partie = new Partie(level,profil);
        lettersFactory = new LettersFactory(this, partie.getMot());
    }

    @Override
    public void init(GameLoop app){
        super.init(app);
        intPhysic();
        initGameScene();
    }

    @Override
    public void update(float fps) {
        tux.simpleUpdate();
        if (chrono >= 50000){
            if (!lettersFactory.isLetterSpawned())
                lettersFactory.spawnLetters();
        }
        if((chrono>=(200000+(partie.getMot().length()*50000+partie.getNiveau()*100000)))||
            letterFound.size()==partie.getMot().length()){
            cleanup();
        }
        chrono++;
    }

    @Override
    public void cleanup() {
        partie.setTemps(chrono);
        partie.setScore(score);
        app.getRootNode().detachAllChildren();
        app.getStateManager().detach(getBulletAppState());
        app.getViewPort().setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
        app.getCamera().setFrustumPerspective(45.0F, (float)app.getCamera().getWidth() / (float)app.getCamera().getHeight(), 1.0F, 1000.0F);
        app.getCamera().setLocation(new Vector3f(0.0F, 0.0F, 10.0F));
        app.getCamera().lookAt(new Vector3f(0.0F, 0.0F, 0.0F), Vector3f.UNIT_Y);
        app.endGame();
    }

    private void intPhysic(){
        BulletAppState bulletAppState = new BulletAppState();
        bulletAppState.setSpeed(8f);
        app.getStateManager().attach(bulletAppState);
        getBulletAppState().startPhysics();
        getBulletAppState().setDebugEnabled(true);
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
}
