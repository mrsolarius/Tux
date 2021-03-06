package fr.litopia.tux.game.gui.hud;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import fr.litopia.tux.game.core.GameFindWord;
import fr.litopia.tux.game.utils.Word;

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
            preloaderTxt.setText(Word.normalize(GameFindWord.partie.getMot()));
        }
    }

    @Override
    public void onEndScreen() {

    }
}
