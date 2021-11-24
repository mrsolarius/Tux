/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import java.util.ArrayList;
import game.Random;

/**
 *
 * @author volatlo
 */
public class Dico {
    private ArrayList<String> listeNiveau1;
    private ArrayList<String> listeNiveau2;
    private ArrayList<String> listeNiveau3;
    private ArrayList<String> listeNiveau4;
    private ArrayList<String> listeNiveau5;
    private String cheminFichierDico;
    
    public Dico(String cheminFichierDico){
        this.cheminFichierDico = cheminFichierDico;
        listeNiveau1 = new ArrayList<String>();
        listeNiveau2 = new ArrayList<String>();
        listeNiveau3 = new ArrayList<String>();
        listeNiveau4 = new ArrayList<String>();
        listeNiveau5 = new ArrayList<String>();
    }
    
    private String getMotDepuisListe(ArrayList<String> list){
        Random r = new Random(0,listeNiveau1.size()-1);
        return listeNiveau1.get(Math.round((float)r.get()));  
    }
    
    private int vérifieNiveau(int niveau){
        int val = 1;
        if(niveau >= 1 && niveau<=5) {
            val=niveau;
        }
        return val;
    }
    
   public String getMotDepuisListeNiveau(int niveau) throws Exception{
       String word = "";
       Random r;
       switch(vérifieNiveau(niveau)){
           case 1:
               word = getMotDepuisListe(listeNiveau1);
               break;
           case 2:
               word = getMotDepuisListe(listeNiveau2);
               break;
           case 3:
               word = getMotDepuisListe(listeNiveau3);
               break;
           case 4:
               word = getMotDepuisListe(listeNiveau4);
               break;
            case 5:
                word = getMotDepuisListe(listeNiveau5);
                break;
            default:
                
       }
        return word;
   }
   
   public void ajouteMotADico(int niveau, String mot){
       switch(vérifieNiveau(niveau)){
           case 1:
               listeNiveau1.add(mot);
               break;
           case 2:
               listeNiveau2.add(mot);
               break;
           case 3:
               listeNiveau3.add(mot);
               break;
           case 4:
               listeNiveau4.add(mot);
               break;
            case 5:
               listeNiveau5.add(mot);
                break;
            default:
                
       }
   }
}
