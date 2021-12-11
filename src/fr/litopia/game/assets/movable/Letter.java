/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.litopia.game.assets.movable;

import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.material.Material;
import com.jme3.scene.Spatial;
import fr.litopia.game.core.GameFindWord;

/**
 *
 * @author zaettal
 */
public class Letter {
    private GameFindWord context;
    private char letter;
    private Spatial cube;
    private RigidBodyControl physics;
    private String id;
    private static int count = 0;
    
    public Letter(GameFindWord context, char l, float x, float z){
        this.context = context;
        this.letter = l;
        this.id = "L"+l+count;
        cube = context.getAssetManager().loadModel("models/cube/cube.obj");
        Material mat_cube = new Material(context.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        if(l>='a'&& l<='z')
            mat_cube.setTexture("ColorMap",context.getAssetManager().loadTexture("/models/cube/"+l+".png"));
        else
            mat_cube.setTexture("ColorMap",context.getAssetManager().loadTexture("/models/cube/cube.png"));
        cube.setMaterial(mat_cube);
        cube.setLocalTranslation(x,(float)context.getRoom().getHeight()*2,z);
        cube.scale(4f);
        cube.setName(this.id);
        CollisionShape cubeShapeCollide = CollisionShapeFactory.createBoxShape(cube);
        physics = new RigidBodyControl(cubeShapeCollide, 1f);
        cube.addControl(physics);
        context.getRootNode().attachChild(cube);
        context.getBulletAppState().getPhysicsSpace().add(physics);
        count++;
    }

    public static void resetCount(){
        count = 0;
    }

    public char getLetter(){
        return letter;
    }

    public float getX(){
        return cube.getLocalTranslation().getX();
    }

    public float getZ(){
        return cube.getLocalTranslation().getZ();
    }

    public float getY(){
        return cube.getLocalTranslation().getY();
    }

    public Spatial getCube() {
        return cube;
    }

    public void remove() {
        cube.removeFromParent();
    }
}
