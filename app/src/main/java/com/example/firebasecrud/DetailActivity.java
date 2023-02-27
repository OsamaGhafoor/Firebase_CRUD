package com.example.firebasecrud;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DetailActivity extends AppCompatActivity {


   private TextView txtdesc,txtname,txtprice;

   private ImageView ivimage;

   private String key = "";

   private String imageUrl = "";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        txtdesc = (TextView) findViewById(R.id.txtdesc);

        ivimage = (ImageView) findViewById(R.id.ivimage);

        txtname =(TextView) findViewById(R.id.txtname);

        txtprice = (TextView) findViewById(R.id.txtprice);



        Bundle bundle = getIntent().getExtras();

        if(bundle!=null){
            txtdesc.setText(bundle.getString("Description"));
           // ivimage.setImageResource(bundle.getInt("Image"));

            key = bundle.getString("KeyValue");

            imageUrl = bundle.getString("Image");

            txtname.setText(bundle.getString("Name"));
            txtprice.setText(bundle.getString("Price"));

            Glide.with(this).load(bundle.getString("Image")).into(ivimage);

        }




    }

    public void deleteRecipe(View view) {

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Recipe");

        FirebaseStorage storage = FirebaseStorage.getInstance();

        StorageReference storageReference = storage.getReferenceFromUrl(imageUrl);

        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                databaseReference.child(key).removeValue();

                Toast.makeText(DetailActivity.this, "Recipe Deleted", Toast.LENGTH_SHORT).show();

                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        });
    }

    public void updateRecipe(View view) {

        startActivity(new Intent(getApplicationContext(),UpdateActivity.class)
                .putExtra("recipeName",txtname.getText().toString())
                .putExtra("recipeDescription",txtdesc.getText().toString())
                .putExtra("recipePrice",txtprice.getText().toString())
                .putExtra("recipeImage",imageUrl)
                .putExtra("key",key)
        );
    }
}