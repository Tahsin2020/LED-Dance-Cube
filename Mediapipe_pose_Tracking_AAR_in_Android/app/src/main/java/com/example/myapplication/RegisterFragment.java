package com.example.myapplication;

import static androidx.databinding.DataBindingUtil.setContentView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

//import com.google.firebase.firestore.FirebaseFirestore;

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
    TextView signin;
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

        super.onCreate(savedInstanceState);
//        setContentView(R.layout.fragment_register);
//
//        reg_registration=findViewById(R.id.btn_reg);
//        reg_name=findViewById(R.id.reg_name);
//        reg_email=findViewById(R.id.reg_email);
//        reg_password=findViewById(R.id.reg_password);
//        reg_conf_pwd=findViewById(R.id.reg_conpwd);
//        signin=findViewById(R.id.signin_view);

        //FirebaseApp.initializeApp(context);

        firebaseFirestore=FirebaseFirestore.getInstance();
        ref = firebaseFirestore.collection("client").document();
//        reg_registration.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(reg_name.getText().toString().equals("")) {
//                    Toast.makeText(Register.this, "Please type a username", Toast.LENGTH_SHORT).show();
//
//                }else if(reg_email.getText().toString().equals("")) {
//                    Toast.makeText(Register.this, "Please type an email id", Toast.LENGTH_SHORT).show();
//
//                }else if(reg_password.getText().toString().equals("")){
//                    Toast.makeText(Register.this, "Please type a password", Toast.LENGTH_SHORT).show();
//
//                }else if(!reg_conf_pwd.getText().toString().equals(reg_password.getText().toString())){
//                    Toast.makeText(Register.this, "Password mismatch", Toast.LENGTH_SHORT).show();
//
//                }else {
//
//                    ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                        @Override
//                        public void onSuccess(DocumentSnapshot documentSnapshot) {
//
//                            if (documentSnapshot.exists())
//                            {
//                                Toast.makeText(Register.this, "Sorry,this user exists", Toast.LENGTH_SHORT).show();
//                            } else {
//                                Map<String, Object> reg_entry = new HashMap<>();
//                                reg_entry.put("Name", reg_name.getText().toString());
//                                reg_entry.put("Email", reg_email.getText().toString());
//                                reg_entry.put("Password", reg_password.getText().toString());
//
//                                //   String myId = ref.getId();
//                                firebaseFirestore.collection("client")
//                                        .add(reg_entry)
//                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                                            @Override
//                                            public void onSuccess(DocumentReference documentReference) {
//                                                Toast.makeText(Register.this, "Successfully added", Toast.LENGTH_SHORT).show();
//                                            }
//                                        })
//                                        .addOnFailureListener(new OnFailureListener() {
//                                            @Override
//                                            public void onFailure(@NonNull Exception e) {
//                                                Log.d("Error", e.getMessage());
//                                            }
//                                        });
//                            }
//                        }
//                    });
//                }
//            }
//        });
//
//        signin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent mainpage = new Intent(Register.this, MainActivity.class);
//                startActivity(mainpage);
//            }
//        });

        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_register, container, false);
        View v = inflater.inflate(R.layout.fragment_login,container,false);
        return v;
    }


}