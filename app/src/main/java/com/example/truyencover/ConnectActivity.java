package com.example.truyencover;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ConnectActivity extends AppCompatActivity {

    private EditText edit_text;
    private Button submit_button;
    private String ipServer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        // init
        edit_text = findViewById(R.id.edit_text);
        submit_button = findViewById(R.id.submit_button);

        // prefs
        SharedPreferences prefs = getSharedPreferences("truyencover", MODE_PRIVATE);
        String IPSERVER = prefs.getString("key_ip", null);

        // kiểm tra có lưu prefs không
        if(IPSERVER != null){
            Intent intent = new Intent(ConnectActivity.this, MainActivity.class);
            intent.putExtra("IPSERVER", IPSERVER);
            startActivity(intent);
            finish();
        }

        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ipServer = edit_text.getText().toString().trim();
                if(ipServer.isEmpty()){
                    Toast.makeText(ConnectActivity.this, "Vui lòng nhập IP SERVER", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(ConnectActivity.this, MainActivity.class);
                    intent.putExtra("IPSERVER",ipServer);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}