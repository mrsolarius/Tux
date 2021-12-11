package fr.litopia.game.gui.menu;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Button;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import fr.litopia.game.model.Profil;
import java.util.ArrayList;

public class LoadPlayerMenu implements ScreenController {
    private Nifty nifty;
    private ArrayList<String> playerNames;
    private String playerName;
    private int index;
    private TextField textField;
    private Button prevButton;
    private Button nextButton;
    private Button loadPlayerButton;

    @Override
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        TextField textField = screen.findNiftyControl("playerLoadField", TextField.class);
        textField.setEnabled(false);
        textField.setText("");
        this.textField = screen.findNiftyControl("playerLoadField", TextField.class);
        this.prevButton = screen.findNiftyControl("prevButton", Button.class);
        this.nextButton = screen.findNiftyControl("nextButton", Button.class);
        this.loadPlayerButton = screen.findNiftyControl("LoadPlayerButton", Button.class);
    }

    @Override
    public void onStartScreen() {
        this.playerNames = Profil.getAllProfileName();
        if(playerNames.size() == 0){
            loadPlayerButton.setEnabled(false);
            nextButton.setEnabled(false);
            prevButton.setEnabled(false);
            loadPlayerButton.setText("Aucun joueur crée");
        }
        else {
            this.index = playerNames.size() / 2;
            this.playerName = playerNames.get(index);
            this.textField.setText(playerName);

            if (index >= playerNames.size()-1) {
                nextButton.setEnabled(false);
            }
            if (index <= 0) {
                prevButton.setEnabled(false);
            }
        }
    }

    @Override
    public void onEndScreen() {

    }

    public void back(){
        nifty.gotoScreen("ProfileMenu");
    }

    public void prevName(){
        if(index >= 0){
            index--;
            playerName = playerNames.get(index);
            textField.setText(playerName);
            if(index <= 0){
                prevButton.setEnabled(false);
            }
            if(index < playerNames.size()-1){
                nextButton.setEnabled(true);
            }
        }
    }

    public void nextName(){
        if(index < playerNames.size()-1){
            index++;
            playerName = playerNames.get(index);
            textField.setText(playerName);
            if(index >= playerNames.size()-1){
                nextButton.setEnabled(false);
            }
            if(index>0){
                prevButton.setEnabled(true);
            }
        }
    }
}
