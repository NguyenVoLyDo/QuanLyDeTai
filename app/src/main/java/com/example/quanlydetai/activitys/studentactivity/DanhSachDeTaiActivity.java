package com.example.quanlydetai.activitys.studentactivity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlydetai.R;
import com.example.quanlydetai.adapters.DangKyDeTaiAdapter;
import com.example.quanlydetai.models.DeTai;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class DanhSachDeTaiActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DangKyDeTaiAdapter adapter;
    private List<DeTai> deTaiList = new ArrayList<>();
    private FirebaseFirestore db;

    private String sinhVienId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_de_tai);

        sinhVienId = getIntent().getStringExtra("maSV");

        recyclerView = findViewById(R.id.recyclerDeTai);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new DangKyDeTaiAdapter(deTaiList);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        loadDeTai();
    }

    private void loadDeTai() {
        db.collection("detai")
                .whereEqualTo("sinhVienId", sinhVienId)
                .get()
                .addOnSuccessListener(query -> {
                    deTaiList.clear();
                    for (QueryDocumentSnapshot doc : query) {
                        DeTai dt = doc.toObject(DeTai.class);
                        deTaiList.add(dt);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Lỗi tải đề tài: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }
}