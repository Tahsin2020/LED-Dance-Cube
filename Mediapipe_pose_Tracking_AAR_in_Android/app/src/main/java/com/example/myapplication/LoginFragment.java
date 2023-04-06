package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class LoginFragment extends Fragment {


    public LoginFragment() {
        // Required empty public constructor
    }

    Button login;
    Button register;
    EditText pwd;
    TextView email;
    //ProgressBar progress;

    FirebaseFirestore db;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login,container,false);
        final Button button = v.findViewById(R.id.btn_login);

        super.onCreate(savedInstanceState);

        email=v.findViewById(R.id.et_email);
        pwd=v.findViewById(R.id.et_password);
        login = v.findViewById(R.id.btn_login);

        db= FirebaseFirestore.getInstance();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                confirm_connected();

              if(email.getText().toString().equals("")){
                  Toast.makeText(getContext(), "Please enter valid email", Toast.LENGTH_SHORT).show();
              }else if( pwd.getText().toString().equals("")){
                  Toast.makeText(getContext(), "Please enter valid password", Toast.LENGTH_SHORT).show();
              }
              db.collection("client")
                      .get()
                      .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                          @Override
                          public void onComplete(@NonNull Task<QuerySnapshot> task) {
                              if(task.isSuccessful()){
                                  for(QueryDocumentSnapshot doc : task.getResult()){
                                      String a=doc.getString("Email");
                                      String b=doc.getString("Password");
                                      String a1=email.getText().toString().trim();
                                      String b1=pwd.getText().toString().trim();
                                      if(a.equalsIgnoreCase(a1) & b.equalsIgnoreCase(b1)) {

                                          Toast.makeText(getContext(), "Logged In", Toast.LENGTH_SHORT).show();

                                          ////////////////////////////
                                          confirm_connected();
                                          break;

                                      }else
                                          Toast.makeText(getContext(), "Cannot login,incorrect Email and Password", Toast.LENGTH_SHORT).show();

                                  }

                              }
                          }
                      });

//                Intent redirect = new Intent(getActivity(),HomePageActivity.class);
//                startActivity(redirect);

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

}