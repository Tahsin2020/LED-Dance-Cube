package com.example.myapplication;

import static com.example.myapplication.HomePageActivity.data_output;
import static com.example.myapplication.StartActivity.user;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;

/*
 * Main Activity class that loads {@link MainFragment}.
 */
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

import org.bson.Document;

import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.result.UpdateResult;

public class GalleryActivity extends AppCompatActivity {

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
                Intent intent = new Intent(this, GalleryActivity.class);
                startActivity(intent);
                return true;
            case R.id.brightness:
                startActivity(new Intent(this, BrightnessActivity.class));
                return true;
            case R.id.statistics:
                startActivity(new Intent(this, StatisticsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    Gallery simpleGallery;

    // CustomizedGalleryAdapter is a java/kotlin
    // class which extends BaseAdapter
    // and implement the override methods.
    CustomizedGalleryAdapter customGalleryAdapter;
    ImageView selectedImageView;

    // To show the selected language, we need this
    // array of images, here taken 10 different kind of
    // most popular programming languages
    int[] images = {
            R.drawable.cone,
            R.drawable.diamond,
            R.drawable.helix,
            R.drawable.pulsating_wave_sphere,
            R.drawable.rolling_ball,
            R.drawable.rotating_wall,
            R.drawable.wave,

    };

    String[] image_names = {
            "Vortex",
            "Diamond",
            "Helix",
            "Sphere",
            "Rolling Ball",
            "Rotating Wall",
            "Wave",

    };

    protected void onStart() {
        super.onStart();
        if (data_output != null) {
            new Thread(new HomePageActivity.Thread3((byte) 0x02)).start();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        new Thread(new PatternActivity.Thread3((byte) 0x02)).start();

        Button btn_image = (Button) findViewById(R.id.button_gallery_name);
        Button btn_play = (Button) findViewById(R.id.button_gallery_play);

        MongoClient mongoClient = user.getMongoClient("mongodb-atlas");
        MongoDatabase mongoDatabase = mongoClient.getDatabase("DanceCube");
        MongoCollection mongoCollection = mongoDatabase.getCollection("Statistic");
        Document filterDoc = new Document("owner_id", user.getId());
        Document updateDoc;

        // Our layout is activity_main
        // get the reference of Gallery. As we are showing
        // languages it is named as languagesGallery
        // meaningful names will be good for easier understanding
        simpleGallery = (Gallery) findViewById(R.id.languagesGallery);

        // get the reference of ImageView
        selectedImageView = (ImageView) findViewById(R.id.imageView);

        // initialize the adapter
        customGalleryAdapter = new CustomizedGalleryAdapter(getApplicationContext(), images);

        // set the adapter for gallery
        simpleGallery.setAdapter(customGalleryAdapter);

        // Let us do item click of gallery and image can be identified by its position
        simpleGallery.setOnItemClickListener((parent, view, position, id) -> {
            // Whichever image is clicked, that is set in the selectedImageView
            // position will indicate the location of image
            selectedImageView.setImageResource(images[position]);
            //String imageName = (String)images[position].getTag();
            //Log.i(getCallingPackage(),imageName + "check image name");
            btn_image.setText(image_names[position]);
            btn_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String patternName = "Vortex";
                    try {
                        switch(position){
                            case 0:
                                Log.i(getCallingPackage(),"check click");
                                new Thread(new PatternActivity.Thread3((byte) 0x20)).start();
                                patternName = "Vortex";
                                break;
                            case 1:
                                new Thread(new PatternActivity.Thread3((byte) 0x10)).start();
                                patternName = "Diamond";
                                System.out.println("clicked " + patternName);
                                break;
                            case 2:
                                new Thread(new PatternActivity.Thread3((byte) 0x70)).start();
                                patternName = "Helix";
                                System.out.println("clicked " + patternName);
                                break;
                            case 3:
                                new Thread(new PatternActivity.Thread3((byte) 0x30)).start();
                                patternName = "Sphere";
                                System.out.println("clicked " + patternName);
                                break;
                            case 4:
                                new Thread(new PatternActivity.Thread3((byte) 0x40)).start();
                                patternName = "Rolling Ball";
                                System.out.println("clicked " + patternName);
                                break;
                            case 5:
                                new Thread(new PatternActivity.Thread3((byte) 0x50)).start();
                                patternName = "Rotating Wall";
                                System.out.println("clicked " + patternName);
                                break;
                            case 6:
                                new Thread(new PatternActivity.Thread3((byte) 0x60)).start();
                                patternName = "Wave";
                                System.out.println("clicked " + patternName);
                                break;
                            default:
                                break;
                        }
                    } finally {
                        System.out.println("In finally block");
                        Document updateDoc = new Document("$inc", new Document(patternName, 1));
                        RealmResultTask<UpdateResult> updateTask = mongoCollection.updateOne(filterDoc, updateDoc);
                        String p = patternName;
                        updateTask.getAsync(task -> {
                            if (task.isSuccess()) {
                                Log.v("Data", "Updated " + p + " sucessfully");
                            } else {
                                Log.v("Data", "Updated " + p + " failed");
                            }
                        });
                    }
                }
            });
        });
    }
}
