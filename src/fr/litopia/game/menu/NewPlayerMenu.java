package fr.litopia.game.menu;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class NewPlayerMenu implements ScreenController {
    private Nifty nifty;

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

    public void back(){
        nifty.gotoScreen("ProfileMenu");
    }

    public void createNewPlayer(){

    }
}
