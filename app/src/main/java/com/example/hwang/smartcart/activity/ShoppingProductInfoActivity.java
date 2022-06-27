package com.example.hwang.smartcart.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.example.hwang.smartcart.R;
import com.example.hwang.smartcart.classModels.Setting;
import com.example.hwang.smartcart.classModels.database.DBManagerClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ShoppingProductInfoActivity extends AppCompatActivity {

    JsonArrayRequest jsonArrayRequest ;

    RequestQueue requestQueue ;

    String GET_JSON_DATA_HTTP_URL = Setting.URL  + "getDataSearch.php"; // URL

    String GET_IMAGE_URL = Setting.URL + "uploads/";

    String JSON_IMAGE_TITLE_NAME = "p_name";
    String JSON_IMAGE_URL = "p_image";
    String JSON_PRODUCT_PRICE = "p_price";

    String p_name;
    String p_image;
    String p_price;

    ImageLoader imageLoader1;
    NetworkImageView networkImageView ;
    TextView txt_product_name;
    TextView txt_product_price;
    TextView txt_amount;
    TextView txt_result;

    Button btn_amoun_up;
    Button btn_amoun_down;
    Button btn_get_product;

    // 상품 정보
    int AMOUNT = 0;

    //DB
    DBManagerClass dbManager;

    // 바코드 넘버
    String barcoeNumber;

    LinearLayout linearLayout ;

    TextView txt_empty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_product_info);

        Init();

        Intent intent = getIntent();

        barcoeNumber = intent.getStringExtra("barcodeData");

        JSON_DATA_WEB_CALL();
    }

    public void JSON_DATA_WEB_CALL(){

        jsonArrayRequest = new JsonArrayRequest(GET_JSON_DATA_HTTP_URL +"?number=" + barcoeNumber,

                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        JSON_PARSE_DATA_AFTER_WEBCALL(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(jsonArrayRequest);
    }

    public void JSON_PARSE_DATA_AFTER_WEBCALL(JSONArray array){

        for(int i = 0; i<array.length(); i++) {
            JSONObject json = null;
            try {
                json = array.getJSONObject(i);
                Log.e("ERR",json.getString(JSON_IMAGE_TITLE_NAME));
                p_name = json.getString(JSON_IMAGE_TITLE_NAME);
                p_image = json.getString(JSON_IMAGE_URL);
                p_price = json.getString(JSON_PRODUCT_PRICE);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(array.length() == 0)
        {
            Log.e("EMPTY" , "OK");
            linearLayout.setVisibility(View.GONE);
            txt_empty.setVisibility(View.VISIBLE);

            return;
        }

        txt_product_name.setText(p_name);
        txt_product_price.setText(p_price);

        imageLoader1 = ServerImageParseAdapter.getInstance(this).getImageLoader();

        imageLoader1.get(GET_IMAGE_URL+ p_image,
                ImageLoader.getImageListener(
                        networkImageView,//Server Image
                        R.mipmap.ic_launcher,//Before loading server image the default showing image.
                        android.R.drawable.ic_dialog_alert //Error image if requested image dose not found on server.
                )
        );

        networkImageView.setImageUrl(GET_IMAGE_URL+ p_image, imageLoader1);

        Log.e("IMAGEURL",GET_IMAGE_URL+ p_image);
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

        linearLayout = findViewById(R.id.linear_info);

        txt_empty = findViewById(R.id.txt_empty_view);

        // 담기
        btn_get_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbManager = new DBManagerClass(ShoppingProductInfoActivity.this, Setting.DB_NAME_LIST, null , 1);
                dbManager.insert(p_image ,p_name, p_price , AMOUNT+"");
                dbManager.close();
                Toast.makeText(getApplicationContext() , "담기 완료." , Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        btn_amoun_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AMOUNT++;
                InfoCalc();
            }
        });

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
        int t_price = Integer.parseInt(p_price);
        String str = "수량을 선택해주세요.";
        txt_amount.setText(  AMOUNT +"");
        if(AMOUNT > 0)
            str = "수량 : " + AMOUNT +" 개 , 합계 : " + (t_price * AMOUNT) + " 원";

        txt_result.setText(str);
    }
}
