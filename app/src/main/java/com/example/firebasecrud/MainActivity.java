package com.example.firebasecrud;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

  private   RecyclerView recyclerview;

   private ArrayList<Food> arrayList;

   private Food food;

    private DatabaseReference databaseReference;

    private ValueEventListener eventListener;

    private ProgressDialog progressDialog;

    private EditText edtsearch;

    private Adapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerview = (RecyclerView) findViewById(R.id.recyclerView);

        edtsearch = (EditText)findViewById(R.id.edtsearch);



        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this,1);

        recyclerview.setLayoutManager(gridLayoutManager);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Items ......");


        arrayList = new ArrayList<>();

       /* food = new Food("Beryani","Karachi Speacial Beryani with mutton and beef","Rs.400",R.drawable.download);
        arrayList.add(food);
        food = new Food("Beryani","Karachi Speacial Beryani with mutton and beef","Rs.400",R.drawable.download);
        arrayList.add(food);
        food = new Food("Beryani","Karachi Speacial Beryani with mutton and beef","Rs.400",R.drawable.download);
        arrayList.add(food);
        food = new Food("Beryani","Karachi Speacial Beryani with mutton and beef","Rs.400",R.drawable.download);
        arrayList.add(food);
        food = new Food("Beryani","Karachi Speacial Beryani with mutton and beef","Rs.400",R.drawable.download);
        arrayList.add(food);
        food = new Food("Beryani","Karachi Speacial Beryani with mutton and beef","Rs.400",R.drawable.download);
        arrayList.add(food);*/

         adapter = new Adapter(MainActivity.this,arrayList);

        recyclerview.setAdapter(adapter);




        databaseReference = FirebaseDatabase.getInstance().getReference("Recipe");

        progressDialog.show();

        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                arrayList.clear();

                for(DataSnapshot itemSnapshot : snapshot.getChildren()){

                    Food foodData = itemSnapshot.getValue(Food.class);

                    foodData.setKey(itemSnapshot.getKey());

                    arrayList.add(foodData);
                }

                adapter.notifyDataSetChanged();

                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                progressDialog.dismiss();

            }
        });

        edtsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                filter(editable.toString());

            }
        });
    }

    private void filter(String txt) {

        ArrayList<Food> filterList = new ArrayList<>();

        for(Food item : arrayList){

            if (item.getItemName().toLowerCase().contains(txt.toLowerCase())){

                filterList.add(item);
            }
        }

        adapter.filteredList(filterList);
    }


    public void btnuploadactivity(View view) {

        startActivity(new Intent(this,UploadActivity.class));

    }
}