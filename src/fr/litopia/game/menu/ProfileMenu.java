package fr.litopia.game.menu;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ControlBuilder;
import de.lessvoid.nifty.builder.PopupBuilder;
import de.lessvoid.nifty.controls.Menu;
import de.lessvoid.nifty.controls.MenuItemActivatedEvent;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.SizeValue;
import org.bushe.swing.event.EventTopicSubscriber;

public class ProfileMenu implements ScreenController {
    private Nifty nifty;
    private Element popup;

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
