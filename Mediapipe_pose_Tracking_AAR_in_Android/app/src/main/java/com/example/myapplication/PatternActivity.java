package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.myapplication.databinding.ActivityPatternBinding;

import java.io.DataOutputStream;
import java.io.IOException;

public class PatternActivity extends AppCompatActivity {
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.Home_page:
                startActivity(new Intent(this, HomePageActivity.class));
                return true;
            case R.id.patterns:
                startActivity(new Intent(this, PatternActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pattern);
        new Thread(new Thread3((byte) 0x02)).start();
        Button button2 = (Button) findViewById(R.id.button_pattern2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pattern2Activity();
            }
        });

        Button button3 = (Button) findViewById(R.id.button_pattern3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pattern3Activity();
            }
        });

        Button button4 = (Button) findViewById(R.id.button_pattern4);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pattern4Activity();
            }
        });

    }

    public void backToMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void pattern2Activity(){ // When button_pattern3 clicked
        new Thread(new Thread3((byte) 0x20)).start();
    }
    public void pattern3Activity(){ // When button_pattern3 clicked
        new Thread(new Thread3((byte) 0x30)).start();
    }
    public void pattern4Activity(){ // When button_pattern4 clicked
        new Thread(new Thread3((byte) 0x40)).start();
    }

    class Thread3 implements Runnable {
        private byte dataToSend;
        Thread3(byte b) {
            dataToSend = b;
        }
        Thread3() {}
        @Override
        public void run() {
            try {
                HomePageActivity.data_output.write(dataToSend);
                HomePageActivity.data_output.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}