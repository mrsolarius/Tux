package fr.litopia.game.gui.menu;

import com.jme3.app.SimpleApplication;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.elements.Element;

public class MainMenu implements ScreenController {
    private final SimpleApplication jeu;
    private Nifty nifty;
    private Element popup;

    public MainMenu(SimpleApplication jeu) {
        // et appelé au lancement de l'application
        this.jeu = jeu;
    }

    @Override
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
    }

    @Override
    public void onStartScreen() {

    }

    @Override
    public void onEndScreen() {

    }

    public void startGame() {
        nifty.gotoScreen("ProfileMenu");
    }

    public void quit(){
        jeu.stop();
    }

}
