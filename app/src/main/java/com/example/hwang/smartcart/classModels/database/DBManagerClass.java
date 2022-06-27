package com.example.hwang.smartcart.classModels.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.example.hwang.smartcart.activity.GetDataAdapter;

import java.util.ArrayList;
import java.util.List;

public class DBManagerClass extends SQLiteOpenHelper {

    public DBManagerClass(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db)  // DB를 새로 생성할때 호출되는 함수 ( 생성자에서 입력받은 DB가 없을때만 호출)
    {

        // datetime : 접속 년월일시간 , url : 접속 url , img : QRCODE 이미지 경로
        String sql = "CREATE TABLE MyCart(" +
                " _id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " p_url TEXT," +
                " p_name TEXT," +
                " p_price TEXT," +
                " p_amount TEXT" +
                ");";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) // DB 업그레이드를 위해 버전이 변경될 때 호출되는 함수 ( DB를 변경하고 싶을때 호출 )
    {
    }

    private void exeQuery(String query) // query 실행
    {
        SQLiteDatabase db = getWritableDatabase();
        Log.e("exeQuery" , query);
        db.execSQL(query);
        db.close();
    }

    public void delete(String param) // data 삭제
    {
        String sql = "DELETE FROM MyCart WHERE _id = " +"'"+ param+"'"+";";
        exeQuery(sql);
    }

    public void insert(String p_url, String p_name, String p_price , String p_amount) // data 추가
    {
        SQLiteDatabase db = getWritableDatabase();
        String sql =
                "INSERT INTO MyCart(p_url,p_name,p_price,p_amount)VALUES(?,?,?,?);";

        SQLiteStatement statement = db.compileStatement(sql);
        statement.bindString(1,p_url);
        statement.bindString(2,p_name);
        statement.bindString(3,p_price);
        statement.bindString(4,p_amount);
        statement.executeInsert();
    }


    // data 요청시 돌려주는 기능
    public List<GetDataAdapter> getData()
    {
        List<GetDataAdapter> GetDataAdapter1 = new ArrayList<>();

        ArrayList<String> arrayList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM MyCart;" , null);
        while(cursor.moveToNext())
        {
            GetDataAdapter GetDataAdapter2 = new GetDataAdapter();

            String p_key = cursor.getString(0);
            String p_url = cursor.getString(1);
            String p_name = cursor.getString(2);
            String p_price = cursor.getString(3);
            String p_amount = cursor.getString(4);
            //arrayList.add(time+":::"+url+":::"+data);
 //           Log.e("datadd",data);
            Log.e("EE",p_key +"::"+p_url+"::"+p_name+"::"+p_price+"::"+p_amount);
            GetDataAdapter2.setPrimaryKey(p_key);
            GetDataAdapter2.setImageServerUrl(p_url);
            GetDataAdapter2.setImageTitleNamee(p_name);
            GetDataAdapter2.setProductPrice(p_price);
            GetDataAdapter2.setAmount(p_amount);

            GetDataAdapter1.add(GetDataAdapter2);
        }
        return GetDataAdapter1;
    }

}
