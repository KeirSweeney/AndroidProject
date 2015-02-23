package com.example.keir.keirsasteroids;

import android.content.Context;
import android.opengl.GLSurfaceView;

/**
 * Created by Keir on 23/02/2015.
 */
public class GameView extends GLSurfaceView {
    private GameRenderer renderer;
    public static Context context;

    public GameView(Context context) {
        super(context);
        renderer = new GameRenderer();
        this.setRenderer((Renderer) renderer);
        GameView.context = context;

    }


}