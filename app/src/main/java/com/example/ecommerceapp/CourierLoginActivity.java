package com.example.ecommerceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerceapp.Model.Courier;
import com.example.ecommerceapp.Model.Users;
import com.example.ecommerceapp.Prevalent.CourierPrevalent;
import com.example.ecommerceapp.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class CourierLoginActivity extends AppCompatActivity {

    private EditText InputPhoneNumber, InputPassword;

    private CheckBox RememberMeCbox;
    private Button CourierLogin, CourierSignup;
    private String parentDbName = "Courier";
    private ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courier_login);

        InputPhoneNumber=(EditText) findViewById(R.id.phone);
        InputPassword = (EditText) findViewById(R.id.password);

        RememberMeCbox = (CheckBox) findViewById(R.id.remember_me_cbox);
        CourierLogin = (Button) findViewById(R.id.login_button);
        CourierSignup = (Button) findViewById(R.id.signup_button);
        loadingBar = new ProgressDialog(this);

        Paper.init(this);




       CourierLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LoginCourier();

//                Intent intent=new Intent(LoginActivity.this,HomeActivity.class);
//                Toast.makeText(LoginActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
//                startActivity(intent);
            }
        });

        CourierSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(CourierLoginActivity.this,CourierCreateAccount.class);
                startActivity(intent);
            }
        });


    }

    private void LoginCourier() {

        String phone = InputPhoneNumber.getText().toString();
        String password = InputPassword.getText().toString();

        if (TextUtils.isEmpty(phone))
        {
            Toast.makeText(this, "Please enter your phone number...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please enter your password...", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar = new ProgressDialog(this);
            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();


            AllowAccessToAccount(phone, password);
        }
    }

    private void AllowAccessToAccount(final String phone, final String password)
    {
        if(RememberMeCbox.isChecked())
        {
            Paper.book().write(Prevalent.UserPhoneKey, phone);
            Paper.book().write(Prevalent.UserPasswordKey, password);
        }


        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();


        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.child(parentDbName).child(phone).exists()) {

                    Courier courierData = dataSnapshot.child(parentDbName).child(phone).getValue(Courier.class);//assign to userData to user object from particular phone

                    if (courierData.getPhonenumber().equals(phone)) {

                        if (courierData.getPassword().equals(password)) {

                            if (parentDbName.equals("Courier")) {

                                Toast.makeText(CourierLoginActivity.this, "Logged in Successfully...", Toast.LENGTH_SHORT).show();
                                //  loadingBar.dismiss();

                                Intent intent = new Intent(CourierLoginActivity.this, CourierDashboardActivity.class);
                                CourierPrevalent.currentOnlineUser = courierData;
                                startActivity(intent);
                            }
                        }
                        else
                        {
                            // loadingBar.dismiss();
                            Toast.makeText(CourierLoginActivity.this, "Password is incorrect.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else
                {
                    Toast.makeText(CourierLoginActivity.this, "Account with this number does not exists.", Toast.LENGTH_SHORT).show();
                    //   loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}