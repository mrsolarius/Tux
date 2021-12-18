package fr.litopia.tux.game.gui.menu;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class ProfileMenu implements ScreenController {
    private Nifty nifty;
    private Element popup;

    @Override
    public void bind(Nifty nifty, Screen screen) {
        //appeler quand on à cliquer sur lancer une nouvelle partie depuis le menu principal
        this.nifty = nifty;
    }

    @Override
    public void onStartScreen() {

    }

    @Override
    public void onEndScreen() {

    }

    public void newPlayer(){
        nifty.gotoScreen("NewPlayer");
    }

    public void loadPlayer() {
        nifty.gotoScreen("LoadPlayer");
    }

    public void back() {
        nifty.gotoScreen("MainMenu");
    }
}
