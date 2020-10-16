package com.example.ecommerceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.ecommerceapp.Model.Courier;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class CourierCreateAccount extends AppCompatActivity {

    private Button CreateRegisterButton;
    private EditText InputName, InputNIC, InputPhoneNumber, InputEmail, InputColomboSuburb, InputPassword, InputConfirmPassword;
    private ProgressBar LoadingBar;
    String test;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courier_create_account);

        CreateRegisterButton =(Button)findViewById(R.id.register_button);
        CreateRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(CourierCreateAccount.this,CourierDashboardActivity.class);
                startActivity(intent);
            }
        });

        CreateRegisterButton  =(Button)findViewById(R.id.register_button);
        InputName           =(EditText)findViewById(R.id.register_name_input);
        InputNIC           =(EditText)findViewById(R.id.register_nic_input);
        InputPhoneNumber           =(EditText)findViewById(R.id.register_phone_number_input);
        InputEmail           =(EditText)findViewById(R.id.register_email_input);
        InputColomboSuburb           =(EditText)findViewById(R.id.register_colombo_suburb_input);
        InputPassword           =(EditText)findViewById(R.id.register_password_input);
        InputConfirmPassword           =(EditText)findViewById(R.id.register_confirm_password_input);

        CreateRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateAccount(); //calling create account function in onCreate function
            }
        });



    }

    private void CreateAccount() {

        String name    = InputName.getText().toString();
        String nic    = InputNIC.getText().toString();
        String phonenumber    = InputPhoneNumber.getText().toString();
        String email    = InputEmail.getText().toString();
        String colombosuburb    = InputColomboSuburb.getText().toString();
        String password    = InputPassword.getText().toString();
        String confirmpassword    = InputConfirmPassword.getText().toString();


        if(TextUtils.isEmpty(name)){
            Toast.makeText(this, "Please Enter Your Name", Toast.LENGTH_SHORT).show();

        }
        else if(TextUtils.isEmpty(nic)) {
            Toast.makeText(this, "Please Enter Your NIC Number", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(phonenumber)){
            Toast.makeText(this, "Please Enter Your Phone Number", Toast.LENGTH_SHORT).show();

        }
        else if(!InputPhoneNumber.getText().toString().matches("[0-9]{10}")){
            InputPhoneNumber.setError("Invalid Format");
        }
        else if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please Enter Your Email Address", Toast.LENGTH_SHORT).show();

        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(InputEmail.getText().toString()).matches()){
            InputEmail.setError("Please Enter A Valid Email");
            Toast.makeText(this, "Please Enter A Valid Email Address", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(colombosuburb)){
            Toast.makeText(this, "Please Enter Colombo Suburb (Ex. Colombo 7)", Toast.LENGTH_SHORT).show();

        }
        else if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please Enter A Password", Toast.LENGTH_SHORT).show();

        }
        else if(TextUtils.isEmpty(confirmpassword)){
            Toast.makeText(this, "Enter  PassWord Again", Toast.LENGTH_SHORT).show();

        }else if(!password.equals(confirmpassword)){

            Toast.makeText(this,"Passwords Should Match",Toast.LENGTH_SHORT).show();

        } else{

            ValidatePhoneNumber(name,nic,phonenumber,email,colombosuburb,password);//calling database connection if all details are ready

        }

    }

    private void ValidatePhoneNumber(final String name, final String nic, final String phonenumber, final String email, final String colombosuburb, final String password) {

        final DatabaseReference RootRef;//getting the database connection
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(!( snapshot.child("Courier").child(phonenumber).exists() )){

                    HashMap<String,Object> userdatamap=new HashMap<>();
                    userdatamap.put("phonenumber",phonenumber);
                    userdatamap.put("name",name);
                    userdatamap.put("nic",nic);
                    userdatamap.put("email",email);
                    userdatamap.put("colombosuburb",colombosuburb);
                    userdatamap.put("password",password);


                    RootRef.child("Courier").child(phonenumber).updateChildren(userdatamap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(CourierCreateAccount.this,"Account successful ",Toast.LENGTH_SHORT).show();

                                        Intent intent=new Intent(CourierCreateAccount.this,MainActivity.class);
                                        startActivity(intent);

                                    }else{
                                        Toast.makeText(CourierCreateAccount.this,"Network error",Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                }
                else{
                    Toast.makeText(CourierCreateAccount.this,"This number already exist",Toast.LENGTH_SHORT).show();

                    Intent intent=new Intent(CourierCreateAccount.this,MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}