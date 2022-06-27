package com.example.hwang.smartcart.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.hwang.smartcart.R;
import com.example.hwang.smartcart.classModels.database.DBManagerClass;
import com.example.hwang.smartcart.classModels.httpThread.HttpRequestThreadClass;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    Button btn_search_barcode;
    Button btn_shopping_list;
    Button btn_sale;
    Button btn_map;
    Button btn_cart_list;

    private View dialogView;

    private TextView txtBarcode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Init(); // UI 연결
    }



    void Init()
    {
        dialogView = getLayoutInflater().inflate(R.layout.dialog_show_information,null);
        txtBarcode = (TextView)dialogView.findViewById(R.id.txt_barcode);
        btn_search_barcode = findViewById(R.id.btn_search_barcode);
        btn_search_barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanQrcode();
            }
        });

        btn_shopping_list = findViewById(R.id.btn_shopping_list);
        btn_sale = findViewById(R.id.btn_sale);
        btn_map = findViewById(R.id.btn_map);
        btn_cart_list = findViewById(R.id.btn_cart_list);

        // 쇼핑리스트
        btn_shopping_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(MainActivity.this , ShoppingListActivity.class);
               startActivity(intent);
            }
        });

        // 할인
        btn_sale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this , SaleActivity.class);
                startActivity(intent);
            }
        });

        // 지도
        btn_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this , MapActivity.class);
                startActivity(intent);
            }
        });

        // 장바구니
        btn_cart_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this , CartListActivity.class);
                startActivity(intent);
            }
        });

    }

    // 스캐너 시작
    private void scanQrcode()
    {
        new IntentIntegrator(this).initiateScan();
    }


    // qrcode , barcode 판별
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);

        if(data == null)
            return;

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        String resultInformation = result.getContents(); // scan 결과 값
        String type =result.getFormatName();

        /*HttpRequestThreadClass httpRequestThreadClass = new HttpRequestThreadClass(resultInformation);
        httpRequestThreadClass.start();

        try {
            httpRequestThreadClass.join();
        } catch (Exception e) {}

        int code = httpRequestThreadClass.getResponseCode();
        Log.e("result", code + "");*/

        if(type.equals("QR_CODE")) // QR코드일때
            displayAlertQrcode(resultInformation , type);
        else // BARCODE 일때
            showBarcodeInfomation(resultInformation , type);

    }

    // Barcode 정보 알림 창 띄우기
    public void showBarcodeInfomation(final String resultUrl , String type)
    {
        // img 시간값으로 파일 명 생성
        //Bitmap bitBarcode = createImage(resultUrl,type);
        String time = DateFormat.getDateTimeInstance().format(new Date());

        Intent intent = new Intent(MainActivity.this , ShoppingProductInfoActivity.class);

        intent.putExtra("barcodeData" , resultUrl);
        intent.putExtra("barcodeType" , type);

        startActivity(intent);
    }

    // QRCODE 접속 알림 창 띄우기
    public void displayAlertQrcode(final String resultUrl , String type)
    {

        txtBarcode.setText(resultUrl);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("URL입니다.");
        alert.setView(dialogView); // alert View 지정
        alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Intent intent = new Intent(MainActivity.this , WebViewActivity.class);
                //intent.putExtra("URL", resultUrl);
                //startActivity(intent);
                ((ViewGroup)dialogView.getParent()).removeView(dialogView);
            }
        });
        alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((ViewGroup)dialogView.getParent()).removeView(dialogView);
            }
        });

        try {
            alert.show();
        }catch (Exception e)
        {
            Log.e("alert",e.toString());
        }
    }


}
