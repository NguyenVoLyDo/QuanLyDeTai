package com.example.quanlydetai.activitys.studentactivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlydetai.R;

public class StudentMainActivity extends AppCompatActivity {
    private Button btnThongBao, btnDangKyDeTai, btnDanhSachDeTai, btnBaoCao, btnKetQua;
    private TextView txtWelcome;
//    private String maSV, hoTen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);

        String maSV = getIntent().getStringExtra("maSV");
        String hoTen = getIntent().getStringExtra("hoTen");

        txtWelcome = findViewById(R.id.txtWelcome);
        btnThongBao = findViewById(R.id.btnThongBao);
        btnDangKyDeTai = findViewById(R.id.btnDangKyDeTai);
        btnDanhSachDeTai = findViewById(R.id.btnDanhSachDeTai);
        btnBaoCao = findViewById(R.id.btnBaoCao);
        btnKetQua = findViewById(R.id.btnKetQua);


        if (hoTen != null) {
            txtWelcome.setText("Xin chào, " + hoTen + " (" + maSV + ")");
        }


        btnThongBao.setOnClickListener(v ->{
                Intent i = new Intent(this, ThongBaoActivity.class);
                i.putExtra("maSV", maSV);
                startActivity(i);
        });

        btnDangKyDeTai.setOnClickListener(v ->{
                Intent i = new Intent(this,DangKyDeTaiActivity.class);
                i.putExtra("maSV", maSV);
                startActivity(i);
        });

        btnDanhSachDeTai.setOnClickListener(v ->{
                Intent i = new Intent(this,DanhSachDeTaiActivity.class);
                i.putExtra("maSV", maSV);
                startActivity(i);
        });

        btnBaoCao.setOnClickListener(v ->{
                Intent i = new Intent(this,BaoCaoActivity.class);
                i.putExtra("maSV", maSV);
                startActivity(i);
            });


        btnKetQua.setOnClickListener(v ->{
                    Intent i = new Intent(this,KetQuaActivity.class);
                    i.putExtra("maSV", maSV);
                    startActivity(i);
                });
    }
}
