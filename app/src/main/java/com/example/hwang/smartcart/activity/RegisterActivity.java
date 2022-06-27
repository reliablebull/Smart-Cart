package com.example.hwang.smartcart.activity;

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

public class RegisterActivity extends AppCompatActivity {

    Button btn_send;
    EditText edt_name;
    EditText edt_birth;
    EditText edt_contact;
    EditText edt_id;
    EditText edt_pw;

    String name , contact , id , pw  , birth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Init();
    }

    void Init()
    {
        btn_send = findViewById(R.id.btn_send);

        edt_birth = findViewById(R.id.edt_birth);
        edt_contact = findViewById(R.id.edt_contact);
        edt_name = findViewById(R.id.edt_name);
        edt_id = findViewById(R.id.edt_id);
        edt_pw = findViewById(R.id.edt_pw);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               name = edt_name.getText().toString();
               contact = edt_contact.getText().toString();
               id = edt_id.getText().toString();
               pw = edt_pw.getText().toString();
               birth = edt_birth.getText().toString();

               Log.e("DD",name + " ");

               if(name.matches("") || contact.matches("") || id.matches("") || pw.matches("") || birth.matches(""))
               {
                   Toast.makeText(getApplicationContext() , "정보를 입력 해주세요." , Toast.LENGTH_SHORT).show();
                   return;
               }
                registDB t = new registDB();
               t.execute();

            }
        });
    }

    // 서버에 회원가입 정보를 넘겨줘서 회원등록하게 하는 AsyncTask
    public class registDB extends AsyncTask<Void, Integer, String> {

        @Override
        protected String doInBackground(Void... unused) {

            /* 인풋 파라메터값 생성 */
            String param = "u_id=" + id + "&u_pw=" + pw + "&u_name=" + name + "&u_contact=" + contact + "&u_birth="+birth+"";
            // 결과
            String data = "";
            try {
                /* 서버연결 */
                URL url = new URL(
                        Setting.URL+"register_customer.php");
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

            if(s.equals("0"))
            {
                Toast.makeText(getApplicationContext() , "등록 완료" , Toast.LENGTH_SHORT).show();
                finish();
            }
            else
            {
                Toast.makeText(getApplicationContext() , "등록된 아이디 입니다." , Toast.LENGTH_SHORT).show();
            }

        }
    }

}
