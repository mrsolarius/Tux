package fr.litopia.game.assets.listeners;

import fr.litopia.game.assets.scene.LetterPlot;

public interface LettersListener {
    //Appeler lorsqu'une lettre touche un plot
    void updateLetter(LetterPlot lettrePlot);
}
