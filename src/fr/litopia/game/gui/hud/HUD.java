package fr.litopia.game.gui.hud;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import fr.litopia.game.core.GameFindWord;
import fr.litopia.game.core.GameState;

public class HUD implements ScreenController {
    private static boolean isVisible = false;
    private static Nifty nifty;
    private static Label scoreLbs;
    private static Label timeLbs;
    private static Label percentLbs;

    @Override
    public void bind(Nifty nifty1, Screen screen) {
        // HUD appeler lorsque la partie à commencé
        nifty = nifty1;
        scoreLbs = screen.findNiftyControl("HUD_Score", Label.class);
        timeLbs = screen.findNiftyControl("HUD_Time", Label.class);
        percentLbs = screen.findNiftyControl("HUD_Percent", Label.class);
        isVisible = true;
        updateHUD();
        isVisible = false;
    }

    @Override
    public void onStartScreen() {
        isVisible = true;
    }

    @Override
    public void onEndScreen() {
        isVisible = false;
    }

    /**
     * Update the HUD
     * Permet de mettre à jour le HUD avec les informations de la partie
     * Doit être appeler à chaque update de la partie
     */
    public static void updateHUD() {
        // Si le HUD est visible et que la partie est en cours
        if (isVisible && GameFindWord.partie != null) {
            scoreLbs.setText("Score : " + GameFindWord.partie.getScore());
            timeLbs.setText("Time : " + (GameFindWord.partie.getMaxTime()-GameFindWord.partie.getTemps())+" sec");
            percentLbs.setText("Found : " + GameFindWord.partie.getTrouve()+" %");
            nifty.update();
        }
    }
}
