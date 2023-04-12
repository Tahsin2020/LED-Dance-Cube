package com.example.myapplication;

import static com.example.myapplication.StartActivity.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.bson.Document;

import java.util.HashMap;

import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.iterable.MongoCursor;

public class StatisticsActivity extends AppCompatActivity {

    HashMap<String, Integer> stats = new HashMap<>();

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        //load data from mongo db

        MongoClient mongoClient = user.getMongoClient("mongodb-atlas");
        MongoDatabase mongoDatabase = mongoClient.getDatabase("DanceCube");
        MongoCollection mongoCollection = mongoDatabase.getCollection("Statistic");
        Document filterDoc = new Document("owner_id", user.getId());
        RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(filterDoc).iterator();
        findTask.getAsync(task->{
            if(task.isSuccess()) {
                MongoCursor<Document> results = task.get();
                if(!results.hasNext()) {
                    Log.v("Result","Could not find");
                }
                while(results.hasNext()) {
                    Document currentDoc = results.next();
                    stats.put("Streaming", (Integer) currentDoc.get("Streaming"));
                    stats.put("Vortex", (Integer) currentDoc.get("Vortex"));
                    stats.put("Diamond", (Integer) currentDoc.get("Diamond"));
                    stats.put("Helix", (Integer) currentDoc.get("Helix"));
                    stats.put("Sphere", (Integer) currentDoc.get("Sphere"));
                    stats.put("Rolling Ball", (Integer) currentDoc.get("Rolling Ball"));
                    stats.put("Rotating Wall", (Integer) currentDoc.get("Rotating Wall"));
                    stats.put("Wave", (Integer) currentDoc.get("Wave"));
                    //System.out.println("Values read from mongoDB: " + stats.toString());
                    ((TextView)findViewById(R.id.sta_streaming)).setText("Streaming: " + stats.get("Streaming").toString());
                    ((TextView)findViewById(R.id.sta_vortex)).setText("Vortex: " + stats.get("Vortex").toString());
                    ((TextView)findViewById(R.id.sta_diamond)).setText("Diamond: " + stats.get("Diamond").toString());
                    ((TextView)findViewById(R.id.sta_helix)).setText("Helix: " + stats.get("Helix").toString());
                    ((TextView)findViewById(R.id.sta_sphere)).setText("Sphere: " + stats.get("Sphere").toString());
                    ((TextView)findViewById(R.id.sta_rolling)).setText("Rolling Ball: " + stats.get("Rolling Ball").toString());
                    ((TextView)findViewById(R.id.sta_rotating)).setText("Rotating Wall: " + stats.get("Rotating Wall").toString());
                    ((TextView)findViewById(R.id.sta_wave)).setText("Wave: " + stats.get("Wave").toString());
                }
            }
            else
            {
                Log.v("Task Error",task.getError().toString());
            }
        });

    }
}