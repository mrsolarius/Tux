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
    private GameFindWord context;
    private LettersListener listener;
    private final char exceptedLetter;
    private char currentLetter;
    private RigidBodyControl physics;
    private Geometry plot;
    private String id;
    private int wordPosition;
    private static int count = 1;

    public LetterPlot(GameFindWord context, char exceptedLetter, int wordPosition, float x, float z, float size, float rotation){
        this.exceptedLetter = exceptedLetter;
        this.currentLetter= ' ';
        this.id = "plot" + count;
        this.wordPosition = wordPosition;
        this.context = context;

        Box box = new Box(size, 1, size);
        plot = new Geometry(id, box);
        Material mat_plot = new Material(context.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        if(count<=20)
            mat_plot.setTexture("ColorMap",context.getAssetManager().loadTexture("/textures/plot/brick"+count+".png"));
        else
            mat_plot.setTexture("ColorMap",context.getAssetManager().loadTexture("/textures/plot/brick.png"));
        plot.setMaterial(mat_plot);
        plot.setLocalTranslation(x,0,z);
        plot.rotate(0,rotation,0);

        CollisionShape shape = CollisionShapeFactory.createBoxShape(plot);
        physics = new RigidBodyControl(shape, 0);
        plot.addControl(physics);
        context.getRootNode().attachChild(plot);
        context.getBulletAppState().getPhysicsSpace().add(physics);
        context.getBulletAppState().getPhysicsSpace().addCollisionListener(this);
        this.listener = (LettersListener) context;
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
        if(physicsCollisionEvent.getNodeA().getName().equals(id) || physicsCollisionEvent.getNodeB().getName().equals(id)){
            if(!physicsCollisionEvent.getNodeA().getName().equals("Tux") && !physicsCollisionEvent.getNodeB().getName().equals("Tux")) {
                Spatial letterNode = physicsCollisionEvent.getNodeA().getName().equals(id) ? physicsCollisionEvent.getNodeB() : physicsCollisionEvent.getNodeA();
                if (this.currentLetter != letterNode.getName().charAt(1)) {
                    this.currentLetter = letterNode.getName().charAt(1);
                    this.listener.updateLetter(this);
                }
            }
        }
    }

    public void remove(){
        this.plot.removeFromParent();
    }
}
