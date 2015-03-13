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
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    public static float myScore = 0f;
    public static boolean startMainActivity = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton start = (ImageButton)findViewById(R.id.btnStart);

        ImageButton exit = (ImageButton)findViewById(R.id.btnExit);

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

        TextView endScore = (TextView)findViewById(R.id.finalScore);
        String textToShow = Float.toString(myScore);
        endScore.setText(textToShow);
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

    public static void GameOver(float score) {
        myScore = score;
        startMainActivity = true;
    }
}
