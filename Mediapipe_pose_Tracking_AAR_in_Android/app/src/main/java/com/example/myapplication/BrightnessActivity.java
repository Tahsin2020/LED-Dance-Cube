package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

public class BrightnessActivity extends AppCompatActivity {


    TextView tvProgressLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brightness);

        // set a change listener on the SeekBar
        SeekBar seekBar = findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(seekBarChangeListener);

        int progress = seekBar.getProgress();
        tvProgressLabel = findViewById(R.id.textView);
        tvProgressLabel.setText("Progress: " + progress);
    }

    SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {

        int seekProgress;

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            // updated continuously as the user slides the thumb
            seekProgress = progress;
            tvProgressLabel.setText("Progress: " + progress);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // called when the user first touches the SeekBar
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // called after the user finishes moving the SeekBar
            switch(seekProgress){
                case 0:  new Thread(new HomePageActivity.Thread3((byte) 0xa0)).start();
                    break;
                case 1:  new Thread(new HomePageActivity.Thread3((byte) 0xa1)).start();
                    break;
                case 2:  new Thread(new HomePageActivity.Thread3((byte) 0xa2)).start();
                    break;
                case 3:  new Thread(new HomePageActivity.Thread3((byte) 0xa3)).start();
                    break;
                case 4:  new Thread(new HomePageActivity.Thread3((byte) 0xa4)).start();
                    break;
                case 5:  new Thread(new HomePageActivity.Thread3((byte) 0xa5)).start();
                    break;
                case 6:  new Thread(new HomePageActivity.Thread3((byte) 0xa6)).start();
                    break;
                case 7:  new Thread(new HomePageActivity.Thread3((byte) 0xa7)).start();
                    break;
                default: new Thread(new HomePageActivity.Thread3((byte) 0xa7)).start();

            }
        }
    };
}
