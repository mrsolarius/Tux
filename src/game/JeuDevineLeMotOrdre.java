package game;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JeuDevineLeMotOrdre extends Jeu {
    private int nbLettresRestantes;
    private LettersManager lettersManager;
    private Dico dico;
    private int chrono;

    //private Chronomètre chrono;

    public JeuDevineLeMotOrdre() {
        super();
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
        lettersManager = new LettersManager(this, dico.getMotDepuisListeNiveau(4));
        lettersManager.spawnPlots();
    }

    @Override
    protected void appliqueRegles(Partie partie) {
        if(chrono>=500)
            if(!lettersManager.isLetterSpawned())
                lettersManager.spawnLetters();
        if(chrono>=1000000){
            chrono=0;
        }
        chrono++;
        //System.out.println(chrono);
    }

    @Override
    protected void terminePartie(Partie partie) {

    }

    private boolean tuxTrouveLettre() {
        return super.collision(super.getLettres().get(super.getLettres().size()-nbLettresRestantes-1));
    }
}
