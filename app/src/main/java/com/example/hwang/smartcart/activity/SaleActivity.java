package com.example.hwang.smartcart.activity;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.hwang.smartcart.R;
import com.example.hwang.smartcart.classModels.Adapter;
import com.pm10.library.CircleIndicator;

public class SaleActivity extends AppCompatActivity {

    ImageView btn_back;

    Adapter adapter;
    ViewPager viewPager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale);

        Init();
    }

    void Init()
    {
        btn_back = findViewById(R.id.btn_back);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        viewPager = (ViewPager) findViewById(R.id.imageView2);
        adapter = new Adapter(this);
        viewPager.setAdapter(adapter); // 사진을 넘길 수 있게 viewPager 사용


        CircleIndicator circleIndicator = (CircleIndicator) findViewById(R.id.circle_indicator);
        circleIndicator.setupWithViewPager(viewPager);


    }
}
