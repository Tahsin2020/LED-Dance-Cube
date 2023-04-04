package com.example.myapplication;

import static androidx.databinding.DataBindingUtil.setContentView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.FirebaseFirestore;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterFragment extends Fragment {

    Button reg_registration;
    EditText reg_name;
    EditText reg_email;
    EditText reg_password;
    EditText reg_conf_pwd;
    //TextView signin;
    FirebaseFirestore firebaseFirestore;
    DocumentReference ref;


    public RegisterFragment() {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//
//        // Create a new user with a first and last name
//        Map<String, Object> user = new HashMap<>();
//
//        user.put("Email", "Ada");
//        user.put("Password", "Lovelace");
//        user.put("Username", 1815);
//
//        // Add a new document with a generated ID
//        db.collection("Users")
//                .add(user);
//        // Required empty public constructor


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FirebaseApp.initializeApp(getContext());

        super.onCreate(savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_register,container,false);
//        setContentView(R.layout.fragment_register);

        reg_registration=v.findViewById(R.id.btn_register);
        reg_name=v.findViewById(R.id.et_name);
        reg_email=v.findViewById(R.id.et_email);
        reg_password=v.findViewById(R.id.et_password);
        reg_conf_pwd=v.findViewById(R.id.et_repassword);
        //signin=v.findViewById(R.id.signin_view);

        //FirebaseApp.initializeApp(context);

        firebaseFirestore=FirebaseFirestore.getInstance();
        ref = firebaseFirestore.collection("client").document();
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

                }else {

                    ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            if (documentSnapshot.exists())
                            {
                                Toast.makeText(getContext(), "Sorry,this user exists", Toast.LENGTH_SHORT).show();
                            } else {
                                Map<String, Object> reg_entry = new HashMap<>();
                                reg_entry.put("Name", reg_name.getText().toString());
                                reg_entry.put("Email", reg_email.getText().toString());
                                reg_entry.put("Password", reg_password.getText().toString());

                                //   String myId = ref.getId();
                                firebaseFirestore.collection("client")
                                        .add(reg_entry)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                Toast.makeText(getContext(), "Successfully added", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.d("Error", e.getMessage());
                                            }
                                        });
                            }
                        }
                    });
                }
            }
        });

//        reg_registration.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent mainpage = new Intent(getActivity(), HomePageActivity.class);
//                startActivity(mainpage);
//            }
//        });

        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_register, container, false);

        return v;
    }


}