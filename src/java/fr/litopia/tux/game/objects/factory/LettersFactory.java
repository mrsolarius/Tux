package fr.litopia.tux.game.objects.factory;

import com.jme3.math.FastMath;
import fr.litopia.tux.game.core.GameFindWord;
import fr.litopia.tux.game.utils.Random;

import fr.litopia.tux.game.objects.movable.Letter;
import fr.litopia.tux.game.objects.scene.LetterPlot;
import fr.litopia.tux.game.utils.Word;
import java.util.ArrayList;

public class LettersFactory {
    private final ArrayList<Letter> letters;
    private final ArrayList<LetterPlot> plots;
    private final String word;
    private final GameFindWord context;
    private Boolean isLetterSpawned;

    /**
     * Permet d'inisialiser les lettres et les plots de la partie
     * @param context : GameFindWord
     * @param word : String
     */
    public LettersFactory(GameFindWord context, String word) {
        this.context = context;
        this.word = Word.normalize(word.toLowerCase());
        this.isLetterSpawned = false;
        letters = new ArrayList<>();
        plots = new ArrayList<>();
    }
    /**
     * Permet d'inisialiser les plots de la partie en veillant à ce qu'il reste bien dans la zone de jeu
     */
    public void spawnPlots(){
        //On prévoie une marge de 30 et une taille de plot de 10
        int plotSize = 10;
        //on inisialise la premier position x et z à -x et -z plus la marge et la taille du plot
        float xPos = -context.getRoom().getWidth()+(30+plotSize);
        float zPos = -context.getRoom().getDepth()+(30+plotSize);
        //pour chaque lettre du mot on crée un plot
        for (int i = 0; i < word.length(); i++) {
            //Si la position x est plus grande que la taille de la room on remet la position x à -x et on incrémente la position z
            //pour repositioner le plot suivant à gauche de la salle mais en dessous de la précédente ligne
            //le systeme fonctionne tant que l'on ne remplis pas entirement la salle en z car le cas n'est pas prévue
            if(!(xPos<=context.getRoom().getWidth()-30)) {
                xPos = -context.getRoom().getWidth()+(30+plotSize);
                zPos += plotSize+30;
            }
            //Inisiallisation du plot à la position définie
            plots.add(new LetterPlot(context, word.charAt(i),i, xPos, zPos, plotSize, 270 * FastMath.DEG_TO_RAD));
            //On ajout à la position x la taille et la marge entre les plots
            xPos+=plotSize+30;
        }
    }

    /**
     * Permet de faire spawner les lettres de la partie aléatoirement dans la zone de jeu
     * (Peut tomber sur un plot déjà présent et donc incrémenter ou décrémenter le score : cas non géré)
     */
    public void spawnLetters(){
        if(!isLetterSpawned) {
            for (int i = 0; i < word.length(); i++) {
                Random rx = new Random(-context.getRoom().getWidth()+40, context.getRoom().getWidth() - 40);
                Random rz = new Random(-context.getRoom().getDepth()+40, context.getRoom().getDepth() - 40);
                letters.add(new Letter(context, word.charAt(i), (float) rx.get(), (float) rz.get()));
            }
            isLetterSpawned = true;
        }
    }

    /**
     * permet de savoir si les lettres on spawner
     * @return isLetterSpawned : boolean
     */
    public boolean isLetterSpawned(){
        return isLetterSpawned;
    }

}
