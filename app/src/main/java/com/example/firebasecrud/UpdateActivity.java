package com.example.firebasecrud;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.util.Calendar;

public class UpdateActivity extends AppCompatActivity {

    private ImageView foodImageup;

    private EditText edtnameup,edtdescup,edtpriceup;

    private Button btnselectImageup,btnupdateup;

    private TextView txtmsgup;

    private Uri uri;

    private String imageUrl,key,oldUrl,recipeName,recipeDesc,recipePrice;

    private DatabaseReference databaseReference;

    private StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        findId();

        Bundle bundle = getIntent().getExtras();

        if(bundle != null){

            Glide.with(UpdateActivity.this)
                    .load(bundle.getString("recipeImage"))
                    .into(foodImageup);

            edtnameup.setText(bundle.getString("recipeName"));

            edtdescup.setText(bundle.getString("recipeDescription"));

            edtpriceup.setText(bundle.getString("recipePrice"));

            key = bundle.getString("key");

            oldUrl = bundle.getString("recipeImage");

        }


        btnselectImageup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_PICK);

                intent.setType("image/*");

                startActivityForResult(intent,1);

            }
        });


        databaseReference = FirebaseDatabase.getInstance().getReference("Recipe").child(key);



        btnupdateup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                recipeName = edtnameup.getText().toString().trim();

                recipeDesc = edtdescup.getText().toString().trim();

                recipePrice = edtpriceup.getText().toString().trim();


                final ProgressDialog progressDialog = new ProgressDialog(UpdateActivity.this);
                progressDialog.setMessage("Recipe Updating .....");
                progressDialog.show();

                storageReference = FirebaseStorage.getInstance()
                        .getReference().child("RecipeImage")
                        .child(uri.getLastPathSegment());

                storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();

                        while (!uriTask.isComplete());

                        Uri uriImage = uriTask.getResult();

                        imageUrl = uriImage.toString();

                        uploadRecipe();

                        progressDialog.dismiss();


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UpdateActivity.this, "failed", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });



            }
        });

    }

    private void uploadRecipe() {



        Food food = new Food(
               recipeName,
                recipeDesc,
                recipePrice,
                imageUrl
        );
        databaseReference.setValue(food).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                StorageReference storageReferencenew = FirebaseStorage.getInstance().getReferenceFromUrl(oldUrl);
                storageReferencenew.delete();

                txtmsgup.setText("Recipe Successfully Updated!");
                Toast.makeText(UpdateActivity.this, "Recipe Successfully Updated!", Toast.LENGTH_SHORT).show();



            }
        });




    }

    private void findId() {

        foodImageup = (ImageView) findViewById(R.id.foodimageup);

        edtnameup = (EditText) findViewById(R.id.edtnameup);

        edtdescup = (EditText) findViewById(R.id.edtdescup);

        edtpriceup = (EditText) findViewById(R.id.edtpriceup);

        btnselectImageup = (Button) findViewById(R.id.btnselectimgup);

        btnupdateup = (Button) findViewById(R.id.btnupdateimgup);

        txtmsgup = (TextView) findViewById(R.id.txtmsgup);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){

            uri = data.getData();
            foodImageup.setImageURI(uri);

        }else{
            Toast.makeText(this, "You haven't picked image ", Toast.LENGTH_SHORT).show();

        }
    }


}