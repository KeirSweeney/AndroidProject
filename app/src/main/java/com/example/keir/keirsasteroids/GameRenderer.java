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

    private float x = 0.5f; // x position of player spacecraft
    private float deltaX = 0f; // increase of x
    private float rot = 0f; // rotation of player spacecraft
    private float deltaRotA = 0f; // increase of rotation - when starting to bank
    private float deltaRotD = 0f;

    private GameObject player = new GameObject();

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

        // load the player mesh & texture
        player.loadTexture(gl, R.drawable.space_frigate_6_color);
        player.loadMesh(R.raw.space_frigate_6, 0);
        //Render the player object
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
        gl.glTranslatef(x, 0.2f, 0.0f);
        gl.glRotatef(rot, 0.0f, 1.0f, 0.0f);
        gl.glRotatef(-90, 0.0f, 0.0f, 1.0f);
        gl.glRotatef(90, 1.0f, 0.0f, 0.0f);
        gl.glScalef(0.01f, 0.01f, 0.01f);

        gl.glMatrixMode(GL10.GL_TEXTURE);
        gl.glLoadIdentity();

        player.draw(gl);

        // x-position
        x += deltaX;
        if (x < 0) { x = 0; Bank(0); }
        if (x > 1) { x = 1; Bank(0); }
        // rotation – when starting the banking animation (accelerating)
        rot += deltaRotA;
        if (rot >= 50) deltaRotA = 0;
        if (rot <= -50) deltaRotA = 0;
        // rotation – when finishing the banking animation (decelerating)
        rot += deltaRotD;
        if (deltaRotD < 0 && rot < 0 || deltaRotD > 0 && rot > 0) {
            rot = 0;
            deltaRotD = 0;
        }

    }

    public void Bank(int val) {
        deltaX = 0.04f * val;
        deltaRotA = 10f * val;
        if (val == 0)
            if (rot > 0)
                deltaRotD = -10;
            else
                deltaRotD = 10;
        else
            deltaRotD = 0;
    }
}
