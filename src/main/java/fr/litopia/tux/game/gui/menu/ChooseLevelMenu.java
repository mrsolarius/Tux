package fr.litopia.tux.game.gui.menu;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Button;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import fr.litopia.tux.game.core.GameLoop;

public class ChooseLevelMenu implements ScreenController {
    private Nifty nifty;
    private final int MAX_LEVEL = 5;
    private int index;
    private TextField textField;
    private Label playerName;
    private Button prevButton;
    private Button nextButton;

    @Override
    public void bind(Nifty nifty, Screen screen) {
        // Et appeler lorsque lon veut choisir un niveau
        this.nifty = nifty;
        textField = screen.findNiftyControl("playerLoadField", TextField.class);
        textField.setEnabled(false);
        prevButton = screen.findNiftyControl("prevButton", Button.class);
        nextButton = screen.findNiftyControl("nextButton", Button.class);
        playerName = screen.findNiftyControl("labelLoadPlayer", Label.class);

    }

    @Override
    public void onStartScreen() {
        playerName.setText(GameLoop.profil.getName()+" "+GameLoop.profil.getBirthdate());
        this.index = MAX_LEVEL /2;
        this.textField.setText(String.valueOf(index));

        if (index >= MAX_LEVEL) {
            nextButton.setEnabled(false);
        }
        if (index <= 1) {
            prevButton.setEnabled(false);
        }
    }

    @Override
    public void onEndScreen() {

    }

    public void back(){
        nifty.gotoScreen("ProfileMenu");
    }

    public void prevName(){
        if(index >= 1){
            index--;
            this.textField.setText(String.valueOf(index));
            if(index <= 1){
                prevButton.setEnabled(false);
            }
            if(index < MAX_LEVEL){
                nextButton.setEnabled(true);
            }
        }
    }

    public void nextName(){
        if(index < MAX_LEVEL){
            index++;
            this.textField.setText(String.valueOf(index));
            if(index >= MAX_LEVEL){
                nextButton.setEnabled(false);
            }
            if(index>0){
                prevButton.setEnabled(true);
            }
        }
    }
}
