package fr.litopia.game.assets.factory;

import com.jme3.math.FastMath;
import fr.litopia.game.assets.movable.Letter;
import fr.litopia.game.assets.scene.LetterPlot;
import fr.litopia.game.core.Jeu;
import fr.litopia.game.utils.Random;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class LettersFactory {
    private ArrayList<Letter> letters;
    private ArrayList<LetterPlot> plots;
    private String word;
    private Jeu context;
    private Boolean isLetterSpawned;

    public LettersFactory(Jeu context, String word) {
        this.context = context;
        this.word = normalize(word.toLowerCase());
        this.isLetterSpawned = false;
        letters = new ArrayList<Letter>();
        plots = new ArrayList<LetterPlot>();
    }

    public void spawnPlots(){
        int plotSize = 10;
        float xPos = -context.getRoom().getWidth()+(30+plotSize);
        float zPos = -context.getRoom().getDepth()+(30+plotSize);
        for (int i = 0; i < word.length(); i++) {
            if(!(xPos<=context.getRoom().getWidth()-30)) {
                xPos = -context.getRoom().getWidth()+(30+plotSize);
                zPos += plotSize+30;
            }
            plots.add(new LetterPlot(context, word.charAt(i),i, xPos, zPos, plotSize, 270 * FastMath.DEG_TO_RAD));
            xPos+=plotSize+30;
        }
    }

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

    public boolean isLetterSpawned(){
        return isLetterSpawned;
    }
    //Remplacer les caractères accentués par leur équivalent sans accent
    public String normalize(String str) {
        String normalized = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalized).replaceAll("");
    }



}
