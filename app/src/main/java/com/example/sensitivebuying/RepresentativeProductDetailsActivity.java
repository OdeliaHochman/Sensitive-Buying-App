package com.example.sensitivebuying;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RepresentativeProductDetailsActivity extends AppCompatActivity {

    private TextView productName,companyName,weight,productDetalis,barcode;
    private ImageView productImage;
    private Button btnUpdate,btnDelete;
    final String activity = " RepresentativeProductDetailsActivity";
    private String productNameS,companyNameS,weightS,barcodeS;
    private FirebaseDatabase firebaseDatabase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("debug",activity);
        setContentView(R.layout.activity_representative_product_details);
        firebaseDatabase = FirebaseDatabase.getInstance();

        productNameS = getIntent().getStringExtra("product name");
        companyNameS = getIntent().getStringExtra("company name");
        weightS = getIntent().getStringExtra("weight");
        barcodeS = getIntent().getStringExtra("barcode");

        productName = (TextView) findViewById(R.id.product_name_Adetails);
        productName.setText(productNameS);
        companyName = (TextView) findViewById(R.id.company_name_Adetails);
        companyName.setText(companyNameS);
        weight = (TextView) findViewById(R.id.product_weight_name_Adetails);
        weight.setText(weightS);
        productDetalis = (TextView) findViewById(R.id.product_details_name_Adetails);
        barcode = (TextView) findViewById(R.id.barcode_Adetails);
        barcode.setText(barcodeS);

        productImage = (ImageView)findViewById(R.id.product_image_Adetails);
        btnUpdate = (Button)findViewById(R.id.btnUpdateProDet);
        btnDelete = (Button)findViewById(R.id.btnDeleteProDet);

        setDetails(barcodeS);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RepresentativeProductDetailsActivity.this, RepresentativeAddProductActivity.class);
                intent.putExtra("barcode",barcodeS);
                startActivity(intent);
                finish();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FirebaseProductsHelper().deleteProduct(barcodeS, new FirebaseProductsHelper.DataStatus() {
                    @Override
                    public void DataIsLoaded(List<Product> productsList, List<String> keys) {

                    }

                    @Override
                    public void DataIsInserted() {

                    }

                    @Override
                    public void DataIsUpdated() {

                    }

                    @Override
                    public void DataIsDeleted() {
                        Toast.makeText(RepresentativeProductDetailsActivity.this,"המוצר הוסר בהצלחה" , Toast.LENGTH_LONG).show();
                        finish();return;
                    }
                });
            }
        });

    }

    private void setDetails (String barcode) {

        DatabaseReference reference = firebaseDatabase.getReference("Products").child(barcode);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Product p = dataSnapshot.getValue(Product.class);
                productDetalis.setText(p.getProductDescription());
                Picasso.get().load(p.getUrlImage()).into(productImage);
                //sensitivelist
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
