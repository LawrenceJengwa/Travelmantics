package com.lawrence.travelmantics;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {
    ArrayList<TravelDeal> deals;
    private FirebaseDatabase mFirebaseDb;
    private DatabaseReference mDbRef;
    private ChildEventListener mChildListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.insert_menu:
                Intent i = new Intent(this, DealActivity.class);
                startActivity(i);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onPause() {
        super.onPause();
       FbUtils.detachListener();
    }
    @Override
    protected void onResume() {
        super.onResume();
        FbUtils.openFbReference("traveldeals", this);
        mFirebaseDb = FbUtils.mDb;
        mDbRef = FbUtils.mDbRef;
        deals = FbUtils.mDeals;
        mChildListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                TravelDeal travelDeal = dataSnapshot.getValue(TravelDeal.class);
                Log.d("Deal ", travelDeal.getTitle());
                travelDeal.setId(dataSnapshot.getKey());
                deals.add(travelDeal);
                           }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mDbRef.addChildEventListener(mChildListener);
    }
}
