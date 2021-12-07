/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.jme3.shader.VarType;
import env3d.Env;
import env3d.advanced.EnvNode;
import org.lwjgl.input.Keyboard;

import static env3d.GameObjectAdapter.assetManager;

/**
 *
 * @author zaettal
 */
public class Tux implements ActionListener {
    private Jeu context;
    private CharacterControl tux;
    private Spatial tuxModel;
    private boolean left = false, right = false, up = false, down = false, lastLeft = false, lastRight = false, lastUp = false, lastDown = false;
    private Vector3f walkDirection = new Vector3f();
    private Vector3f tempDirVector = new Vector3f();
    private Vector3f tempRotateVector = new Vector3f();

    public Tux(Jeu context) {
        this.context = context;

        Spatial tuxModel = assetManager.loadModel("models/tux/tux.obj");
        Material mat_tux = new Material(
                assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat_tux.setTexture("ColorMap",assetManager.loadTexture("models/tux/tux.png"));
        tuxModel.setMaterial(mat_tux);
        tuxModel.setLocalScale(1.5f);
        tuxModel.setLocalTranslation(0, 100, 0);

        CapsuleCollisionShape capsuleShape = new CapsuleCollisionShape(tuxModel.getLocalScale().getX(), tuxModel.getLocalScale().length()-2, 1);
        tux = new CharacterControl(capsuleShape, 5);
        tux.setJumpSpeed(20);
        tux.setFallSpeed(30);

        tuxModel.addControl(tux);

        context.getRootNode().attachChild(tuxModel);
        context.getBulletAppState().getPhysicsSpace().add(tux);

        tux.setGravity(30f);
        tux.setPhysicsLocation(new Vector3f(50, 10, 50));
        setUpKeys();
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
        context.getInputManager().addListener(this, "Left");
        context.getInputManager().addListener(this, "Right");
        context.getInputManager().addListener(this, "Up");
        context.getInputManager().addListener(this, "Down");
        context.getInputManager().addListener(this, "Jump");
    }

    @Override
    public void onAction(String binding, boolean isPressed, float tpf) {
        System.out.println(tux.getPhysicsLocation());
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

    public void simpleUpdate(){
        walkDirection.set(0, 0, 0);
        if (left) {
            if(!lastLeft)
                tux.setViewDirection(new Vector3f(-1,0,0));
            restLastVal();
            lastLeft=true;
        }
        if (right) {
            if(!lastRight)
                tux.setViewDirection(new Vector3f(1,0,0));
            restLastVal();
            lastRight=true;
        }
        if (down) {
            if(!lastUp)
                tux.setViewDirection(new Vector3f(0,0,1));
            restLastVal();
            lastUp=true;
        }
        if (up) {
            if(!lastDown)
                tux.setViewDirection(new Vector3f(0,0,-1));
            restLastVal();
            lastDown=true;
        }
        if(left||right||up||down) {
            updateTempVector();
            walkDirection.addLocal(tempDirVector);
        }
        tux.setWalkDirection(walkDirection);
    }

    private void updateTempVector(){
        tempRotateVector.set(tux.getViewDirection()).multLocal(0.1f);
        tempDirVector.set(tux.getViewDirection()).multLocal(0.1f);
    }

    private void restLastVal(){
        lastLeft = false; lastRight = false; lastUp = false; lastDown = false;
    }

    public float getX(){
        return tux.getPhysicsLocation().getX();
    }

    public float getY(){
        return tux.getPhysicsLocation().getY();
    }

    public float getZ(){
        return tux.getPhysicsLocation().getZ();
    }

    public float getScale(){
        return tuxModel.getLocalScale().getY();
    }
}
