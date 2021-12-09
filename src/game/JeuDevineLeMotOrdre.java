package game;

import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JeuDevineLeMotOrdre extends Jeu implements LettersListener {
    private ArrayList<Integer> letterFound;
    private LettersManager lettersManager;
    private Dico dico;
    private int chrono;
    private int score;
    private BitmapText chronoText;
    private BitmapText scoreText;

    //private Chronomètre chrono;

    public JeuDevineLeMotOrdre() {
        super();
        letterFound = new ArrayList<>();
        dico = new Dico("xml/dico.xml");
        try {
            dico.lireDictionnaireDOM("src/xml/", "dico.xml");
        } catch (SAXException ex) {
            Logger.getLogger(LanceurDeJeu.class.getName()).log(Level.parse("Ici"+ Level.SEVERE), null, ex);
        } catch (IOException ex) {
            Logger.getLogger(LanceurDeJeu.class.getName()).log(Level.parse("là"+ Level.SEVERE), null, ex);
        }
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

        lettersManager = new LettersManager(this, dico.getMotDepuisListeNiveau(1));
        lettersManager.spawnPlots();
    }

    @Override
    protected void appliqueRegles(Partie partie) {
        if(chrono>=500)
            if(!lettersManager.isLetterSpawned())
                lettersManager.spawnLetters();
        if(chrono>=50000){
            chrono=0;
        }
        chronoText.setText("Chrono:"+chrono);
        scoreText.setText("Score:"+score);
        chrono++;
        //System.out.println(chrono);
    }

    @Override
    protected void terminePartie(Partie partie) {

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
