package com.example.secureonemigrate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.ByteArrayOutputStream;

public class HomeActivity extends AppCompatActivity {

    Button next_user;
    FirebaseAuth mAuth;
    int flag=0;
    Button btn,btnUpload,btnCamera;
    ImageView iv;
    public Uri imguri;
    Employee obj;
    StorageReference storageReference;
    Uri downloadUri;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference().child("Employee");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        next_user = findViewById(R.id.next);
        mAuth = FirebaseAuth.getInstance();

        next_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                startActivity(new Intent(HomeActivity.this, MainActivity.class));
                finish();
            }
        });

        Log.e("TAG","OnCreate");
        storageReference= FirebaseStorage.getInstance().getReference();
        iv=findViewById(R.id.iv);
        btn=findViewById(R.id.btn);
        Intent i=getIntent();
        obj=i.getParcelableExtra("obj");

        Bitmap bitmap=GetBitmap();
        Uri qruri=getImageUri(this,bitmap);
        Log.e("TAG QR URI",""+qruri);
        iv.setImageURI(qruri);
//        obj.setQrLink(qruri.toString());
        FileUploader(qruri);


        StorageReference storage=storageReference.child(System.currentTimeMillis()+"."+getExtension(qruri));
        btnCamera=findViewById(R.id.btnCamera);
        btnUpload=findViewById(R.id.btnUpload);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Filechooser();
            }
        });
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FileUploader(imguri);
            }
        });
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Filechooser2();
            }
        });


    }

    private void Filechooser2() {
        Intent i=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        i.setType("image/*");
        startActivityForResult(i,2);
    }

    private String getExtension(Uri uri){
        ContentResolver cr=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }

    //intent to get image
    //in manifest add permission
    private void Filechooser() {
        Intent i=new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i,1);

    }

    //gives local image path and set it to imageview
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            Log.e("TAG","GALLERY");
            imguri=data.getData();
            iv.setImageURI(imguri);
            Log.e("TAG",""+data.getData().toString());

        }else if(requestCode==2 && resultCode==RESULT_OK){
            Log.e("TAG","CAMERA");
            Bitmap bm= (Bitmap) data.getExtras().get("data");
            imguri=getImageUri(this,bm);
            iv.setImageURI(imguri);
        }
    }
    private void FileUploader(Uri link) {

        if(link==null){
            Toast.makeText(this,"NULL",Toast.LENGTH_SHORT).show();
        }else{
            StorageReference ref=null;
            if(flag==0) {
                ref = storageReference.child("QR_"+obj.getEmpID()+"." +getExtension(link));
            }else if(flag==1){
                ref = storageReference.child("PH_"+obj.getEmpID() +"." + getExtension(link));
            }
            //        ref.putFile(imguri)
//                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        // Get a URL to the uploaded content
//                        String s=taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
//
//                        Log.e("TAG",s);
//                        DatabaseReference myRef = database.getReference("message");
//                        myRef.setValue(ref.toString());
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception exception) {
//                        // Handle unsuccessful uploads
//                        // ...
//                    }
//                });
        //to Upload file

            UploadTask uploadTask = ref.putFile(link);
            final StorageReference finalRef = ref;
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return finalRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        //uri to download file
                        downloadUri = task.getResult();
                        Log.e("TAG", "" + downloadUri);
                        if(flag==0){
                            obj.setQrLink(downloadUri.toString());
                            Log.e("TAG QR LINK",downloadUri.toString());
                            flag=1;
                        }
                        else if(flag==1) {
                            obj.setPhotoLink(downloadUri.toString());
                            myRef.child(obj.getEmpID()).setValue(obj);
                        }
//                    myRef.setValue(downloadUri.toString());
                    } else {
                        // Handle failures
                        // ...
                    }
                }
            });
        }


    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    private Bitmap GetBitmap(){
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        Bitmap bitmap;
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(obj.getEmpID(), BarcodeFormat.QR_CODE, 200, 200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            bitmap = barcodeEncoder.createBitmap(bitMatrix);
//            qr_view.setImageBitmap(bitmap);
//                    Picasso.get().load(bitmap).into(qr_view);
//            iv.setImageBitmap(bitmap);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }
}