package fr.litopia.game.core;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.input.InputManager;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;

public abstract class GameLifeCycle {
    GameLoop app;
    GameState state;

    public void init(GameLoop app){
        this.app = app;
        this.state=GameState.INIT;
    }

    public Node getRootNode() {
        return app.getRootNode();
    }

    public BulletAppState getBulletAppState() {
        return app.getStateManager().getState(BulletAppState.class);
    }

    public Camera getCamera() {
        return app.getCamera();
    }

    public InputManager getInputManager() {
        return app.getInputManager();
    }

    public abstract void update(float fps);
    public abstract void cleanup();

    public AssetManager getAssetManager() {
        return app.getAssetManager();
    }
}
