package fr.litopia.game.core;

import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;
import fr.litopia.game.assets.scene.LetterPlot;
import fr.litopia.game.assets.listeners.LettersListener;
import fr.litopia.game.assets.factory.LettersFactory;
import fr.litopia.game.model.Partie;

import java.util.ArrayList;

public class JeuDevineLeMotOrdre extends Jeu implements LettersListener {
    private ArrayList<Integer> letterFound;
    private LettersFactory lettersFactory;
    private int chrono;
    private int score;
    private BitmapText chronoText;
    private BitmapText scoreText;

    //private Chronomètre chrono;

    public JeuDevineLeMotOrdre() {
        super();
        letterFound = new ArrayList<>();
    }


    @Override
    protected void démarrePartie(Partie partie) {
        chronoText = new BitmapText(guiFont, false);
        chronoText.setSize(60);
        chronoText.setColor(ColorRGBA.Red);
        chronoText.setText("Chrono:"+chrono);
        chronoText.setLocalTranslation(0, chronoText.getLineHeight()+100, 0);
        guiNode.attachChild(chronoText);

        scoreText = new BitmapText(guiFont, false);
        scoreText.setSize(60);
        scoreText.setColor(ColorRGBA.Red);
        scoreText.setText("Score:"+score);
        scoreText.setLocalTranslation(0, scoreText.getHeight()+200,0);
        guiNode.attachChild(scoreText);

        lettersFactory = new LettersFactory(this, partie.getMot());
        lettersFactory.spawnPlots();
    }

    @Override
    protected void appliqueRegles(Partie partie) {
        getBulletAppState().setDebugEnabled(true);
        if (chrono >= 500){
            if (!lettersFactory.isLetterSpawned())
                lettersFactory.spawnLetters();
        }
        if((chrono>=(2000+(partie.getMot().length()*500+partie.getNiveau()*1000)))||
            letterFound.size()==partie.getMot().length()){
            terminePartie(partie);
        }
        chronoText.setText("Chrono:"+chrono);
        scoreText.setText("Score:"+score);
        chrono++;
        //System.out.println(chrono);
    }

    @Override
    protected void terminePartie(Partie partie) {
        partie.setTemps(chrono);
        partie.setScore(score);
        lettersFactory.removeAllLetters();
        lettersFactory.removeAllPlots();
    }

    @Override
    public void updateLetter(LetterPlot lettrePlot) {
        if(lettrePlot.isCorrect()){
            if(!letterFound.contains(lettrePlot.getWordPosition())){
                letterFound.add(lettrePlot.getWordPosition());
                score+=100;
            }else{
                score-=10;
            }
        }else{
            score-=5;
            if(letterFound.contains(lettrePlot.getWordPosition())) {
                try {
                    letterFound.remove(lettrePlot.getWordPosition());
                }   catch (Exception e){
                    System.out.println("Erreur");
                    e.printStackTrace();
                }
            }
        }
    }
}
