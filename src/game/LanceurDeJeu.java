/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xml.sax.SAXException;

/**
 *
 * @author zaettal
 */
public class LanceurDeJeu {
    
    public static void main(String[] args) {
        Dico jeSuisUnePatate = new Dico("xml/dico.xml");
        try {
            jeSuisUnePatate.lireDictionnaireDOM("src/xml/", "dico.xml");
        } catch (SAXException ex) {
            Logger.getLogger(LanceurDeJeu.class.getName()).log(Level.parse("Ici"+ Level.SEVERE), null, ex);
        } catch (IOException ex) {
            Logger.getLogger(LanceurDeJeu.class.getName()).log(Level.parse("là"+ Level.SEVERE), null, ex);
        }
        System.out.println(jeSuisUnePatate.toString());

        // Declare un Jeu
        Game g;
        //Instancie un nouveau jeu
        g = new Game();
        //Execute le jeu
        g.execute();
    }
}
