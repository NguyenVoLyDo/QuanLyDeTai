package com.example.quanlydetai.activitys.giangvienactivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import com.example.quanlydetai.R;
import com.example.quanlydetai.activitys.studentactivity.ThongBaoActivity;

public class GVMainActivity extends AppCompatActivity {
    private Button btnDuyetDeTai, btnChamBaoCao, btnGuiThongBao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gvmain);

        String maGV = getIntent().getStringExtra("maGV");

        btnDuyetDeTai = findViewById(R.id.btnDuyetDeTai);
        btnChamBaoCao = findViewById(R.id.btnChamBaoCao);
        btnGuiThongBao = findViewById(R.id.btnGuiThongBao);

        btnDuyetDeTai.setOnClickListener(v ->{
                Intent i = new Intent(this, DuyetDeTaiActivity.class);
                i.putExtra("maGV", maGV);
                startActivity(i);
            });

        btnChamBaoCao.setOnClickListener(v ->{
            Intent i = new Intent(this, ChamBaoCaoActivity.class);
            i.putExtra("maGV", maGV);
            startActivity(i);
        });

        btnGuiThongBao.setOnClickListener(v ->{
            Intent i = new Intent(this, GuiThongBaoActivity.class);
            i.putExtra("maGV", maGV);
            startActivity(i);
        });
    }
}