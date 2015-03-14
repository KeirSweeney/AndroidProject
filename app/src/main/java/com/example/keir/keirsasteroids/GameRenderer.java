package com.example.keir.keirsasteroids;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Debug;
import android.os.Vibrator;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Keir on 23/02/2015.
 */
public class GameRenderer implements Renderer {

    Rectangle mRectangle = new Rectangle();

    private GameObject background = new GameObject();
    private float bgScroll = 0f;


    private float x = 0.5f; // x position of player spacecraft
    private float y = 0.2f; // ypos of player
    private float deltaX = 0f; // increase of x
    private float rot = 0f; // rotation of player spacecraft
    private float deltaRotA = 0f; // increase of rotation - when starting to bank
    private float deltaRotD = 0f;
    private boolean isShooting;

    private boolean hasShot;

    private GameObject player = new GameObject();
    private float playerRad = 0.15f;
    public int health = 100;
    private GameObject bullet = new GameObject();
    private int maxBullet = 5;
    private float bulletRad = 0.01f;


    public static boolean bulletSound = false;

    private GameObject asteroid = new GameObject();
    private float asteroidRad = 0.05f;

    private float asteroidX = 0.5f;
    private float asteroidY = 1.0f;
    private int maxAsteroid = 3;

    private boolean bulletAsteroidCollision = false;

    public static float score = 0;
    private boolean randomX = true;
    //health bar

    //asteroid Instancing
    private LinkedList<Asteroid> asteroids = new LinkedList<>();
    private LinkedList<Bullet> bullets = new LinkedList<>();

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

        // load the player mesh & texture
        player.loadTexture(gl, R.raw.space_frigate_6_color);
        player.loadMesh(R.raw.space_frigate_6, 0);

        //load bullet
        bullet.loadTexture(gl,R.drawable.circle);
        bullet.loadMesh(vertices,normals,textures,faces);

        //load asteroid
        asteroid.loadTexture(gl,R.drawable.asteroid);
        asteroid.loadMesh(vertices,normals,textures,faces);
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

        //render the health bar
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
        gl.glTranslatef(0.5f,1.0f,0.0f);
        gl.glScalef((health/100.0f) * 0.5f,0.05f,1.0f);
        gl.glColor4f(0f,1.0f,0f,1.0f);
        mRectangle.draw(gl);
        gl.glColor4f(1.0f,1.0f,1.0f,1.0f);

        //health/100 * 0.5

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

        Point size = new Point();
        GameActivity.display.getSize(size);
        float W = size.x;
        float h = size.y;

        Asteroid a = new Asteroid();

        a.y = 1.0f;
        a.x = 0.5f;
        a.vy = 0.01f;

        if(asteroids.size() < maxAsteroid) {
            randomX = true;
            asteroids.add(a);
        }

        ListIterator<Asteroid> ia = asteroids.listIterator();

            while (ia.hasNext()) {
                Asteroid ma = ia.next();

                if(randomX && asteroids.size() != maxAsteroid) {
                    double thisX = randDouble();
                    ma.x = (float)thisX;
                }
                else {
                    randomX = false;
                }

                gl.glMatrixMode(GL10.GL_MODELVIEW);
                gl.glLoadIdentity();
                gl.glTranslatef(ma.x, ma.y, 1.0f);
                gl.glScalef(0.3f, 0.3f * (W / h), 1.0f);
                gl.glTranslatef(-0.5f, -0.5f, 0f);
                gl.glEnable(GL10.GL_BLEND);
                gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
                gl.glDisable(GL10.GL_LIGHTING);
                gl.glDisable(GL10.GL_DEPTH_TEST);

                asteroid.draw(gl);

                gl.glEnable(GL10.GL_DEPTH_TEST);
                gl.glDisable(GL10.GL_BLEND);
                gl.glEnable(GL10.GL_LIGHTING);


                ma.y -= a.vy;

                float dy = y - ma.y;
                float dx = x - ma.x;

                double d = Math.sqrt(dx * dx + dy * dy);



                if((float)d < (asteroidRad + playerRad)) {
                    health -= 10;
                    ia.remove();
                    vibrate();
                    continue;
                }

            }



        Bullet b = new Bullet();
        b.y = 0.4f;
        if(isShooting) {
            b.x = x;
            bullets.add(b);
        }

        ListIterator<Bullet> ib = bullets.listIterator();

            while (ib.hasNext()) {
                Bullet mb = ib.next();

                gl.glMatrixMode(GL10.GL_MODELVIEW);
                gl.glLoadIdentity();
                gl.glTranslatef(mb.x, mb.y, 1.0f);
                gl.glScalef(0.05f, 0.05f * (W / h), 1.0f);
                gl.glTranslatef(-0.5f, -0.5f, 0f);
                gl.glEnable(GL10.GL_BLEND);
                gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
                gl.glDisable(GL10.GL_LIGHTING);
                gl.glDisable(GL10.GL_DEPTH_TEST);
                gl.glDisable(GL10.GL_DEPTH_TEST);

                bullet.draw(gl);

                gl.glEnable(GL10.GL_DEPTH_TEST);
                gl.glEnable(GL10.GL_DEPTH_TEST);
                gl.glDisable(GL10.GL_BLEND);
                gl.glEnable(GL10.GL_LIGHTING);

                mb.vy = 0.01f;
                mb.y += mb.vy;

                ListIterator<Asteroid> ya = asteroids.listIterator();

                while (ya.hasNext()) {
                    Asteroid ta = ya.next();

                    float dx = mb.x - ta.x;
                    float dy = mb.y - ta.y;

                    double d = Math.sqrt(dx * dx + dy * dy);


                    if((float)d < (asteroidRad + bulletRad)) {
                        Log.d("collsion", "collision!");
                        ib.remove();
                        ya.remove();
                        continue;
                    }
                }
            }



            /*
            if(!randomX) {
                double thisX = randDouble();
                asteroidX = (float)thisX;
                randomX = true;
            }

            gl.glMatrixMode(GL10.GL_MODELVIEW);
            gl.glLoadIdentity();
            gl.glTranslatef(asteroidX, asteroidY, 0.0f);
            gl.glScalef(0.3f, 0.3f * (W / h), 1.0f);
            gl.glTranslatef(-0.5f, -0.5f, 0f);
            gl.glEnable(GL10.GL_BLEND);
            gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
            gl.glDisable(GL10.GL_LIGHTING);

            asteroid.draw(gl);
            gl.glDisable(GL10.GL_BLEND);
            gl.glEnable(GL10.GL_LIGHTING);

            asteroidY -= 0.02f;

            float xDist = Math.abs(asteroidX - x);
            float yDist = Math.abs(asteroidY - 0.4f);

            if (xDist < 0.15 && yDist < 0.04) {
                health -= 10;
                vibrate();

                asteroidY = 1.0f;
                randomX = false;
            }

            if (asteroidY < 0f) {
                asteroidY = 1.0f;
                randomX = false;
            }*/
        //}

        if(health <= 0) {
            MainActivity.GameOver(score);
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



    public void Shooting(boolean shoot){
        isShooting = shoot;
    }



    public static void vibrate(){
        GameActivity.vibrator.vibrate(500);
        Log.d("Vibrate", "Vibrate");
    }


    public static double randDouble() {

        // Usually this can be a field rather than a method variable
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        double randomNum = rand.nextDouble();

        return randomNum;
    }


}
