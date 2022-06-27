package com.example.hwang.smartcart.activity;

import android.graphics.Canvas;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.hwang.smartcart.R;
import com.example.hwang.smartcart.classModels.Setting;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProductListActivity extends AppCompatActivity {

    List<GetDataAdapter> GetDataAdapter1;

    RecyclerView recyclerView;

    RecyclerView.LayoutManager recyclerViewlayoutManager;

    RecyclerViewAdapter recyclerViewadapter;

    // JSON 파싱을 위한 key값
    String GET_JSON_DATA_HTTP_URL = Setting.URL  + "getData.php";
    String JSON_IMAGE_TITLE_NAME = "p_name";
    String JSON_IMAGE_URL = "p_image";
    String JSON_PRODUCT_PRICE = "p_price";

    JsonArrayRequest jsonArrayRequest ;

    RequestQueue requestQueue ;

    SwipeController swipeController = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_product_list);

        GetDataAdapter1 = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_product);

        recyclerView.setHasFixedSize(true);

        recyclerViewlayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(recyclerViewlayoutManager);


        JSON_DATA_WEB_CALL(); // 서버에서 상품 list 요청

    }

    // 상품 리스트 가져오는 함수
    public void JSON_DATA_WEB_CALL(){

        // volley를 이용한 웹 서버 호출
        jsonArrayRequest = new JsonArrayRequest(GET_JSON_DATA_HTTP_URL,

                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        // 응답결과는 response에 JSONArray형식으로 반환
                        // 파싱하는 함수 호출
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

    // 응답받은 값을 파싱하는 함수
    public void JSON_PARSE_DATA_AFTER_WEBCALL(JSONArray array){

        for(int i = 0; i<array.length(); i++) {
            // 상품에 대한 객체 생성
            GetDataAdapter GetDataAdapter2 = new GetDataAdapter();

            JSONObject json = null;
            try {
                // 위에서 선언한 JSON 값을 key값에 맞게 파싱
                // JSON 형식은 key:value 이형식임.
                json = array.getJSONObject(i);
                Log.e("ERR",json.getString(JSON_IMAGE_TITLE_NAME));
                GetDataAdapter2.setImageTitleNamee(json.getString(JSON_IMAGE_TITLE_NAME));
                GetDataAdapter2.setImageServerUrl(json.getString(JSON_IMAGE_URL));
                GetDataAdapter2.setProductPrice(json.getString(JSON_PRODUCT_PRICE));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            // GetDataAdapter1 List에 저장
            GetDataAdapter1.add(GetDataAdapter2);
        }

        // recyclerviewadapter에 위에서 받은 GetDataAdpater1 매칭
        recyclerViewadapter = new RecyclerViewAdapter(GetDataAdapter1, this);
        //recyclerViewadapter.getDataAdapter.
        // recyclerview에 adpater 연결
        recyclerView.setAdapter(recyclerViewadapter);

        // 삭제 버튼이 나올수 있도록 swipe controller 연결
        swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onRightClicked(int position) {

                recyclerViewadapter.getDataAdapter.remove(position);
                recyclerViewadapter.notifyItemRemoved(position);
                recyclerViewadapter.notifyItemRangeChanged(position, recyclerViewadapter.getItemCount());
            }
        });

        //ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        //itemTouchhelper.attachToRecyclerView(recyclerView);

        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });
    }
}
