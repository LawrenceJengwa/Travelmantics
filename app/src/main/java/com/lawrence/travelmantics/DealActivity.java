package com.lawrence.travelmantics;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DealActivity extends AppCompatActivity {
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private EditText txtTitle, txtPrice, txtDescription;
    TravelDeal deal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FbUtils.openFbReference("traveldeals", this);
        mFirebaseDatabase = FbUtils.mDb;
        mDatabaseReference = FbUtils.mDbRef;
        txtTitle = findViewById(R.id.txtTitle);
        txtDescription = findViewById(R.id.txtDescription);
        txtPrice = findViewById(R.id.txtPrice);
        Intent i = getIntent();
        TravelDeal deal = (TravelDeal) i.getSerializableExtra("Deal");
        if (deal == null){
            deal = new TravelDeal();
        }
        this.deal = deal;
        txtTitle.setText(deal.getTitle());
        txtDescription.setText(deal.getDescription());
        txtPrice.setText(deal.getPrice());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_menu:
            saveDeal();
            Toast.makeText(this, "Deal has been saved", Toast.LENGTH_LONG).show();
            clean();
            backToList();
            case R.id.delete_menu:
                deleteDeal();
                Toast.makeText(this, "Deal Deleted", Toast.LENGTH_SHORT).show();
                backToList();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu, menu);
        return true;
    }
    private void saveDeal(){
        deal.setTitle(txtTitle.getText().toString());
        deal.setDescription(txtDescription.getText().toString());
        deal.setPrice(txtPrice.getText().toString());
        if (deal.getId() == null) {
            mDatabaseReference.push().setValue(deal);
        }else {
            mDatabaseReference.child(deal.getId()).setValue(deal);
        }
    }
    private void deleteDeal(){
     if (deal == null){
         Toast.makeText(this, "Save deal before deleting", Toast.LENGTH_SHORT).show();
         return;
     }
     mDatabaseReference.child(deal.getId()).removeValue();
    }
    private void backToList(){
        Intent i = new Intent(this, ListActivity.class);
        startActivity(i);
    }
    private void clean(){
        txtTitle.setText("");
        txtDescription.setText("");
        txtPrice.setText("");
        txtTitle.requestFocus();

    }
}
