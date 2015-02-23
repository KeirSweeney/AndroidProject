package com.example.keir.keirsasteroids;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Keir on 23/02/2015.
 */
public class GameRenderer implements Renderer {

    private GameObject background = new GameObject();
    private float bgScroll = 0f;

    private float vertices[] = {
            0.0f, 0.0f, 0.0f,
            1.0f, 0.0f, 0.0f,
            1.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
    };
    private float normals[] = {
            0.0f, -1.0f, 0.0f,
            0.0f, -1.0f, 0.0f,
            0.0f, -1.0f, 0.0f,
            0.0f, -1.0f, 0.0f,
    };
    private float textures[] = {
            0.0f, 0.0f,
            1.0f, 0f,
            1f, 1.0f,
            0f, 1f,
    };
    private short faces[] = {
            0,1,2,
            0,2,3,
    };

    public GameRenderer() {
        super();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        gl.glEnable(GL10.GL_DEPTH_TEST); // depth test necessary for most 3D scenes
        gl.glEnable(GL10.GL_NORMALIZE); // necessary for some model formats
        gl.glShadeModel(GL10.GL_SMOOTH); // smooth shading mode is the default one
        gl.glEnable(GL10.GL_TEXTURE_2D); // enable texturing
        gl.glClearDepthf(1.0f); // clear depth buffer
        // setup lighting
        gl.glEnable(GL10.GL_LIGHTING);
        gl.glEnable(GL10.GL_LIGHT0);
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION,
                new float [] { 1.0f, 1.0f, 2.5f, 0.0f }, 0);
        // load the background mesh & texture
        background.loadTexture(gl, R.drawable.backgroundstars);
        background.loadMesh(vertices, normals, textures, faces);
        // All other game objects will be initialised here


    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        gl.glViewport(0, 0, width,height);
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrthof(0f, 1f, 0f, 1f, -10f, 10f);

    }

    @Override
    public void onDrawFrame(GL10 gl) {
        // limit frame rate to 60 fps
        try {
            Thread.sleep(60);
        } catch (InterruptedException e) { }
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
        gl.glTranslatef(0.0f, 0.0f, -5.0f);gl.glMatrixMode(GL10.GL_TEXTURE);
        gl.glLoadIdentity();
        gl.glTranslatef(0.0f, bgScroll, 0.0f);
        bgScroll += .002f;
        if (bgScroll == Float.MAX_VALUE)
            bgScroll = 0f;
        gl.glDisable(GL10.GL_LIGHTING);
        background.draw(gl);
        gl.glEnable(GL10.GL_LIGHTING);

//All other game drawing will be called here

    }
}
