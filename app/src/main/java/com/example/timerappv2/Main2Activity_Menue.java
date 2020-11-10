package com.example.timerappv2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Main2Activity_Menue extends AppCompatActivity {

    Button set_menue_ok;
    EditText set_scrolbar_max;
    EditText set_timer1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2__menue);

        set_menue_ok = (Button)findViewById(R.id.btn_menue_ok);
        set_scrolbar_max = (EditText) findViewById(R.id.et_max_Time);
        set_timer1 = (EditText) findViewById(R.id.et_timer1);

        set_menue_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_MainAktivity();
            }
        });
    }

    public void open_MainAktivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
