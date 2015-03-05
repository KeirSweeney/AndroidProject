package com.example.keir.keirsasteroids;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.ImageView;


public class GameActivity extends ActionBarActivity implements SensorEventListener{
    private GameView view;

    private SensorManager senSensorManager;
    private Sensor senAccelerometer;

    private long lastUpdate = 0;
    private float last_x,last_y,last_z;
    private static final int SHAKE_THRESHOLD = 600;

    public static float tiltX;

    public static boolean touched;

    public static Display display;

    public MediaPlayer mediaPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = new GameView(this);
        setContentView(view);

        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor((Sensor.TYPE_ACCELEROMETER));
        senSensorManager.registerListener(this,senAccelerometer,SensorManager.SENSOR_DELAY_NORMAL);

        display = getWindowManager().getDefaultDisplay();

        mediaPlayer = MediaPlayer.create(GameActivity.this, R.raw.arpanauts);
        mediaPlayer.start();
        mediaPlayer.setLooping(true);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                view.Shooting(true);
                break;
            case MotionEvent.ACTION_UP:
                view.Shooting(false);
                break;
        }
        return false;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;

        if(mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            tiltX = event.values[0];
            float absX = Math.abs(tiltX);
            //Log.d("Game","absX = " + absX);

            if(absX < 2.0f){
                view.Bank(0);
            }
            else if(tiltX > 0) {
                view.Bank(-1);
            }
            else{
                view.Bank(+1);
            }

           /* float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            long curTime = System.currentTimeMillis();
            //keep shake code in case i decide to add some bomb or explosion when the phone is shaked.
            if((curTime - lastUpdate) > 100){
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                float speed = Math.abs(x + y + z - last_x - last_y - last_z)/diffTime * 10000;

                if(speed > SHAKE_THRESHOLD){
                    //if a strong shake happens, do something here.
                }

                last_x = x;
                last_y = y;
                last_z = z;
            }*/
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    protected void onPause() {
        super.onPause();
        senSensorManager.unregisterListener(this);
        mediaPlayer.pause();
    }

    protected void onResume(){
        super.onResume();
        senSensorManager.registerListener(this,senAccelerometer,SensorManager.SENSOR_DELAY_NORMAL);
        mediaPlayer.start();
    }
}
