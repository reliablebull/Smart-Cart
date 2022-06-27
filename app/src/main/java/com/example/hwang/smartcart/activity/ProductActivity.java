package com.example.hwang.smartcart.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.example.hwang.smartcart.R;

import org.w3c.dom.Text;

public class ProductActivity extends AppCompatActivity {

    TextView txt_result;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        txt_result = findViewById(R.id.txt_result);


        Intent intent = getIntent();

        String barcoeNumber = intent.getStringExtra("barcodeData");

        txt_result.setText(barcoeNumber);


    }
}
