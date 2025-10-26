package com.example.quanlydetai.activitys.studentactivity;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.quanlydetai.R;
import com.example.quanlydetai.adapters.DeTaiGoiYAdapter;
import com.example.quanlydetai.models.DeTai;
import com.example.quanlydetai.models.DeTaiGoiY;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.*;

public class DangKyDeTaiActivity extends AppCompatActivity {

    private EditText edtTenDeTai, edtMoTa;
    private RecyclerView rvDeTaiGoiY;
    private SwipeRefreshLayout swipeRefreshGoiY;
    private FirebaseFirestore db;
    private String sinhVienId;

    private List<DeTaiGoiY> dsGoiY = new ArrayList<>();
    private DeTaiGoiYAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky_de_tai);

        sinhVienId = getIntent().getStringExtra("maSV");

        edtTenDeTai = findViewById(R.id.edtTenDeTai);
        edtMoTa = findViewById(R.id.edtMoTa);
        Button btnDangKy = findViewById(R.id.btnDangKy);

        rvDeTaiGoiY = findViewById(R.id.rvDanhSachDeTai);
        swipeRefreshGoiY = findViewById(R.id.swipeRefreshGoiY);

        rvDeTaiGoiY.setLayoutManager(new LinearLayoutManager(this));
        db = FirebaseFirestore.getInstance();

        adapter = new DeTaiGoiYAdapter(dsGoiY, (v, deTai) -> {
            edtTenDeTai.setText(deTai.getTenDeTai());
            edtMoTa.setText(deTai.getMoTa());
        });

        rvDeTaiGoiY.setAdapter(adapter);

        btnDangKy.setOnClickListener(v -> dangKyDeTai());
        swipeRefreshGoiY.setOnRefreshListener(this::loadDeTaiGoiY);

        loadDeTaiGoiY();
    }

    private void loadDeTaiGoiY() {
        swipeRefreshGoiY.setRefreshing(true);

        db.collection("detai_goiy")
                .get()
                .addOnSuccessListener(q -> {
                    dsGoiY.clear();
                    q.forEach(doc -> dsGoiY.add(doc.toObject(DeTaiGoiY.class)));
                    adapter.notifyDataSetChanged();
                    swipeRefreshGoiY.setRefreshing(false);

                    if(dsGoiY.isEmpty())
                        Toast.makeText(this, "Không có đề tài gợi ý!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    swipeRefreshGoiY.setRefreshing(false);
                    Toast.makeText(this, "Lỗi tải dữ liệu!", Toast.LENGTH_SHORT).show();
                });
    }

    private void dangKyDeTai() {
        String ten = edtTenDeTai.getText().toString().trim();
        String moTa = edtMoTa.getText().toString().trim();

        if (ten.isEmpty() || moTa.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        String id = db.collection("detai").document().getId();
        String ngay = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        DeTai deTai = new DeTai(id, ten, moTa, sinhVienId, "pending", ngay);

        db.collection("detai").document(id).set(deTai)
                .addOnSuccessListener(a -> Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
