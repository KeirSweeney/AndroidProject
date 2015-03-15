package com.example.keir.keirsasteroids;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.TextView;


public class GameActivity extends ActionBarActivity implements SensorEventListener{
    private GameView view;

    private TextView myText = null;

    private SensorManager senSensorManager;
    private Sensor senAccelerometer;

    private long lastUpdate = 0;
    private float last_x,last_y,last_z;
    private static final int SHAKE_THRESHOLD = 600;

    public static float tiltX;

    public static boolean touched;

    public static Display display;

    public MediaPlayer BGmediaPlayer;
    public MediaPlayer shootMediaPlayer;

    public static Vibrator vibrator;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = new GameView(this);
        setContentView(view);


        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor((Sensor.TYPE_ACCELEROMETER));
        senSensorManager.registerListener(this,senAccelerometer,SensorManager.SENSOR_DELAY_NORMAL);

        display = getWindowManager().getDefaultDisplay();

        BGmediaPlayer = MediaPlayer.create(GameActivity.this, R.raw.arpanauts);
        BGmediaPlayer.start();
        BGmediaPlayer.setLooping(true);

        shootMediaPlayer = MediaPlayer.create(GameActivity.this, R.raw.lasershoot);
        shootMediaPlayer.setLooping(false);
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
                if(!GameRenderer.bulletSound) {
                    shootMediaPlayer.start();
                }
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

            if(absX < 1.5f){
                view.Bank(0);
            }
            else if(tiltX > 0) {
                view.Bank(-1);
            }
            else{
                view.Bank(+1);
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    protected void onPause() {
        super.onPause();
        senSensorManager.unregisterListener(this);
        BGmediaPlayer.pause();
    }

    protected void onResume(){
        super.onResume();
        senSensorManager.registerListener(this,senAccelerometer,SensorManager.SENSOR_DELAY_NORMAL);
        BGmediaPlayer.start();
    }
}
