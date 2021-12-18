package fr.litopia.tux.game.gui.hud;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import fr.litopia.tux.game.core.GameFindWord;

public class EndGame implements ScreenController {
    private Label message;
    private Label score;
    private Label time;

    @Override
    public void bind(Nifty nifty, Screen screen) {
        // HUD appeler lors de la fin de partie (gagne ou perdu)
        message = screen.findNiftyControl("EndGameMessage", Label.class);
        score = screen.findNiftyControl("EndGameScore", Label.class);
        time = screen.findNiftyControl("EndGameTemp", Label.class);
    }

    @Override
    public void onStartScreen() {
        // Récupération des scores et affichage dans le HUD en fonction du résultat
        if(GameFindWord.partie.getTrouve()!=100){
            message.setText("Perdu !");
            score.setText("Score : "+GameFindWord.partie.getScore());
            time.setText("Trouver : "+GameFindWord.partie.getTrouve()+"%");
        }else{
            message.setText("Gagne !");
            score.setText("Score : "+GameFindWord.partie.getScore());
            time.setText("Temps : "+((int)GameFindWord.partie.getTemps())+" sec");
        }
    }

    @Override
    public void onEndScreen() {

    }
}
