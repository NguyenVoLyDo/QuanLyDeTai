package com.example.quanlydetai.activitys.studentactivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlydetai.R;
import com.example.quanlydetai.models.DeTai;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DangKyDeTaiActivity extends AppCompatActivity {
    private EditText edtTenDeTai, edtMoTa;
    private Button btnDangKy;
    private FirebaseFirestore db;

    // Giả định sinh viên đã login, ta có mã sinh viên
    private String sinhVienId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky_de_tai);
        sinhVienId = getIntent().getStringExtra("maSV");

        edtTenDeTai = findViewById(R.id.edtTenDeTai);
        edtMoTa = findViewById(R.id.edtMoTa);
        btnDangKy = findViewById(R.id.btnDangKy);

        db = FirebaseFirestore.getInstance();

        btnDangKy.setOnClickListener(v -> dangKyDeTai());
    }

    private void dangKyDeTai() {
        String tenDeTai = edtTenDeTai.getText().toString().trim();
        String moTa = edtMoTa.getText().toString().trim();

        if (tenDeTai.isEmpty() || moTa.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        String ngayDangKy = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        // Tạo document mới trong Firestore
        String docId = db.collection("detai").document().getId();
        DeTai deTai = new DeTai(docId, tenDeTai, moTa, sinhVienId, "pending", ngayDangKy);

        db.collection("detai").document(docId).set(deTai)
                .addOnSuccessListener(aVoid ->
                        Toast.makeText(this, "Đăng ký đề tài thành công!", Toast.LENGTH_SHORT).show()
                )
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }
}