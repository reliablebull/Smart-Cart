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

public class CartListActivity extends AppCompatActivity {


    ImageView btn_back;
    RecyclerView recyclerView;
    RecyclerViewAdapterShoppingList recyclerViewadapter;
    RecyclerView.LayoutManager recyclerViewlayoutManager;
    SwipeController swipeController = null;
    TextView txt_empty_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_list);

        Init(); // UI SET
        //getData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(recyclerView != null)
            getData();
    }

    // DB에서 DATA 가져오기
    void getData()
    {
        // DB 객체 선언
        DBManagerClass dbManager  = new DBManagerClass(this, Setting.DB_NAME_LIST, null , 1);
        // DATA를 담을 LIST 선언
        List<GetDataAdapter> GetDataAdapter1 = dbManager.getData();
        recyclerViewadapter = new RecyclerViewAdapterShoppingList(GetDataAdapter1, this); // GetDataAdapter1리스트 recyclerViewadapter 에 연걸
        if(GetDataAdapter1.isEmpty()) // 리스트가 비어있다면
        {
            Log.e("EMPTY" , "OK");
            recyclerView.setVisibility(View.GONE);  // recyclerView를 숨김
            txt_empty_view.setVisibility(View.VISIBLE); // '선택된 리스트가 없다'는 textview 보여줌.
        }
        else // 리스트가 있다면
        {
            Log.e("EMPTY" , "No");
            recyclerView.setVisibility(View.VISIBLE); // recyclerView를 보여줌
            txt_empty_view.setVisibility(View.GONE); // '선택된 리스트가 없다'는 textview 숨김
        }

        // 위에서 생성한 recyclerViewadapter 를 recyclerView에 SET
        recyclerView.setAdapter(recyclerViewadapter);

        // DB객체 삭제
        dbManager.close();
    }

    // UI 연결
    void Init()
    {

        btn_back = findViewById(R.id.btn_back);
        recyclerView = findViewById(R.id.recyclerview_shopping_list);
        txt_empty_view = findViewById(R.id.txt_empty_view);

        recyclerView.setHasFixedSize(true);

        recyclerViewlayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(recyclerViewlayoutManager);

        // recyclerview 아이템을 왼쪽으로 밀었을때 효과를 주는 swipcontrol 생성
        swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onRightClicked(int position) { // 삭제 버튼이 눌렸다면

                // DB에서 삭제
                DBManagerClass dbManager  = new DBManagerClass(CartListActivity.this, Setting.DB_NAME_LIST, null , 1);
                dbManager.delete(recyclerViewadapter.getDataAdapter.get(position).getPrimaryKey());
                dbManager.close();
                // 해당 아이템 recyclerviewAdapter에서 삭제
                recyclerViewadapter.getDataAdapter.remove(position);

                // recyclerview에 현재 리스트가 변경되었다고 알려줌
                recyclerViewadapter.notifyItemRemoved(position);
                recyclerViewadapter.notifyItemRangeChanged(position, recyclerViewadapter.getItemCount());

                // 리스트가 비어있다면
                if(recyclerViewadapter.getDataAdapter.isEmpty())
                {
                    Log.e("EMPTY" , "OK");
                    recyclerView.setVisibility(View.GONE); // recyclerView를 숨김
                    txt_empty_view.setVisibility(View.VISIBLE);// '선택된 리스트가 없다'는 textview 보여줌.
                }
            }
        });

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(recyclerView);

        // recyclerView에 슬라이드 데코레이션 적용
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
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
