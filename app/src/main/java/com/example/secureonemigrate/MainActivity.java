package com.example.secureonemigrate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.le.AdvertiseData;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    EditText phone_num,emp_designation,emp_name;
    Button send_otp;
    DatabaseReference databaseReference;
    int pehleSeHai;
    CountDownLatch ct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        send_otp = findViewById(R.id.otp);
        emp_name = findViewById(R.id.pName);
        emp_designation = findViewById(R.id.pDes);
        phone_num = findViewById(R.id.pNum);
        pehleSeHai = 0;
        ct = new CountDownLatch(1);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        send_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = emp_name.getText().toString().toLowerCase().trim();
                String designation = emp_designation.getText().toString().trim();
                final String mobile = phone_num.getText().toString().trim();

                if(name.isEmpty() || name.length() < 5){
                    emp_name.setError("Enter valid name");
                    emp_name.requestFocus();
                    return;
                }

                if(designation.isEmpty()){
                    emp_designation.setError("Enter valid Designation");
                    emp_designation.requestFocus();
                    return;
                }

                if(mobile.isEmpty() || mobile.length() < 10){
                    phone_num.setError("Enter a valid mobile");
                    phone_num.requestFocus();
                    return;
                }


                final Query applesQuery = databaseReference.orderByChild("phoneNum").equalTo(mobile);

                applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot appleSnapshot : snapshot.getChildren()) {
                            Log.e("HERE2", appleSnapshot.getKey());
                            pehleSeHai = 1;
                            Log.e("pehleSeHai", ""+pehleSeHai);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });

//                try {
//                    ct.await();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }


                if(pehleSeHai == 0) {
                    Intent intent = new Intent(MainActivity.this, VerifyPhoneActivity.class);
                    intent.putExtra("name", name);
                    intent.putExtra("designation",designation);
                    intent.putExtra("mobile",mobile);
                    startActivity(intent);
                }else{
                    Toast.makeText(MainActivity.this, "4 baar ayega kya?", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void mCheckInforServer(String child){
        mReadDataOnce(child, new OnGetDataListener() {
            @Override
            public void onStart() {
                //DO SOME THING WHEN START GET DATA HERE
            }

            @Override
            public void onFailure(DatabaseError error) {

            }

            @Override
            public void onSuccess(DataSnapshot data) {
                //DO SOME THING WHEN GET DATA SUCCESS HERE
            }

        });
    }
    public void mReadDataOnce(String child, final OnGetDataListener listener) {
        listener.onStart();
        FirebaseDatabase.getInstance().getReference().child(child).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listener.onSuccess(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onFailure(databaseError);
            }
        });
    }

    }