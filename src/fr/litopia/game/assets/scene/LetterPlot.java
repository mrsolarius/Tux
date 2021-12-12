package fr.litopia.game.assets.scene;

import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import fr.litopia.game.assets.listeners.LettersListener;
import fr.litopia.game.core.GameFindWord;

public class LetterPlot implements PhysicsCollisionListener {
    private final LettersListener listener;
    private final char exceptedLetter;
    private char currentLetter;
    private final String id;
    private final int wordPosition;
    private static int count = 1;

    public LetterPlot(GameFindWord context, char exceptedLetter, int wordPosition, float x, float z, float size, float rotation){
        this.exceptedLetter = exceptedLetter;
        this.currentLetter= ' ';
        this.id = "plot" + count;
        this.wordPosition = wordPosition;

        // Creation du plot
        Box box = new Box(size, 1, size);
        Geometry plot = new Geometry(id, box);
        Material mat_plot = new Material(context.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        // Definition de la texture en fonction du numéro de plot
        if(count<=20)
            mat_plot.setTexture("ColorMap",context.getAssetManager().loadTexture("/textures/plot/brick"+count+".png"));
        else
            mat_plot.setTexture("ColorMap",context.getAssetManager().loadTexture("/textures/plot/brick.png"));
        plot.setMaterial(mat_plot);
        plot.setLocalTranslation(x,0,z);
        plot.rotate(0,rotation,0);

        // mise en place de là physique
        CollisionShape shape = CollisionShapeFactory.createBoxShape(plot);
        RigidBodyControl physics = new RigidBodyControl(shape, 0);
        plot.addControl(physics);

        // Mise en place du plot dans la scène
        context.getRootNode().attachChild(plot);
        context.getBulletAppState().getPhysicsSpace().add(physics);

        // Ajout du listener
        context.getBulletAppState().getPhysicsSpace().addCollisionListener(this);
        this.listener = context;
        count++;
    }


    public boolean isCorrect(){
        return currentLetter == exceptedLetter;
    }

    public static void resetCount(){
        count = 1;
    }

    public int getWordPosition() {
        return wordPosition;
    }

    @Override
    public void collision(PhysicsCollisionEvent physicsCollisionEvent) {
        // On verfie que le plot a été touché grace à son id
        if(physicsCollisionEvent.getNodeA().getName().equals(id) || physicsCollisionEvent.getNodeB().getName().equals(id)){
            //On verifie qu'il sagit bien d'un lettre et pas d'un tux
            if(!physicsCollisionEvent.getNodeA().getName().equals("Tux") && !physicsCollisionEvent.getNodeB().getName().equals("Tux")) {
                //On récupère la lettre touchée
                Spatial letterNode = physicsCollisionEvent.getNodeA().getName().equals(id) ? physicsCollisionEvent.getNodeB() : physicsCollisionEvent.getNodeA();
                //On mes à jour la lettre courante et on appelle le call back du listener
                if (this.currentLetter != letterNode.getName().charAt(1)) {
                    this.currentLetter = letterNode.getName().charAt(1);
                    //On appelle le call back du listener pour que GameFindWord puisse mettre à jour ce qu'il doit mettre à jour
                    this.listener.updateLetter(this);
                }
            }
        }
    }
}
