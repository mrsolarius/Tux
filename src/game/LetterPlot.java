package game;

import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.PhysicsControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.material.Material;
import com.jme3.math.Matrix3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

import static env3d.GameObjectAdapter.assetManager;

public class LetterPlot implements PhysicsCollisionListener {
    private LettersListener listener;
    private final char exceptedLetter;
    private char currentLetter;
    private RigidBodyControl physics;
    private Geometry plot;
    private String id;
    private int wordPosition;
    private static int count = 1;

    public LetterPlot(Jeu context, char exceptedLetter,int wordPosition, float x, float z,float size,float rotation){
        this.exceptedLetter = exceptedLetter;
        this.currentLetter= ' ';
        this.id = "plot" + count;
        this.wordPosition = wordPosition;

        Box box = new Box(size, 1, size);
        plot = new Geometry(id, box);
        Material mat_plot = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat_plot.setTexture("ColorMap", assetManager.loadTexture("models/cube/cube.png"));
        if(count<=20)
            mat_plot.setTexture("ColorMap",assetManager.loadTexture("/models/plot/brick"+count+".png"));
        else
            mat_plot.setTexture("ColorMap",assetManager.loadTexture("/models/plot/brick.png"));
        plot.setMaterial(mat_plot);
        plot.setLocalTranslation(x,0,z);
        plot.rotate(0,rotation,0);

        CollisionShape shape = CollisionShapeFactory.createBoxShape(plot);
        RigidBodyControl physics = new RigidBodyControl(shape, 0);
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

    public void resetCount(){
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
}
