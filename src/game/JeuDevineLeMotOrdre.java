package game;

public class JeuDevineLeMotOrdre extends Jeu {
    private int nbLettresRestantes;
    //private Chronomètre chrono;

    public JeuDevineLeMotOrdre() {
        super();
        //this.chrono = new Chronomètre();
    }

    @Override
    protected void démarrePartie(Partie partie) {
    }

    @Override
    protected void appliqueRegles(Partie partie) {

    }

    @Override
    protected void terminePartie(Partie partie) {

    }

    private boolean tuxTrouveLettre() {
        return super.collision(super.getLettres().get(super.getLettres().size()-nbLettresRestantes-1));
    }
}
