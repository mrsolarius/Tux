package fr.litopia.game.gui.menu;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class NewPlayerMenu implements ScreenController {
    private Nifty nifty;
    private TextField playerName;

    @Override
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        playerName = screen.findNiftyControl("playerNewField", TextField.class);
        playerName.setEnabled(true);
        //@todo : verifier que le nom n'est pas deja pris et que le nom n'est pas vide (sinon afficher un message d'erreur)
        playerName.setText("Pseudonyme");
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
}
