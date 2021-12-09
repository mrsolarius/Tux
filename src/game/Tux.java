/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.input.ChaseCamera;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.shader.VarType;
import env3d.Env;
import env3d.advanced.EnvNode;
import org.lwjgl.input.Keyboard;

import static com.jme3.scene.Spatial.BatchHint.Always;
import static env3d.GameObjectAdapter.assetManager;

/**
 *
 * @author zaettal
 */
public class Tux implements ActionListener, PhysicsCollisionListener {
    private Jeu context;
    private Node tuxNode;
    private BetterCharacterControl tux;
    private Geometry tuxModel;
    private ChaseCamera chaseCam;
    private boolean left = false, right = false, up = false, down = false, lastLeft = false, lastRight = false, lastUp = false, lastDown = false;
    private Vector3f walkDirection = new Vector3f();
    private Vector3f tempDirVector = new Vector3f();
    private Vector3f tempRotateVector = new Vector3f();
    private Vector3f camDir = new Vector3f();
    private Vector3f camLeft = new Vector3f();

    // Our movement speed
    private float speed;
    private float strafeSpeed;
    private float headHeight;

    public Tux(Jeu context) {
        // set player speed
        speed = 6f;
        strafeSpeed = 4f;
        headHeight = 3f;
        this.context = context;

        tuxNode = new Node("Tux");

        tuxModel = (Geometry) assetManager.loadModel("models/tux/tux.obj");
        Material mat_tux = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat_tux.setTexture("ColorMap",assetManager.loadTexture("models/tux/tux.png"));
        tuxModel.setMaterial(mat_tux);
        tuxModel.setLocalScale(5f);
        tuxModel.setLocalTranslation(new Vector3f(0,tuxModel.getLocalScale().y,0));
        tuxNode.attachChild(tuxModel);

        // Inisialisation de la phisique de Tux (CharacterControl)
        tux = new BetterCharacterControl(tuxModel.getLocalScale().x-1,(tuxModel.getLocalScale().y-1)*2,1f);
        tux.setJumpForce(new Vector3f(0,15f,0));
        tux.setGravity(new Vector3f(0, 10f ,0));
        tux.warp(new Vector3f(0,0,0));

        // On ajoute le CharacterControl au Node
        tuxNode.addControl(tux);
        context.getBulletAppState().setDebugEnabled(true);
        context.getBulletAppState().getPhysicsSpace().add(tuxNode);
        context.getRootNode().attachChild(tuxNode);


        chaseCam = new ChaseCamera(context.getCamera(), tuxModel, context.getInputManager());
        chaseCam.setDragToRotate(false);
        chaseCam.setMinDistance(5f);
        chaseCam.setMaxDistance(80f);
        chaseCam.setDefaultDistance(20f);
        chaseCam.setDefaultHorizontalRotation(0f);
        chaseCam.setMaxVerticalRotation(0.6f);
        chaseCam.setMinVerticalRotation(0);
        chaseCam.setSmoothMotion(true);

        setUpKeys();
        setUpPhysicsListeners();
    }
    private void setUpKeys() {
        context.getInputManager().addMapping("Left", new KeyTrigger(KeyInput.KEY_Q));
        context.getInputManager().addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
        context.getInputManager().addMapping("Up", new KeyTrigger(KeyInput.KEY_Z));
        context.getInputManager().addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
        context.getInputManager().addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));
        context.getInputManager().addMapping("Left", new KeyTrigger(KeyInput.KEY_LEFT));
        context.getInputManager().addMapping("Right", new KeyTrigger(KeyInput.KEY_RIGHT));
        context.getInputManager().addMapping("Up", new KeyTrigger(KeyInput.KEY_UP));
        context.getInputManager().addMapping("Down", new KeyTrigger(KeyInput.KEY_DOWN));
        context.getInputManager().addMapping("Jump", new KeyTrigger(KeyInput.KEY_NUMPAD0));
        context.getInputManager().addListener(this, "Left");
        context.getInputManager().addListener(this, "Right");
        context.getInputManager().addListener(this, "Up");
        context.getInputManager().addListener(this, "Down");
        context.getInputManager().addListener(this, "Jump");
    }

    private void setUpPhysicsListeners() {
        tux.getPhysicsSpace().addCollisionListener(this);
    }

    @Override
    public void onAction(String binding, boolean isPressed, float tpf) {
        if (binding.equals("Left")) {
            left = isPressed;
        } else if (binding.equals("Right")) {
            right= isPressed;
        } else if (binding.equals("Up")) {
            up = isPressed;
        } else if (binding.equals("Down")) {
            down = isPressed;
        } else if (binding.equals("Jump")) {
            if (isPressed) {
                tux.jump();
            }
        }
    }

    public void simpleUpdate() {
        camDir.set(context.getCamera().getDirection()).multLocal(speed, 0.0f, speed);
        camLeft.set(context.getCamera().getLeft()).multLocal(strafeSpeed);
        walkDirection.set(0, 0, 0);
        if (left) {
            walkDirection.addLocal(camLeft);
        }
        if (right) {
            walkDirection.addLocal(camLeft.negate());
        }
        if (up) {
            walkDirection.addLocal(camDir);
        }
        if (down) {
            walkDirection.addLocal(camDir.negate());
        }
        tux.setWalkDirection(walkDirection);
        tux.setViewDirection(camDir);
    }

    private void updateTempVector(){
        tempRotateVector.set(tux.getViewDirection()).multLocal(0.1f);
        tempDirVector.set(tux.getViewDirection()).multLocal(0.1f);
    }

    private void restLastVal(){
        lastLeft = false; lastRight = false; lastUp = false; lastDown = false;
    }

    public float getX(){
        return tuxModel.getLocalTranslation().x;
    }

    public float getY(){
        return tuxModel.getLocalTranslation().y;
    }

    public float getZ(){
        return tuxModel.getLocalTranslation().z;
    }

    public float getScale(){
        return tuxModel.getLocalScale().getY();
    }

    @Override
    public void collision(PhysicsCollisionEvent physicsCollisionEvent) {
        System.out.println("collision: A:" + physicsCollisionEvent.getNodeA().getName() + " B:" + physicsCollisionEvent.getNodeB().getName());
    }

    public Geometry getSpatial() {
        return tuxModel;
    }
}
