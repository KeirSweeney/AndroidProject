package com.example.keir.keirsasteroids;

import android.content.Context;
import android.opengl.GLSurfaceView;

/**
 * Created by Keir on 23/02/2015.
 */
public class GameView extends GLSurfaceView {
    private GameRenderer renderer;
    public static GameActivity context;

    public GameView(GameActivity context) {
        super(context);
        renderer = new GameRenderer();
        this.setRenderer((Renderer) renderer);
        GameView.context = context;

    }

    public void Bank(int val) {
        renderer.Bank(val);
    }

    public void Shooting(boolean shoot){
        renderer.Shooting(shoot);
    }


}
