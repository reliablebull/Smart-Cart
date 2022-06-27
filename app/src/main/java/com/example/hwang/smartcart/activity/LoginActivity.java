package com.example.hwang.smartcart.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hwang.smartcart.R;
import com.example.hwang.smartcart.classModels.Setting;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    Button btn_reg; // 등록
    Button btn_login; // 로그인

    EditText edt_id;
    EditText edt_pw;

    String id;
    String pw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Init(); // UI연결
    }

    void Init()
    {
        btn_reg = findViewById(R.id.btn_reg);
        // 회원가입 버튼
        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext() , RegisterActivity.class);
                startActivity(intent);
            }
        });

        edt_id = findViewById(R.id.edt_id);
        edt_pw = findViewById(R.id.edt_pw);

        btn_login = findViewById(R.id.btn_login);

        // 로그인 버튼
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id = edt_id.getText().toString();
                pw = edt_pw.getText().toString();

                // id 또는 pw 입력란이 비어져 있다면
                if( id.matches("") || pw.matches(""))
                {
                    Toast.makeText(getApplicationContext() , "아이디 또는 패스워드를 입력하세요." , Toast.LENGTH_SHORT).show();
                    return;
                }

                // 정상적으로 id , pw가 입력되었다면 서버와 연결.
                registDB t = new registDB();
                t.execute();
            }
        });



    }

    // 로그인 버튼 클릭 후 서버와 로그인 정보 확인하는 AsynTask
    public class registDB extends AsyncTask<Void, Integer, String> {

        @Override
        protected String doInBackground(Void... unused) {

            /* 인풋 파라메터값 생성 */
            String param = "u_id=" + id + "&u_pw=" + pw +"";
            // 결과
            String data = "";
            try {
                /* 서버연결 */
                URL url = new URL(
                        Setting.URL+"login.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.connect();

                /* 안드로이드 -> 서버 파라메터값 전달 */
                OutputStream outs = conn.getOutputStream();
                outs.write(param.getBytes("UTF-8"));
                outs.flush();
                outs.close();

                /* 서버 -> 안드로이드 파라메터값 전달 */
                InputStream is = null;
                BufferedReader in = null;


                is = conn.getInputStream();
                in = new BufferedReader(new InputStreamReader(is), 8 * 1024);
                String line = null;
                StringBuffer buff = new StringBuffer();
                while ( ( line = in.readLine() ) != null )
                {
                    buff.append(line + "\n");
                }
                data = buff.toString().trim();
                Log.e("RECV DATA",data);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            // s가 1 -> 로그인 성공
            // 1이 아니면 로그인 실패
            if(s.equals("1"))
            {
                // 로그인 성공시 MainActivity로 이동
                Toast.makeText(getApplicationContext() , "환영합니다." , Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext() , MainActivity.class);
                startActivity(intent);

                finish();
            }
            else
            {
                Toast.makeText(getApplicationContext() , "아이디 또는 패스워드를 확인하세요" , Toast.LENGTH_SHORT).show();
            }

        }
    }
}
