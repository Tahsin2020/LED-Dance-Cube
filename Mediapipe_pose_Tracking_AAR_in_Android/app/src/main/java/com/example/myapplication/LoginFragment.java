package com.example.myapplication;

import static com.example.myapplication.StartActivity.*;
//import static com.example.myapplication.StartActivity.mongoCollection;
//import static com.example.myapplication.StartActivity.user;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
//import com.google.firebase.firestore.DocumentReference;
//import com.google.firebase.firestore.DocumentSnapshot;
//import com.google.firebase.firestore.EventListener;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.FirebaseFirestoreException;
//import com.google.firebase.firestore.QueryDocumentSnapshot;
//import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import org.bson.Document;

import java.util.ArrayList;
import java.util.HashMap;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.iterable.MongoCursor;
import io.realm.mongodb.mongo.result.UpdateResult;

public class LoginFragment extends Fragment {

    public HashMap<String, Integer> stats = new HashMap<>();
    public LoginFragment() {
        // Required empty public constructor
    }
    private MongoDatabase mongoDatabase;
    private MongoClient mongoClient;
    private MongoCollection<Document> mongoCollection;

    Button login;
    EditText pwdView;
    TextView emailView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login,container,false);
        final Button button = v.findViewById(R.id.btn_login);

        super.onCreate(savedInstanceState);

        emailView =v.findViewById(R.id.et_email);
        pwdView =v.findViewById(R.id.et_password);
        login = v.findViewById(R.id.btn_login);
        ImageView imageView = v.findViewById(R.id.login_cube_gif);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                animation(imageView);
                String email = emailView.getText().toString();
                String password = pwdView.getText().toString();
                if (email.equals("")) {
                    Toast.makeText(getContext(), "Enter an email", Toast.LENGTH_SHORT).show();
                } else if (password.equals("")) {
                    Toast.makeText(getContext(), "Enter an password", Toast.LENGTH_SHORT).show();
                } else {
                    Credentials credentials = Credentials.emailPassword(email, password);
                    app.loginAsync(credentials, new App.Callback<User>() {
                        @Override
                        public void onResult(App.Result<User> r) {
                            if(r.isSuccess())
                            {
                                Log.v("User","Logged In Successfully");
                                Toast.makeText(getContext(), "Logged In successfully", Toast.LENGTH_SHORT).show();
                                user = app.currentUser();
                                Intent redirect = new Intent(getActivity(),HomePageActivity.class);
                                startActivity(redirect);
                            }
                            else
                            {
                                Log.v("User","Failed to Login");
                                Toast.makeText(getContext(), "Email or password incorrect", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        return v;
    }

     public void confirm_connected(){
         AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
         AlertDialog dialog = builder.create();

        View view = View.inflate(getContext(), R.layout.activity_home, null);
        dialog.setView(view);

        Button button = (Button) view.findViewById(R.id.button_home2);
        //final EditText editText = (EditText) view.findViewById(R.id.new_group_name);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent redirect = new Intent(getActivity(),HomePageActivity.class);
                startActivity(redirect);
            }
        });

        dialog.show();
    }

    public void animation(ImageView imageView) {

        ObjectAnimator elevation = ObjectAnimator.ofFloat(imageView, "elevation", 0f, 10f);
        ObjectAnimator rotation = ObjectAnimator.ofFloat(imageView, "rotation", 0f, 360f);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(imageView, "scaleX", 1f, 2f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(imageView, "scaleY", 1f, 2f);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(rotation, scaleX, scaleY);
        animatorSet.setDuration(1000);

        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
//                Intent redirect = new Intent(getActivity(),HomePageActivity.class);
//                startActivity(redirect);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });

        animatorSet.start();
    }



}