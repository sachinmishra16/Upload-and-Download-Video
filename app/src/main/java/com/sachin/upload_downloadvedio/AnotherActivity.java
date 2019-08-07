package com.sachin.upload_downloadvedio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AnotherActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Upload> arrayList=new ArrayList<>();
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_another);

        recyclerView=findViewById(R.id.recyclerid);

        databaseReference= FirebaseDatabase.getInstance().getReference("Videos");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    Upload upload=snapshot.getValue(Upload.class);

                    arrayList.add(upload);


                }

                VideoAdapter adapter=new VideoAdapter(AnotherActivity.this,arrayList);

                recyclerView.setLayoutManager(new LinearLayoutManager(AnotherActivity.this));
                recyclerView.setAdapter(adapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
