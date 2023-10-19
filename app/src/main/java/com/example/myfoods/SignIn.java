package com.example.myfoods;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myfoods.Common.Common;
import com.example.myfoods.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class SignIn extends AppCompatActivity {
    EditText editPhone, editPassword;
    Button btnSignIn;
    com.rey.material.widget.CheckBox checkBoxRemember;

    TextView textForgotPass;

    FirebaseDatabase database;
    DatabaseReference table_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        editPhone = (MaterialEditText) findViewById(R.id.edit_phone);
        editPassword = (MaterialEditText) findViewById(R.id.edit_password);
        btnSignIn = (Button) findViewById(R.id.btn_signIn);
        checkBoxRemember = (CheckBox) findViewById(R.id.checkbox_remember);
        textForgotPass = (TextView) findViewById(R.id.text_forgotPass);

        Paper.init(this);

        database = FirebaseDatabase.getInstance();
        table_user = database.getReference("User");

        textForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showForgotPassDialog();
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Common.isConnectToInternet(getBaseContext())) {
                    if (checkBoxRemember.isChecked()){
                        Paper.book().write(Common.USER_KEY, editPhone.getText().toString());
                        Paper.book().write(Common.PASS_KEY, editPassword.getText().toString());
                    }

                    final ProgressDialog mDialog = new ProgressDialog(SignIn.this);
                    mDialog.setMessage("Please waiting..");
                    mDialog.show();
                    table_user.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child(editPhone.getText().toString()).exists()) {
                                mDialog.dismiss();
                                User user = dataSnapshot.child(editPhone.getText().toString()).getValue(User.class);
                                user.setPhone(editPhone.getText().toString());
                                if (user.getPassword().equals(editPassword.getText().toString())) {
                                    Intent homeIntent = new Intent(SignIn.this, Home.class);
                                    Common.currentUser = user;
                                    startActivity(homeIntent);
                                    finish();

                                    table_user.removeEventListener(this);
                                } else {
                                    Toast.makeText(SignIn.this, "Sign in failed!!", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                mDialog.dismiss();
                                Toast.makeText(SignIn.this, "User not exist in Database", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }else{
                    Toast.makeText(SignIn.this, "Check the connection!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

    private void showForgotPassDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Forgot Password");
        builder.setMessage("Input Code");

        LayoutInflater inflater = this.getLayoutInflater();
        View forgotView = inflater.inflate(R.layout.forgot_password_layout, null);

        builder.setView(forgotView);
        builder.setIcon(R.drawable.ic_baseline_security_24);

        MaterialEditText editPhone = (MaterialEditText) forgotView.findViewById(R.id.edit_phone);
        MaterialEditText editSecureCode = (MaterialEditText) forgotView.findViewById(R.id.edit_secureCode);

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.child(editPhone.getText().toString())
                                .getValue(User.class);

                        if (user.getSecureCode().equals(editSecureCode.getText().toString())){
                            Toast.makeText(SignIn.this, "Your password : " + user.getPassword(), Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(SignIn.this, "Code wrong!" + user.getPassword(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.show();


    }
}