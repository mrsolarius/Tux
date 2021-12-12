/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.litopia.tux.game.model;

import com.sun.org.apache.xerces.internal.parsers.DOMParser;

import java.util.ArrayList;
import java.io.IOException;

import fr.litopia.tux.game.utils.Random;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author volatlo
 */
public class Dico {
    private final ArrayList<String> listeNiveau1;
    private final ArrayList<String> listeNiveau2;
    private final ArrayList<String> listeNiveau3;
    private final ArrayList<String> listeNiveau4;
    private final ArrayList<String> listeNiveau5;
    public static String jeSuisUnBackSlashN = "\n";
    public static final String PATH_DICO = "src/res/xml/dico.xml";
    
    public Dico(String cheminFichierDico){
        listeNiveau1 = new ArrayList<>();
        listeNiveau2 = new ArrayList<>();
        listeNiveau3 = new ArrayList<>();
        listeNiveau4 = new ArrayList<>();
        listeNiveau5 = new ArrayList<>();
    }
    
    private String getMotDepuisListe(ArrayList<String> list){
        Random r = new Random(0,list.size()-1);
        return list.get(Math.round((float)r.get()));
    }
    
    private int checkNiveau(int niveau){
        int val;
        if(niveau >= 1 && niveau<=5) {
            val=niveau;
        }else{
            val=1;
        }
        return val;
    }
    
   public String getMotDepuisListeNiveau(int niveau){
       String word = "";
       Random r;
       switch(checkNiveau(niveau)){
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
       switch(checkNiveau(niveau)){
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
   
   public void lireDictionnaireDOM() throws SAXException, IOException{
       // crée un parser de type DOM
        DOMParser parser = new DOMParser();
        // parse le document XML correspondant au fichier filename dans le chemin path
        parser.parse(PATH_DICO);
        // récupère l"instance de document
        Document doc = parser.getDocument();
        // récupère la liste des éléments nommés tr:pos
        NodeList posList = doc.getChildNodes();
        NodeList elements = posList.item(0).getChildNodes();

        for (int i = 0; i < elements.getLength();i++) {
            Node node = elements.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                char rapport = ((Element) node).getAttribute("niveau").charAt(0);
                switch(rapport){
                    case '1':
                        listeNiveau1.add(node.getTextContent());
                        break;
                    case '2':
                        listeNiveau2.add(node.getTextContent());
                        break;
                    case '3':
                        listeNiveau3.add(node.getTextContent());
                        break;
                    case '4':
                        listeNiveau4.add(node.getTextContent());
                        break;
                    case '5':
                        listeNiveau5.add(node.getTextContent());
                        break;
                }
            }
        }
   }

   public String appelCoursier(ArrayList<String> arr){
       StringBuilder coursier = new StringBuilder();
       for (String courier : arr) {
           coursier.append(courier);
           coursier.append(jeSuisUnBackSlashN);
       }
       return coursier.toString();
   }
   
    @Override
    public String toString() {
        String leDesert = "--- Le Desert de la Premier Liste ---";
        leDesert+=jeSuisUnBackSlashN;
        leDesert+= appelCoursier(listeNiveau1);
        leDesert+=jeSuisUnBackSlashN;
        leDesert+= "--- Le Desert de la Deuxieme Liste ---";
        leDesert+=jeSuisUnBackSlashN;
        leDesert += appelCoursier(listeNiveau2);
        leDesert+= "--- Le Desert de la Troisieme Liste ---";
        leDesert+=jeSuisUnBackSlashN;
        leDesert += appelCoursier(listeNiveau3);
        leDesert+= "--- Le Desert de la Quatriemme Liste ---";
        leDesert+=jeSuisUnBackSlashN;
        leDesert += appelCoursier(listeNiveau4);
        leDesert+= "--- Le Desert de la Cinquieme Liste ---";
        leDesert+=jeSuisUnBackSlashN;
        leDesert += appelCoursier(listeNiveau5);
        return leDesert; //To change body of generated methods, choose Tools | Templates.
    }
}
