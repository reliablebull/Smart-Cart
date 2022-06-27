package com.example.hwang.smartcart.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.hwang.smartcart.R;
import com.example.hwang.smartcart.classModels.Setting;
import com.example.hwang.smartcart.classModels.database.DBManagerClass;

// 상품 상세 정보 페이지
public class ProductInfoActivity extends AppCompatActivity {

    ImageLoader imageLoader1;
    NetworkImageView networkImageView ;
    TextView txt_product_name;
    TextView txt_product_price;
    TextView txt_amount;
    TextView txt_result;

    Button btn_amoun_up;
    Button btn_amoun_down;
    Button btn_get_product;

    String URL = Setting.URL + "uploads/";

    //"http://192.168.0.2;

    // 상품 정보
    int AMOUNT = 0;
    String imgurl;
    String price;
    String name;

    //DB
    DBManagerClass dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info);

        Intent intent = getIntent();
         imgurl = intent.getStringExtra("URL"); // 상품 이미지 URL
         name = intent.getStringExtra("NAME"); // 상품명
         price = intent.getStringExtra("PRICE"); // 상품가격

        // UI 연결
        Init();
        txt_product_name.setText(name);

        txt_product_price.setText(price);

        // volley를 이용한 이미지 로딩
        imageLoader1 = ServerImageParseAdapter.getInstance(this).getImageLoader();

        imageLoader1.get(URL+ imgurl,
                ImageLoader.getImageListener(
                        networkImageView,//Server Image
                        R.mipmap.ic_launcher,//Before loading server image the default showing image.
                        android.R.drawable.ic_dialog_alert //Error image if requested image dose not found on server.
                )
        );

        networkImageView.setImageUrl(URL+ imgurl, imageLoader1);
    }

    void Init()
    {
        networkImageView = findViewById(R.id.img_product);
        txt_product_name = findViewById(R.id.txt_product_name);
        txt_product_price = findViewById(R.id.txt_product_price);
        txt_amount = findViewById(R.id.txt_amount);
        txt_result = findViewById(R.id.txt_result);

        btn_amoun_up = findViewById(R.id.btn_amount_up);
        btn_amoun_down = findViewById(R.id.btn_amount_down);
        btn_get_product = findViewById(R.id.btn_get_product);

        // 담기
        btn_get_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbManager = new DBManagerClass(ProductInfoActivity.this, Setting.DB_NAME_CART, null , 1);
                dbManager.insert(imgurl ,name, price , AMOUNT+"");
                dbManager.close();
                Toast.makeText(getApplicationContext() , "담기 완료." , Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        // 수량 증가
        btn_amoun_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AMOUNT++;
                InfoCalc();
            }
        });
        // 수량감소
        btn_amoun_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(AMOUNT == 0)
                    return;

                AMOUNT--;

                InfoCalc();
            }
        });
    }

    // 상품 정보 계산
    void InfoCalc()
    {
        int t_price = Integer.parseInt(price);
        String str = "수량을 선택해주세요.";
        txt_amount.setText(  AMOUNT +"");

        if(AMOUNT > 0)
            str = "수량 : " + AMOUNT +" 개 , 합계 : " + (t_price * AMOUNT) + " 원";

            txt_result.setText(str);
    }
}
