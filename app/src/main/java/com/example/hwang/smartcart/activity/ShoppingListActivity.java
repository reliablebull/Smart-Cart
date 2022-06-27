package com.example.hwang.smartcart.activity;

import android.content.Intent;
import android.graphics.Canvas;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hwang.smartcart.R;
import com.example.hwang.smartcart.classModels.Setting;
import com.example.hwang.smartcart.classModels.database.DBManagerClass;

import java.util.List;

public class ShoppingListActivity extends AppCompatActivity {

    ImageView btn_add;
    ImageView btn_back;
    RecyclerView recyclerView;
    RecyclerViewAdapterShoppingList recyclerViewadapter;
    RecyclerView.LayoutManager recyclerViewlayoutManager;
    SwipeController swipeController = null;
    TextView txt_empty_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        Init();
        //getData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(recyclerView != null)
            getData();
    }

    void getData()
    {
        DBManagerClass dbManager  = new DBManagerClass(this, Setting.DB_NAME_CART, null , 1);
        List<GetDataAdapter> GetDataAdapter1 = dbManager.getData();
        recyclerViewadapter = new RecyclerViewAdapterShoppingList(GetDataAdapter1, this);
        if(GetDataAdapter1.isEmpty())
        {
            Log.e("EMPTY" , "OK");
            recyclerView.setVisibility(View.GONE);
            txt_empty_view.setVisibility(View.VISIBLE);
        }
        else
        {
            Log.e("EMPTY" , "No");
            recyclerView.setVisibility(View.VISIBLE);
            txt_empty_view.setVisibility(View.GONE);
        }

//        Toast.makeText(this , GetDataAdapter1.get(0).getProductPrice() , Toast.LENGTH_SHORT).show();
        recyclerView.setAdapter(recyclerViewadapter);

        dbManager.close();
    }


    void Init()
    {
        btn_add = findViewById(R.id.btn_add);
        btn_back = findViewById(R.id.btn_back);
        recyclerView = findViewById(R.id.recyclerview_shopping_list);
        txt_empty_view = findViewById(R.id.txt_empty_view);

        recyclerView.setHasFixedSize(true);

        recyclerViewlayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(recyclerViewlayoutManager);

        swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onRightClicked(int position) {

                DBManagerClass dbManager  = new DBManagerClass(ShoppingListActivity.this, Setting.DB_NAME_CART, null , 1);
                dbManager.delete(recyclerViewadapter.getDataAdapter.get(position).getPrimaryKey());
                dbManager.close();
                recyclerViewadapter.getDataAdapter.remove(position);
                recyclerViewadapter.notifyItemRemoved(position);
                recyclerViewadapter.notifyItemRangeChanged(position, recyclerViewadapter.getItemCount());

                //Log.e("POSITION",recyclerViewadapter.getDataAdapter.get(position).getPrimaryKey()+"");
                if(recyclerViewadapter.getDataAdapter.isEmpty())
                {
                    Log.e("EMPTY" , "OK");
                    recyclerView.setVisibility(View.GONE);
                    txt_empty_view.setVisibility(View.VISIBLE);
                }
            }
        });

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(recyclerView);

        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });

        // 제품 추가 btn
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext() , ProductListActivity.class);

                startActivity(intent);

                //ProductListActivity productListActivity = new ProductListActivity();

            }
        });

        // 뒤로가기 btn
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
