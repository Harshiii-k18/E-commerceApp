package com.example.shoppingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;


import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shoppingapp.Model.Users;
import com.example.shoppingapp.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;




public class ShopLoginActivity extends AppCompatActivity {
    private EditText InputPhoneNumber, InputPassword;
    private Button LoginButton;
    private ProgressDialog loadingBar;
    private String parentDbName="Users";







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_login);
        LoginButton = (Button) findViewById(R.id.login_btn);
        InputPhoneNumber = (EditText) findViewById(R.id.login_phone_number_input);
        InputPassword = (EditText) findViewById(R.id.login_password_input);






        loadingBar = new ProgressDialog(this);
        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUser();

            }
        });


    }
    private void LoginUser(){
        String phone=InputPhoneNumber.getText().toString();
        String password=InputPassword.getText().toString();
         if(TextUtils.isEmpty(phone))
        {
            Toast.makeText(this, "Please write Your Phone Number" , Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please write Your Password" , Toast.LENGTH_SHORT).show();
        }
        else
         {
             loadingBar.setTitle("Login Account");
             loadingBar.setMessage("Please wait, While we are checking the credentials");
             loadingBar.setCanceledOnTouchOutside(false);
             loadingBar.show();
             AllowAccessToAccount(phone,password);
         }





    }
    private  void AllowAccessToAccount(final String phone, final String password)
    {


        final DatabaseReference RootRef;
        RootRef= FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(parentDbName).child(phone).exists())
                {
                    Users usersData=snapshot.child(parentDbName).child(phone).getValue(Users.class);

                    if(usersData.getPhone().equals(phone))
                    {
                        if(usersData.getPassword().equals(password)){


                           Toast.makeText(ShopLoginActivity.this,"Logged in successfully",Toast.LENGTH_SHORT).show();
                           loadingBar.dismiss();
                            Intent intent = new Intent(ShopLoginActivity.this, HomeActivity.class);
                            startActivity(intent);

                        }
                        else{
                            loadingBar.dismiss();
                            Toast.makeText(ShopLoginActivity.this,"Incorrect Password",Toast.LENGTH_SHORT).show();
                        }

                        }
                    }
                else{
                    Toast.makeText(ShopLoginActivity.this,"Account with this" +phone+ "number is doesn't exists",Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    
}