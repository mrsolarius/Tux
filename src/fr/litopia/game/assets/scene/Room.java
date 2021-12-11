/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.litopia.game.assets.scene;

import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import fr.litopia.game.core.GameFindWord;

/**
 *
 * @author zaettal
 */
public class Room {
    private Spatial floor;
    private Spatial wallNorth;
    private Spatial wallEast;
    private Spatial wallSouth;
    private Spatial wallWest;
    private int depth = 200;
    private int height = 200;
    private int width = 200;
    private final GameFindWord context;

    public Room(GameFindWord context, int width, int depth, int height, String textureBottom, String textureNorth, String textureEast, String textureSouth, String textureWest) {
        //Definition de la taille de la room
        this.width = width;
        this.depth = depth;
        this.height = height;
        this.context = context;
        //definition du sol
        Box boxFloor = new Box(width, 10, depth);
        floor = new Geometry("Floor", boxFloor);
        floor.setLocalTranslation(0, -boxFloor.yExtent, 0);
        floor.setMaterial(generateMaterial(textureBottom));
        CollisionShape sceneShapeFloor = CollisionShapeFactory.createMeshShape(floor);
        floor.addControl(new RigidBodyControl(sceneShapeFloor, 0));
        context.getRootNode().attachChild(floor);
        context.getBulletAppState().getPhysicsSpace().add(floor);

        //definition des murs
        Box boxNS = new Box(width, height, 10);
        Box boxEW = new Box(10, height, width);


        final Material mat2 = new Material(context.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat2.setTransparent(true);
        mat2.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        mat2.setColor("Color", new ColorRGBA(0, 1, 0, 0.01f));

        wallNorth = new Geometry("WallNorth", boxNS);
        wallNorth.setLocalTranslation(10, height, -depth);
        wallNorth.setMaterial(mat2);
        CollisionShape sceneShapeNorth = CollisionShapeFactory.createBoxShape(wallNorth);
        wallNorth.addControl(new RigidBodyControl(sceneShapeNorth, 0));
        //context.getRootNode().attachChild(wallNorth);
        context.getBulletAppState().getPhysicsSpace().add(wallNorth);

        wallEast = new Geometry("WallEast", boxEW);
        wallEast.setLocalTranslation(width, height, 10);
        wallEast.setMaterial(mat2);
        CollisionShape sceneShapeEast = CollisionShapeFactory.createBoxShape(wallEast);
        wallEast.addControl(new RigidBodyControl(sceneShapeEast, 0));
        //context.getRootNode().attachChild(wallEast);
        context.getBulletAppState().getPhysicsSpace().add(wallEast);


        wallSouth = new Geometry("WallSouth", boxNS);
        wallSouth.setLocalTranslation(10, height, depth);
        wallSouth.setMaterial(mat2);
        CollisionShape sceneShape = CollisionShapeFactory.createBoxShape(wallSouth);
        wallSouth.addControl(new RigidBodyControl(sceneShape, 0));
        //context.getRootNode().attachChild(wallSouth);
        context.getBulletAppState().getPhysicsSpace().add(wallSouth);

        wallWest = new Geometry("WallWest", boxEW);
        wallWest.setLocalTranslation(-width, height, 10);
        wallWest.setMaterial(mat2);
        CollisionShape sceneShapeWest = CollisionShapeFactory.createBoxShape(wallWest);
        wallWest.addControl(new RigidBodyControl(sceneShapeWest, 0));
        //context.getRootNode().attachChild(wallWest);
        context.getBulletAppState().getPhysicsSpace().add(wallWest);
    }

    private Material generateMaterial(String texture) {
        Material material = new Material(context.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        material.setTexture("ColorMap",context.getAssetManager().loadTexture(texture));
        return material;
    }

    public int getDepth() {
        return depth;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}
