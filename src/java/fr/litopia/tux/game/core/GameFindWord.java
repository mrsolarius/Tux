package fr.litopia.tux.game.core;

import com.jme3.bullet.BulletAppState;
import com.jme3.light.AmbientLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.ssao.SSAOFilter;
import com.jme3.texture.Texture;
import com.jme3.util.SkyFactory;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.elements.events.NiftyMousePrimaryClickedEvent;
import fr.litopia.tux.game.model.Profil;

import fr.litopia.tux.game.objects.factory.LettersFactory;
import fr.litopia.tux.game.objects.listeners.LettersListener;
import fr.litopia.tux.game.objects.movable.Letter;
import fr.litopia.tux.game.objects.movable.Tux;
import fr.litopia.tux.game.objects.scene.LetterPlot;
import fr.litopia.tux.game.objects.scene.Room;
import fr.litopia.tux.game.gui.hud.HUD;
import fr.litopia.tux.game.model.Dico;
import fr.litopia.tux.game.model.Partie;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameFindWord extends GameLifeCycle implements LettersListener {
    private Room room;
    private Tux tux;
    private Profil profil;
    public static Partie partie;
    private ArrayList<Integer> letterFound;
    private LettersFactory lettersFactory;
    private int lastSecond, chrono, score;

    public GameFindWord(String profileName, int level) {
        constructorInit(profileName, level);
    }
    /**
     * Constructeur de la classe GameFindWord sous forme de methode pour simplifier la réinitialisation de la partie
     * @param profileName Nom du profil
     * @param level Niveau de difficulté
     */
    private void constructorInit(String profileName, int level) {
        state = GameState.INIT;
        letterFound = new ArrayList<>();
        Dico dico = new Dico("src/res/xml/dico.xml");
        try {
            dico.lireDictionnaireDOM();
        } catch (Exception e) {
            Logger.getLogger(Profil.class.getName()).log(Level.SEVERE, null, e);
        }
        profil = new Profil(profileName);
        LocalDateTime ldt = LocalDateTime.now().plusDays(1);
        DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.FRANCE);
        String strDate = formatDate.format(ldt);
        partie = new Partie(strDate, dico.getMotDepuisListeNiveau(level), level);
        lettersFactory = new LettersFactory(this, partie.getMot());
        lastSecond=0;
        score=0;
        chrono=0;
    }

    /**
     * Methode d'inisialisation de la partie
     * Elle permet de mettre en place le jeu
     * @param app Application de la boucle principale
     */
    @Override
    public void init(GameLoop app) {
        super.init(app);
        intPhysic();
        initGameScene();
        //Reset du timer
        app.getTimer().reset();
        //Suscription à l'observer des evenements de l'interface graphique Nifty
        app.getNifty().subscribeAnnotations(this);
        //Inisialisation de la cinématique de début de jeux
        app.getNifty().gotoScreen("PreGame");
        //Passage en mode cinématique
        state = GameState.CINEMATIC;
    }

    /**
     * Methode de mise à jour de la partie doit être appelée à chaque tick du jeu
     * @param fps Nombre de frame par seconde
     */
    @Override
    public void update(float fps) {
        //Si le jeu est en mode cinématique ou en mode jeu
        if (state == GameState.GAME_RUNNING || state == GameState.CINEMATIC) {
            //On récupère la valeur courante du timer
            chrono = (int) app.getTimer().getTimeInSeconds();
            //On met à jour les mouvements de tux
            tux.simpleUpdate();

            // Si on et en mode cinématique et que le timer est inférieur à 10 secondes
            if (chrono <= 10 && state == GameState.CINEMATIC) {
                //On affiche la cinématique en fonction du timer
                cinematicNumber();
            }

            //Si on et en mode jeu
            if (state == GameState.GAME_RUNNING) {
                //Si on à pas encore fait poper les lettres
                if (!lettersFactory.isLetterSpawned()) {
                    //On fait poper les lettres
                    lettersFactory.spawnLetters();
                    //On réset le timer car la partie commence
                    app.getTimer().reset();
                }
                //Si le chrono et egal à 2 (donc que la partie à commancé depuis 2 secondes)
                if(chrono==2){
                    //On affiche le HUD
                    app.getNifty().gotoScreen("HUD");
                }
                //Si le chrono est superieur au maximum de la partie ou que toutes les lettres sont trouvées
                if ((chrono >= partie.getMaxTime()) || (letterFound.size() == partie.getMot().length())) {
                    //Alors on passe en mode menu
                    state = GameState.MENU;
                    app.getInputManager().setCursorVisible(true);
                    //on sauvegarde le score de la partie
                    save();
                    //on affiche le menu de fin de jeux
                    app.getNifty().gotoScreen("EndGame");
                }

                //Quoi qu'il arrive tant qu'on est en mode jeu on met à jour le HUD et le score dans partie
                updatePartie();
                HUD.updateHUD();
            }
        }
        //lorsque le chrono est diférent de la dernière seconde alors on mes à jour la dernière seconde
        //Utile pour les cinématiques
        if (chrono != lastSecond) lastSecond = chrono;
    }

    /**
     * Methode de nettoyage de la partie qui permet de réinisialiser l'application
     * telle qu'elle était avant la création de la partie
     */
    @Override
    public void cleanup() {
        //On réinitialise les variables statiques des plots et des lettres
        LetterPlot.resetCount();
        Letter.resetCount();

        //Suprime tous les élément de la scène et de la physique
        app.getRootNode().detachAllChildren();
        app.getStateManager().detach(getBulletAppState());

        //On réinitialise le background de la scène
        app.getViewPort().setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));

        //on réinitialise la caméra tel qu'elle était avant la création de la partie
        app.getCamera().setFrustumPerspective(45.0F, (float) app.getCamera().getWidth() / (float) app.getCamera().getHeight(), 1.0F, 1000.0F);
        app.getCamera().setLocation(new Vector3f(0.0F, 0.0F, 10.0F));
        app.getCamera().lookAt(new Vector3f(0.0F, 0.0F, 0.0F), Vector3f.UNIT_Y);
    }

    /**
     * Methode de sauvegarde de la partie dans les fichiers XML
     */
    public void save() {
        updatePartie();
        profil.ajouterPartie(partie);
        profil.sauvegarder(profil.getName());
    }

    /**
     * Methode de mise à jour de la partie
     */
    public void updatePartie(){
        partie.setTemps(chrono);
        partie.setScore(score);
        partie.setTrouve(partie.getMot().length() - letterFound.size());
    }

    /**
     * Methode de mise en place de la cinématique de début de partie
     */
    private void cinematicNumber() {
        //On commence la cinématique 2 seconde apres l'initialisation de la partie
        //Chaque scene de cinématique est une seconde et correspond à un écran
        if (chrono == 2 && chrono != lastSecond) {
            app.getNifty().gotoScreen("PreGame1");
        } else if (chrono == 3 && chrono != lastSecond) {
            app.getNifty().gotoScreen("PreGame2");
        } else if (chrono == 4 && chrono != lastSecond) {
            app.getNifty().gotoScreen("PreGame3");
        } else if (chrono == 5 && chrono != lastSecond) {
            app.getNifty().gotoScreen("PreGame4");
        } else if (chrono == 6 && chrono != lastSecond) {
            app.getNifty().gotoScreen("PreGame5");
        } else if (chrono == 7 && chrono != lastSecond) {
            app.getNifty().gotoScreen("PreGame6");
            //Lorsque l'on arrive sur le dernier écran de la cinématique on passe en mode jeu car la partie commence
            state = GameState.GAME_RUNNING;
        }
    }

    /**
     * Methode initialisation de la physique
     */
    private void intPhysic() {
        BulletAppState bulletAppState = new BulletAppState();
        //on definit la vitess de la physique (le nombre de mise à jour par frame)
        bulletAppState.setSpeed(8f);
        //on attache la physique au stateManager qui et appeler à chaque tick par la classe parente Application
        app.getStateManager().attach(bulletAppState);
        getBulletAppState().startPhysics();
    }

    /**
     * Methode de mise en place de la scène
     */
    private void initGameScene() {
        //Définition du background de la scène
        app.getViewPort().setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
        //Mise en place des lumières
        setUpLight();
        //Création de la skybox
        setUpSkyBox();
        //Création de la scène dans la room
        room = new Room(this, 220, 200, 200, "assets/textures/floor_stone.png", "assets/textures/1x1.png", "assets/textures/1x1.png", "/textures/1x1.png", "/textures/1x1.png");
        //Initialisation du tux
        tux = new Tux(this);
        //Spawn des lettres
        lettersFactory.spawnPlots();
    }

    /**
     * Methode de mise en place de la skybox
     */
    private void setUpSkyBox() {
        // Préparation de la skybox
        Texture west = app.getAssetManager().loadTexture("assets/textures/skybox/holodeck/west.png");
        Texture east = app.getAssetManager().loadTexture("assets/textures/skybox/holodeck/east.png");
        Texture north = app.getAssetManager().loadTexture("assets/textures/skybox/holodeck/north.png");
        Texture south = app.getAssetManager().loadTexture("assets/textures/skybox/holodeck/south.png");
        Texture up = app.getAssetManager().loadTexture("assets/textures/skybox/holodeck/top.png");
        Texture down = app.getAssetManager().loadTexture("assets/textures/skybox/holodeck/bottom.png");
        app.getRootNode().attachChild(SkyFactory.createSky(app.getAssetManager(), west, east, north, south, up, down));
    }

    /**
     * Methode de mise en place des lumières
     */
    private void setUpLight() {
        // Mise en place des lumières
        AmbientLight al = new AmbientLight();
        al.setColor(ColorRGBA.White.mult(1.3f));
        app.getRootNode().addLight(al);

        // Mise en place d'un filtre SSAO pour créer de fausse ombres
        FilterPostProcessor fpp = new FilterPostProcessor(app.getAssetManager());
        SSAOFilter ssaoFilter = new SSAOFilter(2.9299974f,32.920483f,5.8100376f,0.091000035f);
        ssaoFilter.setEnabled(true);
        fpp.addFilter(ssaoFilter);
        app.getViewPort().addProcessor(fpp);
    }

    /**
     * getter de la room
     */
    public Room getRoom() {
        return room;
    }

    /**
     * Implementation du callback de letterplots
     * Qui permet de savoir si une lettre à touché un plot
     * @param lettrePlot : lettre qui a touché un plot
     */
    @Override
    public void updateLetter(LetterPlot lettrePlot) {
        //Si la lettre qui à touché le plot correspond à la lettre attendue
        if (lettrePlot.isCorrect()) {
            //Verfie que la lettre n'a pas déjà été trouvé
            if (!letterFound.contains(lettrePlot.getWordPosition())) {
                //Si elle na pas déjà été trouvé on l'ajoute à la liste des lettres trouvées
                //et on incrémente le score
                letterFound.add(lettrePlot.getWordPosition());
                score += 100;
            } else {
                //Sinon on décrémente le score
                score -= 10;
            }
        } else {
            //Si la lettre qui à touché le plot ne correspond pas à la lettre attendue
            //on décrémente le score
            score -= 5;
            //On regarde si son index n'est pas déjà dans les lettres trouvées
            if (letterFound.contains(lettrePlot.getWordPosition())) {
                //Si oui on la retire de la liste
                try {
                    letterFound.remove(lettrePlot.getWordPosition());
                } catch (Exception e) {
                    System.out.println("Erreur de suppression de la lettre");
                }
            }
        }
    }

    /**
     * Callback de l'événement du click sur le button rejouer de l'interface de fin de partie
     * @param id : id du bouton
     * @param event : evenement du click
     */
    @NiftyEventSubscriber(id = "EndGameButtonReplay")
    public void onReplayGameClick(String id, NiftyMousePrimaryClickedEvent event) {
        //On clean le jeu
        cleanup();
        //on récupère le niveau dans une variable temporaire
        int level = partie.getNiveau();
        //on refait appel au constructeur de la classe
        constructorInit(this.profil.getName(), level);
        //on réinitialise le jeux
        init(this.app);
    }

    /**
     * Callback de l'événement du click sur le button quitter de l'interface de fin de partie
     * @param id : id du bouton
     * @param event : evenement du click
     */
    @NiftyEventSubscriber(id = "EndGameButtonEnd")
    public void onQuitGameClick(String id, NiftyMousePrimaryClickedEvent event) {
        //on clean le jeu
        cleanup();
        //on reset le timer
        app.getTimer().reset();
        //on informe la boucle principale de la fin de partie
        app.endGame();
    }
}
