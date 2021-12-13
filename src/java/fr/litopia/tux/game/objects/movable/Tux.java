/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.litopia.tux.game.objects.movable;

import com.jme3.audio.AudioNode;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.input.ChaseCamera;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import fr.litopia.tux.game.core.GameFindWord;

/**
 *
 * @author zaettal
 */
public class Tux implements ActionListener {
    //Le contexte
    private final GameFindWord context;

    private final BetterCharacterControl tux;

    //Nos vectors pour les mouvements et la caméra
    private final Vector3f walkDirection = new Vector3f();
    private final Vector3f camLongitudinal = new Vector3f();
    private final Vector3f camLateral = new Vector3f();

    // variable permettant de savoir si on est en train de marcher et dans quelle direction
    private boolean left = false, right = false, up = false, down = false, run = false;

    // nos vitesses de déplacement
    private final float speed;
    private final float strafeSpeed;
    private final float runMultiplier;

    private final AudioNode jump;

    public Tux(GameFindWord context) {
        // Definition de la vitesses de deplacement
        speed = 6f;
        strafeSpeed = 4f;
        runMultiplier = 4f;
        this.context = context;

        // On cree le tux
        //Tux dans le monde
        Node tuxNode = new Node("Tux");

        // On charge le model
        Geometry tuxModel = (Geometry) context.getAssetManager().loadModel("assets/models/tux/tux.obj");
        Material mat_tux = new Material(context.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat_tux.setTexture("ColorMap",context.getAssetManager().loadTexture("assets/models/tux/tux.png"));
        tuxModel.setMaterial(mat_tux);
        tuxModel.setLocalScale(5f);
        tuxModel.setLocalTranslation(new Vector3f(0, tuxModel.getLocalScale().y,0));
        tuxNode.attachChild(tuxModel);

        // Inisialisation de la phisique de Tux (CharacterControl)
        tux = new BetterCharacterControl(tuxModel.getLocalScale().x-1,(tuxModel.getLocalScale().y-1)*2,1f);
        tux.setJumpForce(new Vector3f(0,15f,0));
        tux.setGravity(new Vector3f(0, 10f ,0));
        tux.warp(new Vector3f(0,0,0));

        // On ajoute le CharacterControl au Node
        tuxNode.addControl(tux);
        context.getBulletAppState().getPhysicsSpace().add(tuxNode);
        context.getRootNode().attachChild(tuxNode);

        // On initialise la caméra de Tux (ChaseCamera)
        //Camera de tux
        ChaseCamera chaseCam = new ChaseCamera(context.getCamera(), tuxModel, context.getInputManager());
        chaseCam.setDragToRotate(false);
        chaseCam.setMinDistance(5f);
        chaseCam.setMaxDistance(100f);
        chaseCam.setDefaultDistance(40f);
        chaseCam.setDefaultHorizontalRotation(0f);
        chaseCam.setDefaultVerticalRotation(0.3f);
        chaseCam.setMaxVerticalRotation(0.5f);
        chaseCam.setMinVerticalRotation(0);
        chaseCam.setSmoothMotion(true);
        chaseCam.setTrailingEnabled(true);

        //On ajout le son de saut
        jump = new AudioNode(context.getAssetManager(), "sounds/effect/jump.wav");
        jump.setPositional(false);
        jump.setLooping(false);
        jump.setVolume(0.2f);

        // On met en place les event listener
        setUpKeys();
    }

    /**
     * Permet de mettre en place les evenements de Tux
     */
    private void setUpKeys() {
        context.getInputManager().addMapping("Left", new KeyTrigger(KeyInput.KEY_Q));
        context.getInputManager().addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
        context.getInputManager().addMapping("Up", new KeyTrigger(KeyInput.KEY_Z));
        context.getInputManager().addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
        context.getInputManager().addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));
        context.getInputManager().addMapping("Run", new KeyTrigger(KeyInput.KEY_LSHIFT));
        context.getInputManager().addListener(this, "Left");
        context.getInputManager().addListener(this, "Right");
        context.getInputManager().addListener(this, "Up");
        context.getInputManager().addListener(this, "Down");
        context.getInputManager().addListener(this, "Jump");
        context.getInputManager().addListener(this, "Run");
    }

    /**
     * Permet de récupérer les evenement clavier
     * @param binding le nom de l'evenement
     * @param isPressed true si l'evenement est appuyé
     * @param tpf le temps ecoulé depuis le dernier appel
     */
    @Override
    public void onAction(String binding, boolean isPressed, float tpf) {
        // Pour chaque type de mouvement on met à jour la variable correspondante avec la valeur de isPressed
        switch (binding) {
            case "Left":
                left = isPressed;
                break;
            case "Right":
                right = isPressed;
                break;
            case "Up":
                up = isPressed;
                break;
            case "Down":
                down = isPressed;
                break;
            case "Jump":
                //Si on appuie sur Espace on saute is pressed
                if (isPressed) {
                    jump.play();
                    tux.jump();
                }
                break;
            case "Run":
                run = isPressed;
                break;
        }
    }

    /**
     * Permet de mettre à jour tux à chaque frame
     */
    public void simpleUpdate() {
        // Si on est en mode run on multiplie la vitesse de déplacement par runMultiplier
        // ici on met à jour les vecteurs lateral et longitudinal en fonction de l'angle de la caméra
        if(run) {
            camLongitudinal.set(context.getCamera().getDirection()).multLocal(speed * runMultiplier, 0.0f, speed * runMultiplier);
            camLateral.set(context.getCamera().getLeft()).multLocal(strafeSpeed*(runMultiplier/2.0f));
        }else {
            camLongitudinal.set(context.getCamera().getDirection()).multLocal(speed, 0.0f, speed);
            camLateral.set(context.getCamera().getLeft()).multLocal(strafeSpeed);
        }
        // On initialise le vecteur de déplacement à 0
        walkDirection.set(0, 0, 0);
        // pour chaque direction on ajoute la valeur lateral ou longitudinal ou leur opposé.
        if (left) {
            walkDirection.addLocal(camLateral);
        }
        if (right) {
            walkDirection.addLocal(camLateral.negate());
        }
        if (up) {
            walkDirection.addLocal(camLongitudinal);
        }
        if (down) {
            walkDirection.addLocal(camLongitudinal.negate());
        }
        // On met à jour le vecteur de déplacement dans tux
        tux.setWalkDirection(walkDirection);
        // On mes à jour la rotation de tux pour qu'il soit toujours dos à la caméra
        tux.setViewDirection(camLongitudinal);
    }
}
