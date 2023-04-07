package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
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

    Animation zoom_in,zoom_out;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login,container,false);
        final Button button = v.findViewById(R.id.btn_login);

        super.onCreate(savedInstanceState);

        email=v.findViewById(R.id.et_email);
        pwd=v.findViewById(R.id.et_password);
        login = v.findViewById(R.id.btn_login);
        ImageView imageView = v.findViewById(R.id.login_cube_gif);

        db= FirebaseFirestore.getInstance();
//        login.setOnClickListener(this);
//        register.setOnClickListener(this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

//                zoom_in = AnimationUtils.loadAnimation(getContext(),R.anim.zoomin);
//                imageView.setVisibility(View.VISIBLE);
//                imageView.startAnimation(zoom_in);
                //animation(imageView);

                animation(imageView);


//                Intent redirect = new Intent(getActivity(),HomePageActivity.class);
//                startActivity(redirect);



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
                                          Intent redirect = new Intent(getActivity(),HomePageActivity.class);
                                          startActivity(redirect);
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
                Intent redirect = new Intent(getActivity(),HomePageActivity.class);
                startActivity(redirect);
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