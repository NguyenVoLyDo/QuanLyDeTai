package com.example.quanlydetai.activitys.giangvienactivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlydetai.R;
import com.example.quanlydetai.models.DeTaiGoiY;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SuaDeTaiGoiYActivity extends AppCompatActivity {

    private TextInputEditText edtTenDeTai, edtMoTa;
    private Button btnSave;
    private ProgressBar progressBar;
    private FirebaseFirestore db;
    private String deTaiGoiYId;
    private DocumentReference deTaiRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sua_de_tai_goi_y);

        // Khởi tạo views
        edtTenDeTai = findViewById(R.id.edtTenDeTai);
        edtMoTa = findViewById(R.id.edtMoTa);
        btnSave = findViewById(R.id.btnSave);
        progressBar = findViewById(R.id.progressBar);

        db = FirebaseFirestore.getInstance();

        // Nhận ID từ Intent
        deTaiGoiYId = getIntent().getStringExtra("deTaiGoiYId");
        if (deTaiGoiYId == null || deTaiGoiYId.isEmpty()) {
            Toast.makeText(this, "ID đề tài không hợp lệ.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        deTaiRef = db.collection("detai_goiy").document(deTaiGoiYId);

        loadDeTaiData();

        btnSave.setOnClickListener(v -> saveChanges());
    }

    private void loadDeTaiData() {
        progressBar.setVisibility(View.VISIBLE);
        btnSave.setEnabled(false);

        deTaiRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                DeTaiGoiY deTai = documentSnapshot.toObject(DeTaiGoiY.class);
                if (deTai != null) {
                    edtTenDeTai.setText(deTai.getTenDeTai());
                    edtMoTa.setText(deTai.getMoTa());
                }
            } else {
                Toast.makeText(this, "Không tìm thấy đề tài.", Toast.LENGTH_SHORT).show();
                finish();
            }
            progressBar.setVisibility(View.GONE);
            btnSave.setEnabled(true);
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Lỗi khi tải dữ liệu: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            finish();
        });
    }

    private void saveChanges() {
        String tenDeTai = edtTenDeTai.getText().toString().trim();
        String moTa = edtMoTa.getText().toString().trim();

        if (tenDeTai.isEmpty() || moTa.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đủ thông tin.", Toast.LENGTH_SHORT).show();
            return;
        }

        btnSave.setEnabled(false);


        deTaiRef.update("tenDeTai", tenDeTai, "moTa", moTa)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                    finish(); // Đóng Activity sau khi lưu thành công
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Lỗi khi cập nhật: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    btnSave.setEnabled(true); // Kích hoạt lại nút nếu có lỗi
                });
    }
}
