/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import com.jme3.material.Material;
import com.jme3.scene.Spatial;
import com.jme3.shader.VarType;
import env3d.Env;
import env3d.advanced.EnvNode;
import org.lwjgl.input.Keyboard;

/**
 *
 * @author zaettal
 */
public class Tux extends EnvNode {

    private Room room;
    private Env env;

    public Tux(Room room, Env env) {
        this.room = room;
        this.env = env;
        setScale(4.0 * 2);
        setX(room.getWidth() / 2);// positionnement au milieu de la largeur de la room
        setY(getScale() * 1.1); // positionnement en hauteur basé sur la taille de Tux
        setZ(room.getDepth() / 2); // positionnement au milieu de la profondeur de la room
        setTexture("/models/tux/tux_special.png");
        setModel("/models/tux/tux.obj");
    }

    public void déplace() {
        if (env.getKeyDown(Keyboard.KEY_Z) || env.getKeyDown(Keyboard.KEY_UP)) { // Fleche 'haut' ou Z
            // Haut
            if (this.testeRoomCollision(this.getX(), this.getZ() - 1)) {
                this.setRotateY(-180);
                this.setZ(this.getZ() - 1.0);
            }
        }
        if (env.getKeyDown(Keyboard.KEY_Q) || env.getKeyDown(Keyboard.KEY_LEFT)) { // Fleche 'gauche' ou Q
            if (this.testeRoomCollision(this.getX() - 1, this.getZ())) {
                this.setRotateY(-90);
                this.setX(this.getX() - 1.0);
            }
        }
        if (env.getKeyDown(Keyboard.KEY_D) || env.getKeyDown(Keyboard.KEY_RIGHT)) { // Fleche 'gauche' ou Q
            if (this.testeRoomCollision(this.getX() + 1, this.getZ())) {
                this.setRotateY(90);
                this.setX(this.getX() + 1.0);
            }
        }
        if (env.getKeyDown(Keyboard.KEY_S) || env.getKeyDown(Keyboard.KEY_DOWN)) { // Fleche 'gauche' ou Q
            if (this.testeRoomCollision(this.getX(), this.getZ() + 1)) {
                this.setRotateY(360);
                this.setZ(this.getZ() + 1.0);
            }
        }
    }

    public Boolean testeRoomCollision(double x, double z) {
        Boolean val = true;
        if ((x + this.getScale() >= this.room.getWidth()) || (x - this.getScale() <= 0)) {
            val = false;
        }else if ((z + this.getScale() >= this.room.getDepth())||(z - this.getScale() <= 0)) {
            val = false;
        }
        return val;
    }
}
