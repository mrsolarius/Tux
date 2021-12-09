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
import fr.litopia.game.core.Jeu;

import static env3d.GameObjectAdapter.assetManager;

/**
 *
 * @author zaettal
 */
public class Letter {
    private char letter;
    private Spatial cube;
    private String id;
    private static int count = 0;
    
    public Letter(Jeu context, char l, float x, float z){
        this.letter = l;
        this.id = "L"+l+count;
        cube = assetManager.loadModel("models/cube/cube.obj");
        Material mat_cube = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        if(l>='a'&& l<='z')
            mat_cube.setTexture("ColorMap",assetManager.loadTexture("/models/letter/"+l+".png"));
        else
            mat_cube.setTexture("ColorMap",assetManager.loadTexture("/models/cube/cube.png"));
        cube.setMaterial(mat_cube);
        cube.setLocalTranslation(x,(float)context.getRoom().getHeight()*2,z);
        cube.scale(4f);
        cube.setName(this.id);
        CollisionShape cubeShapeCollide = CollisionShapeFactory.createBoxShape(cube);
        RigidBodyControl cubeRigidBody = new RigidBodyControl(cubeShapeCollide, 1f);
        cube.addControl(cubeRigidBody);
        context.getRootNode().attachChild(cube);
        context.getBulletAppState().getPhysicsSpace().add(cubeRigidBody);
        count++;
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
}
