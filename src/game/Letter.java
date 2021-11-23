/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import env3d.advanced.EnvNode;

/**
 *
 * @author zaettal
 */
public class Letter extends EnvNode{
    private char letter;
    
    public Letter(char l, double x, double z){
        this.letter = l;
        setScale(4);
        setX(x);// positionnement au milieu de la largeur de la room
        setY(getScale() * 1.1); // positionnement en hauteur basé sur la taille de Tux
        setZ(z); // positionnement au milieu de la profondeur de la room
        setTexture("/models/letter/"+l+".png");
        setModel("/models/letter/cube.obj");
    }
}
