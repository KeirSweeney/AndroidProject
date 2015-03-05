package com.example.keir.keirsasteroids;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

       /* MediaPlayer mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.Arpanauts);
        mediaPlayer.start();
        mediaPlayer.setLooping(true);
        */


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton start = (ImageButton)findViewById(R.id.btnStart);
        //start.setImageResource(R.drawable.start);
        ImageButton exit = (ImageButton)findViewById(R.id.btnExit);
        //exit.setImageResource(R.drawable.exit);

       // ImageView img = new ImageView(this);
        //img.setImageResource(R.drawable.android_hunter);

        //start.getBackground().setAlpha(0);
        start.setHapticFeedbackEnabled(true);

        //exit.getBackground().setAlpha(0);
        exit.setHapticFeedbackEnabled(true);

        start.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent game = new Intent(getApplicationContext(), GameActivity.class);
                startActivity(game);
            }
        });
        exit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int pid= android.os.Process.myPid();
                android.os.Process.killProcess(pid);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
