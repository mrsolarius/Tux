package fr.litopia.tux.game.objects.listeners;

import fr.litopia.tux.game.objects.scene.LetterPlot;

public interface LettersListener {
    //Appeler lorsqu'une lettre touche un plot
    void updateLetter(LetterPlot lettrePlot);
}
