package com.sachin.upload_downloadvedio;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class MainActivity extends AppCompatActivity {

    VideoView videoView;
    Button choose,upload,download;
    Uri filepath;

    DatabaseReference databaseReference;
    StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        videoView=findViewById(R.id.uploadvideoViewid);
        upload=findViewById(R.id.uploadbuttonid);
        download=findViewById(R.id.downloadbuttonid);
        choose=findViewById(R.id.selectbuttonid);

        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent();
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,0);

            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UploadVideo();
            }
        });

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,AnotherActivity.class));
            }
        });


        storageReference= FirebaseStorage.getInstance().getReference("Stored videos/");
        databaseReference=FirebaseDatabase.getInstance().getReference("Videos");


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==0 &&resultCode==RESULT_OK&& data!=null) {

            filepath = data.getData();
            videoView.setVideoURI(filepath);
            videoView.start();


        }

        }

        public void UploadVideo()
        {

            if (filepath!=null)
            {

                final ProgressDialog dialog=new ProgressDialog(this);
                dialog.setMessage("Uploading...");
                dialog.show();

                StorageReference storageRef=storageReference.child(System.currentTimeMillis()+":"+
                        getFileExtension(filepath));

                storageRef.putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Toast.makeText(MainActivity.this, "successfuul", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();

                        Task<Uri> result=taskSnapshot.getMetadata().getReference().getDownloadUrl();

                        result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {


                                Upload upload=new Upload();

                                upload.setUrl(String.valueOf(uri));

                                String key=databaseReference.push().getKey();

                                databaseReference.child(key).setValue(upload);





                            }
                        });


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT).show();
                    }
                });



            }


        }

    public String getFileExtension(Uri uri)    // for extension of your file.//when called
    {

        ContentResolver cR=getContentResolver();        //
        MimeTypeMap mime=MimeTypeMap.getSingleton();   //singletone for designing purpose

        return mime.getExtensionFromMimeType(cR.getType(uri));


    }
}
