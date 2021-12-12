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

    @Override
    public void bind(Nifty nifty, Screen screen) {
        // Et appeler lorsque le mot et afficher au début de la partie
        PreGame.nifty = nifty;
        preloaderTxt = screen.findNiftyControl("PreloaderWord", Label.class);
    }

    @Override
    public void onStartScreen() {
        //Si la partie est en cours on affiche le mot à trouver
        if(GameFindWord.partie!=null){
            preloaderTxt.setText(GameFindWord.partie.getMot());
        }
    }

    @Override
    public void onEndScreen() {

    }
}
