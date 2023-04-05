package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.protobuf.NullValue;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class HomePageActivity extends AppCompatActivity {
    private static final String TAG = "HomePageActivity";
    private static final String SERVER_IP = "128.189.244.150";
    private static final int SERVER_PORT = 12345;
//    private static final String SERVER_IP = "192.168.4.1";
//    private static final int SERVER_PORT = 80;
    public static DataOutputStream data_output;
    private InputStream input;
    Thread Thread1 = null;
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
                Intent intent = new Intent(this, PatternActivity.class);
                startActivity(intent);
                return true;
            case R.id.brightness:
                new Thread(new HomePageActivity.Thread3((byte) 0xa0)).start();
                startActivity(new Intent(this, BrightnessActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_unconnect);
//        Thread1 = new Thread(new Thread1());
//        Thread1.start();

        ///ToggleButton materialSwitch = (ToggleButton) findViewById(R.id.material_switch); // initiate a toggle button
       // Boolean ButtonState = materialSwitch.isChecked(); // check current state of a toggle button (true or false).

        Switch onOffSwitch = (Switch)  findViewById(R.id.sw_all_on);
        onOffSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //Log.v("Switch State=", ""+isChecked);
                if(isChecked) //Log.i(getCallingPackage(),"checked");
                {new Thread(new Thread3((byte) 0x01)).start();
                    onOffSwitch.setText("all lights on");}
                else new Thread(new Thread3((byte) 0x0F)).start();  //Log.i(getCallingPackage(),"unchecked");
            }

        });



        Button button = (Button) findViewById(R.id.button_home1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMainActivity();
            }
        });

        Button button_2 = (Button) findViewById(R.id.button_home2);
        button_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  ToPatternActivity();
//                ImageView img= (ImageView) findViewById(R.id.img_no_connection);
//                img.setImageResource(R.drawable.signal_black);

            }
        });

        Button button_3 = (Button) findViewById(R.id.button_home3);
        button_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ToGalleryActivity();
            }
        });

    }

    protected void onStart() {
        super.onStart();
        if (data_output != null) {
            new Thread(new Thread3((byte) 0x01)).start();
        }
    }

    public void backToMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void ToPatternActivity(){
        Intent intent = new Intent(this, PatternActivity.class);
        startActivity(intent);
    }

    public void ToGalleryActivity(){
        Intent intent = new Intent(this, GalleryActivity.class);
        startActivity(intent);
    }

    class Thread1 implements Runnable {
        @Override
        public void run() {
            Socket socket;
            try {
                socket = new Socket(SERVER_IP, SERVER_PORT);
                data_output = new DataOutputStream(socket.getOutputStream());
                input = socket.getInputStream();
                Log.v(TAG, "Connected to server");
                new Thread(new Thread2()).start();
                new Thread(new Thread3((byte) 0x01)).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class Thread2 implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    byte[] buffer = new byte[2048];
                    int bytesRead = input.read(buffer);
                    String message = new String(buffer, 0, bytesRead);
                    Log.v(TAG, message);
                    if (message != null) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ImageView img = (ImageView) findViewById(R.id.img_no_connection);
                                img.setImageResource(R.drawable.signal_black);
                            }
                        });
                    } else {
                        Thread1 = new Thread(new Thread1());
                        Thread1.start();
                        return;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class Thread3 implements Runnable {
        private byte dataToSend;
        Thread3(byte b) {
            dataToSend = b;
        }
        Thread3() {}
        @Override
        public void run() {
            if(data_output != null) {
                try {
                    HomePageActivity.data_output.write(dataToSend);
                    HomePageActivity.data_output.flush();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

        }
    }
}
