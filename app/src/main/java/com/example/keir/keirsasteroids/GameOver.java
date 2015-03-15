package com.example.keir.keirsasteroids;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;


public class GameOver extends ActionBarActivity {

    public static String textToShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        ImageButton start = (ImageButton)findViewById(R.id.imageButton);

        //start.setHapticFeedbackEnabled(true);

        start.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Intent game = new Intent(getApplicationContext(), GameActivity.class);
                startActivity(game);
            }
        });



        TextView endScore = (TextView)findViewById(R.id.FinalScore);

        endScore.setText(textToShow);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game_over, menu);
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

