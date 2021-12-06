package test;

import game.Dico;
import game.LanceurDeJeu;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TestDico {
    public static void main(String[] args) {
        Dico dico = new Dico("xml/dico.xml");
        try {
            dico.lireDictionnaireDOM("src/xml/", "dico.xml");
        } catch (SAXException ex) {
            Logger.getLogger(LanceurDeJeu.class.getName()).log(Level.parse("Ici"+ Level.SEVERE), null, ex);
        } catch (IOException ex) {
            Logger.getLogger(LanceurDeJeu.class.getName()).log(Level.parse("là"+ Level.SEVERE), null, ex);
        }
        System.out.println(dico.toString());
    }
}
