package com.example.quanlydetai.activitys.studentactivity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlydetai.R;
import com.example.quanlydetai.adapters.ThongBaoAdapter;
import com.example.quanlydetai.models.ThongBao;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ThongBaoActivity extends AppCompatActivity {
    private ThongBaoAdapter adapter;
    private final List<ThongBao> thongBaoList = new ArrayList<>();
    private FirebaseFirestore db;

    private String sinhVienId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_bao);

        sinhVienId = getIntent().getStringExtra("maSV");

        RecyclerView recyclerView = findViewById(R.id.recyclerThongBao);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ThongBaoAdapter(thongBaoList);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        loadThongBao();
    }

    private void loadThongBao() {
        db.collection("thongbao")
                .whereIn("nguoiNhan", java.util.Arrays.asList(sinhVienId, "all"))
                .get()
                .addOnSuccessListener(query -> {
                    thongBaoList.clear();
                    for (QueryDocumentSnapshot doc : query) {
                        ThongBao tb = doc.toObject(ThongBao.class);
                        thongBaoList.add(tb);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Lỗi tải thông báo: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }
}