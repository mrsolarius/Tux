/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import com.jme3.bounding.BoundingVolume;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.collision.Collidable;
import com.jme3.collision.CollisionResults;
import com.jme3.collision.UnsupportedCollisionException;
import com.jme3.material.Material;
import com.jme3.scene.SceneGraphVisitor;
import com.jme3.scene.Spatial;
import env3d.advanced.EnvNode;

import java.util.Queue;

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
        cube.setLocalTranslation(x,40,z);
        RigidBodyControl cubeBody = new RigidBodyControl( 10);
        cube.setLocalScale(2f,2f,2f);
        cube.setName(this.id);
        cube.addControl(cubeBody);
        context.getRootNode().attachChild(cube);
        context.getBulletAppState().getPhysicsSpace().add(cubeBody);
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
