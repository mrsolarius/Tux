package fr.litopia.game.gui.hud;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.SizeValue;
import fr.litopia.game.core.GameFindWord;

public class PreGame implements ScreenController {
    private static Nifty nifty;
    private Label preloaderTxt;
    private Boolean connected=false;

    @Override
    public void bind(Nifty nifty, Screen screen) {
        System.out.println("bind");
        PreGame.nifty = nifty;
        preloaderTxt = screen.findNiftyControl("PreloaderWord", Label.class);
        connected = true;
    }

    @Override
    public void onStartScreen() {
        if(GameFindWord.partie!=null){
            preloaderTxt.setText(GameFindWord.partie.getMot());
        }
    }

    @Override
    public void onEndScreen() {

    }
}
