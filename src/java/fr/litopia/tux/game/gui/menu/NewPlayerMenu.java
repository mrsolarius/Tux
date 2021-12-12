package fr.litopia.tux.game.gui.menu;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public class NewPlayerMenu implements ScreenController {
    private Nifty nifty;

    @Override
    public void bind(Nifty nifty, Screen screen) {
        //appeler lors de la création d'un nouveau joueur
        this.nifty = nifty;
        TextField playerName = screen.findNiftyControl("playerNewField", TextField.class);
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
