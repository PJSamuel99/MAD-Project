package com.example.ecommerceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerceapp.Prevalent.CourierPrevalent;
import com.example.ecommerceapp.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


public class CourierManageAccount extends AppCompatActivity {

    private EditText CourierPhoneUpdate, CourierEmailUpdate, CourierColomboSuburbUpdate;
    private Button UpdateButton, ToDeleteButton;
    private String Phone;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courier_manage_account);
        reference = FirebaseDatabase.getInstance().getReference("Courier");



        CourierPhoneUpdate = (EditText)findViewById(R.id.edit_phone);
        CourierEmailUpdate = (EditText)findViewById(R.id.edit_email);
        CourierColomboSuburbUpdate = (EditText)findViewById(R.id.edit_colombo_suburb);
        UpdateButton = (Button)findViewById(R.id.update_button);
        ToDeleteButton = (Button)findViewById(R.id.todelete_button);

        Phone = getIntent().getStringExtra("Phone");

        CourierInfoDisplay();

        UpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateOnlyCourierInfo();
            }
        });

        ToDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View view) {
                deleteCourierAccount(CourierPhoneUpdate);

            }
        });
    }

    private void updateOnlyCourierInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Courier");

        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("phonenumber", CourierPhoneUpdate.getText().toString());
        userMap.put("email", CourierEmailUpdate.getText().toString());
        userMap.put("colombosuburb", CourierColomboSuburbUpdate.getText().toString());
        ref.child(Phone).updateChildren(userMap);

        startActivity(new Intent(CourierManageAccount.this, CourierDashboardActivity.class));
        Toast.makeText(CourierManageAccount.this, "Courier Details Updated Successfully", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void CourierInfoDisplay() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Courier").child(Phone);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    String phonenumber         = snapshot.child("phonenumber").getValue().toString();
                    String email         = snapshot.child("email").getValue().toString();
                    String colombosuburb       =snapshot.child("colombosuburb").getValue().toString();

                    CourierPhoneUpdate.setText(phonenumber);
                    CourierEmailUpdate.setText(email);
                    CourierColomboSuburbUpdate.setText(colombosuburb);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void deleteCourierAccount(final EditText u_phone) {

        final DatabaseReference userListRef = FirebaseDatabase.getInstance().getReference().child("Courier").child(CourierPrevalent.currentOnlineUser.getPhonenumber());


        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Are you sure you want to delete your account?");
        builder.setMessage("This action is irreversible.");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                userListRef.removeValue();

                Toast.makeText(CourierManageAccount.this, "Your account has been deactivated", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CourierManageAccount.this, MainActivity.class);
                startActivity(intent);

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog ad = builder.create();
        ad.show();
    }
}