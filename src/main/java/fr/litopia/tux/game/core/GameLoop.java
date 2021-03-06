package fr.litopia.tux.game.core;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.audio.AudioData;
import com.jme3.audio.AudioNode;
import com.jme3.niftygui.NiftyJmeDisplay;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.elements.events.NiftyMousePrimaryClickedEvent;
import de.lessvoid.nifty.screen.Screen;
import fr.litopia.tux.game.gui.menu.MainMenu;
import fr.litopia.tux.game.model.Profil;

import java.io.File;

import static fr.litopia.tux.game.core.GameLauncher.ASSETS_PATH;
import static fr.litopia.tux.game.core.GameState.*;

public class GameLoop extends SimpleApplication {
    private Nifty nifty;
    private GameState state;
    private GameFindWord game;
    public static Profil profil;
    private AudioNode music;

    /**
     * Initialisation de l'application
     */
    @Override
    public void simpleInitApp() {
        //Désactivation des prefabs
        flyCam.setEnabled(false);
        setDisplayFps(false);
        setDisplayStatView(false);

        //Definition du statut en mode initialisation
        state = INIT;
        //Definition de l'emplacement de stockage des assets
        assetManager.registerLocator(ASSETS_PATH, FileLocator.class);
        //Initialisation des menus Nifty
        initNifty();
        //Récupération de la music de menu
        music = new AudioNode(assetManager, "Sounds/music/minecraftmusicmenu.wav", AudioData.DataType.Stream);
        music.setLooping(true);
        music.setVolume(4f);
        getAudioRenderer().playSource(music);
        //Fin de l'initialisation passage au statut menu
        state = MENU;
    }

    /**
     * Boucle de jeu mis à jour tous les tick jeux
     * @param tpf temps par frame
     */
    @Override
    public void simpleUpdate(float tpf) {
        //Si le jeu est en cours alors on met à jour le jeu
        if(state == GAME_RUNNING){
            game.update(tpf);
        }
        //Si on est dans les menus on met à jour le menu
        if(state == MENU){
            nifty.invokeMethods();
        }
    }

    /**
     * Callback appelé lorsque lors du clic sur le button "LoadPlayer"
     * On peut alors lancer le jeu avec le nom du joueur
     * @param id id du bouton
     * @param event evenement
     */
    @NiftyEventSubscriber(id="LoadPlayerButton")
    public void onLoadPlayerButtonClick(String id, NiftyMousePrimaryClickedEvent event) {
        Screen screen = nifty.getScreen("LoadPlayer");
        TextField textField = screen.findNiftyControl("playerLoadField", TextField.class);
        profil = new Profil(textField.getRealText());
        nifty.gotoScreen("ChooseLevel");
    }

    /**
     * Callback appelé lorsque lors du clic sur le button "NewPlayerButton"
     * On peut alors lancer le jeu avec le nom du joueur
     * @param id id du bouton
     * @param event evenement
     */
    @NiftyEventSubscriber(id="NewPlayerButton")
    public void onNewPlayerButtonClick(String id, NiftyMousePrimaryClickedEvent event) {
        Screen screen = nifty.getScreen("NewPlayer");
        TextField textField = screen.findNiftyControl("playerNewField", TextField.class);
        profil = new Profil(textField.getRealText(),"25/10/1999");
        profil.sauvegarder(textField.getRealText());
        nifty.gotoScreen("ChooseLevel");
    }

    /**
     * Callback appelé lorsque lors du clic sur le button "playButton"
     * On peut alors lancer le jeu avec le nom du joueur
     * @param id id du bouton
     *
     */
    @NiftyEventSubscriber(id="playButton")
    public void onPlayButtonClick(String id, NiftyMousePrimaryClickedEvent event) {
        Screen screen = nifty.getScreen("ChooseLevel");
        TextField textField = screen.findNiftyControl("playerLoadField", TextField.class);
        game = new GameFindWord(profil.getName(),Integer.parseInt(textField.getRealText()));
        nifty.gotoScreen("PreGame");
        initGame();
    }

    /**
     * Initialisation de Nifty
     * Permet de charger les éléments du menu
     * Et de les lancer
     */
    private void initNifty(){
        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(
                assetManager,
                inputManager,
                audioRenderer,
                guiViewPort);
        nifty = niftyDisplay.getNifty();
        MainMenu startScreen = new MainMenu(this);
        nifty.fromXml("xml/menus.xml", "MainMenu", startScreen);
        //Souscription à l'observer
        nifty.subscribeAnnotations(this);
        //Lancement de Nifty
        guiViewPort.addProcessor(niftyDisplay);
    }

    /**
     * Initialisation du jeu
     * Permet de mettre à jour les status de la boucle de jeux et d'initialiser le jeu
     */
    private void initGame(){
        //Game peut être null si on a pas de nom de joueur donc on verifie qu'il à bien été créé
        if(game!=null){
            //On arrête la musique de menu
            getAudioRenderer().stopSource(music);
            //On met à jour l'écran du jeu
            nifty.gotoScreen("Game");
            //On initialise le jeu
            game.init(this);
            //On met à jour le statut du jeu
            state = GAME_RUNNING;
        }
    }

    /**
     * Callback appelé par le jeu lorsque qu'il se termine
     * Il permet de revenir au menu et de mettre à jour le state
     */
    public void endGame(){
        //On passe au menu
        state = MENU;
        //On supprime le jeu
        game = null;
        //On revient au menu
        nifty.gotoScreen("MainMenu");
        //On lance la musique de menu
        getAudioRenderer().playSource(music);
        //on affiche le curseur pour le voir apparaitre sur les menus
        inputManager.setCursorVisible(true);
    }

    public Nifty getNifty() {
        return nifty;
    }

    public void setPause(boolean pause){
        paused = pause;
    }
}
