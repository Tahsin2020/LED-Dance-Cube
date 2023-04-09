package com.example.myapplication;

import static com.example.myapplication.StartActivity.*;

import android.content.Intent;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

import org.bson.Document;


import io.realm.mongodb.App;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;


public class RegisterFragment extends Fragment {

    Button reg_registration;
    EditText reg_name;
    EditText reg_email;
    EditText reg_password;
    EditText reg_conf_pwd;
    private MongoDatabase mongoDatabase;
    private MongoClient mongoClient;
    private MongoCollection<Document> mongoCollection;
//    public static User user;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_register,container,false);
        reg_registration=v.findViewById(R.id.btn_register);
        reg_name=v.findViewById(R.id.et_name);
        reg_email=v.findViewById(R.id.et_email);
        reg_password=v.findViewById(R.id.et_password);
        reg_conf_pwd=v.findViewById(R.id.et_repassword);
        reg_registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(reg_name.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "Please type a username", Toast.LENGTH_SHORT).show();
                }else if(reg_email.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "Please type an email id", Toast.LENGTH_SHORT).show();
                }else if(reg_password.getText().toString().equals("")){
                    Toast.makeText(getContext(), "Please type a password", Toast.LENGTH_SHORT).show();
                }else if(!reg_conf_pwd.getText().toString().equals(reg_password.getText().toString())){
                    Toast.makeText(getContext(), "Password mismatch", Toast.LENGTH_SHORT).show();
                }else if (reg_password.getText().toString().length() < 6) {
                    Toast.makeText(getContext(), "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                }
                else {
                    app.getEmailPassword().registerUserAsync(reg_email.getText().toString(), reg_password.getText().toString(),it->{
                        if (it.isSuccess()) {
                            Credentials credentials = Credentials.emailPassword(reg_email.getText().toString(), reg_password.getText().toString());
                            app.loginAsync(credentials, new App.Callback<User>() {
                                @Override
                                public void onResult(App.Result<User> result) {
                                    if (result.isSuccess()) {
                                        Log.v("User","Registered Successfully");
                                        Toast.makeText(getContext(), "Registered successfully", Toast.LENGTH_SHORT).show();
                                        user = app.currentUser();
                                        mongoClient = user.getMongoClient("mongodb-atlas");
                                        mongoDatabase = mongoClient.getDatabase("DanceCube");
                                        mongoCollection = mongoDatabase.getCollection("Statistic");
                                        Document document = new Document("owner_id", user.getId());
                                        document.append("Streaming", 0);
                                        document.append("Vortex", 0);
                                        document.append("Diamond", 0);
                                        document.append("Helix", 0);
                                        document.append("Sphere", 0);
                                        document.append("Rolling Ball", 0);
                                        document.append("Rotating Wall", 0);
                                        document.append("Wave", 0);
                                        mongoCollection.insertOne(document).getAsync(r -> {
                                            if (r.isSuccess()) {
                                                Log.v("Data", "Data inserted successfully");
                                            } else {
                                                Log.v("Data", "Data insertion failed: " + r.getError().toString());
                                            }
                                        });
                                        Intent redirect = new Intent(getActivity(),HomePageActivity.class);
                                        startActivity(redirect);
                                    }
                                }
                            });

                        } else {
                            Log.v("User","Registeration failed");
                            Toast.makeText(getContext(), "Registered failed, please try again", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        return v;
    }


}