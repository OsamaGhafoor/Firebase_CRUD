package com.example.firebasecrud;

import static android.app.Activity.RESULT_OK;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class UploadActivity extends AppCompatActivity {

    private ImageView foodimage;
    private EditText edtname,edtdesc,edtprice;
    private Button btnimg,btnuploadimg;

    private TextView txtmsg;
    private Uri uri;

    private String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

       findId();

        btnimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);

                intent.setType("image/*");

                startActivityForResult(intent,1);
            }
        });

        btnuploadimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });
    }

    private void findId() {

        foodimage = (ImageView) findViewById(R.id.foodimage);

        edtname = (EditText) findViewById(R.id.edtname);

        edtdesc = (EditText) findViewById(R.id.edtdesc);

        edtprice = (EditText) findViewById(R.id.edtprice);

        btnimg = (Button) findViewById(R.id.btnselectimg);

        btnuploadimg = (Button) findViewById(R.id.btnuploadimg);

        txtmsg = (TextView) findViewById(R.id.txtmsg);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){

            uri = data.getData();
            foodimage.setImageURI(uri);

        }else{
            Toast.makeText(this, "You haven't picked image ", Toast.LENGTH_SHORT).show();

        }
    }

    public void uploadImage(){

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Recipe Uploading .....");
        progressDialog.show();

        StorageReference storageReference = FirebaseStorage.getInstance()
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
                Toast.makeText(UploadActivity.this, "failed", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

    }



    public void uploadRecipe(){

       DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Recipe");


        String myCurrentDateTime = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
        Food food = new Food(
                edtname.getText().toString(),
                edtdesc.getText().toString(),
                edtprice.getText().toString(),
                imageUrl
        );
        databaseReference.child(myCurrentDateTime).setValue(food);

       txtmsg.setText("Recipe Successfully Saved!");
        edtname.setText("");
        edtdesc.setText("");
        edtprice.setText("");
    }
}