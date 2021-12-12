package fr.litopia.tux.game.objects.movable;

import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.material.Material;
import com.jme3.scene.Spatial;
import fr.litopia.tux.game.core.GameFindWord;

/**
 *
 * @author zaettal
 */
public class Letter {
    private final char letter;
    //Compteur d'instances de lettres permet de leur donnée un id unique utiliser dans bullet physics
    private static int count = 0;
    
    public Letter(GameFindWord context, char l, float x, float z){
        this.letter = l;
        String id = "L" + l + count;

        //Création du cube
        Spatial cube = context.getAssetManager().loadModel("assets/models/cube/cube.obj");
        Material mat_cube = new Material(context.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        //on change sa texture en fonction de la lettre
        if(l>='a'&& l<='z')
            mat_cube.setTexture("ColorMap",context.getAssetManager().loadTexture("assets/models/cube/"+l+".png"));
        else
            mat_cube.setTexture("ColorMap",context.getAssetManager().loadTexture("assets/models/cube/cube.png"));
        cube.setMaterial(mat_cube);
        cube.setLocalTranslation(x,(float)context.getRoom().getHeight()*2,z);
        cube.scale(4f);
        cube.setName(id);

        //initialisation de la physique
        CollisionShape cubeShapeCollide = CollisionShapeFactory.createBoxShape(cube);
        RigidBodyControl physics = new RigidBodyControl(cubeShapeCollide, 8f);
        cube.addControl(physics);

        //ajout de la lettre au monde physique
        context.getRootNode().attachChild(cube);
        context.getBulletAppState().getPhysicsSpace().add(physics);

        //incrementation du compteur d'instances
        count++;
    }

    /**
     * Permet de réinitialiser le compteur d'instances
     */
    public static void resetCount(){
        count = 0;
    }

    /**
     * permet de récupérer la lettre
     * @return lettre : char
     */
    public char getLetter(){
        return letter;
    }
}
