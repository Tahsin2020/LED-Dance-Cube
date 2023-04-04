package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import androidx.fragment.app.Fragment;

public class LoginFragment extends Fragment {


    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_login,container,false);
        final Button button = v.findViewById(R.id.btn_login);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // do something when the corky is clicked

                Intent redirect = new Intent(getActivity(),HomePageActivity.class);
                startActivity(redirect);
            }
        });

        return v;
       //return inflater.inflate(R.layout.fragment_login, container, false);
    }


//    if(username.getText().toString().equals("admin") &&
//            password.getText().toString().equals("admin")){
//        Toast.makeText(getApplicationContext(), "Redirecting...",
//                Toast.LENGTH_SHORT).show();
//        Intent i = new Intent(login.this, your_new_activity_name.class);
//        startActivity(i);
//
//    }



}