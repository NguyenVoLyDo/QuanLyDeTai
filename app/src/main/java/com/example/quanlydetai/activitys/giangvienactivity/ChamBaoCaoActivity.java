package com.example.quanlydetai.activitys.giangvienactivity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlydetai.R;
import com.example.quanlydetai.adapters.ChamBaoCaoAdapter;
import com.example.quanlydetai.models.BaoCao;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ChamBaoCaoActivity extends AppCompatActivity {

    private ChamBaoCaoAdapter adapter;
    private List<BaoCao> baoCaoList;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cham_bao_cao);

        RecyclerView recyclerView = findViewById(R.id.recyclerChamBaoCao);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        baoCaoList = new ArrayList<>();
        boolean isGiangVien = true;
        adapter = new ChamBaoCaoAdapter(this, baoCaoList, isGiangVien);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        loadBaoCao();
    }

    private void loadBaoCao() {
        db.collection("baocao")
                .whereEqualTo("trangThai", "Chưa chấm") // chỉ lấy báo cáo Đã duyệt
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    baoCaoList.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        BaoCao bc = doc.toObject(BaoCao.class);
                        bc.setDeTaiId(doc.getId()); // lưu id document để cập nhật điểm
                        baoCaoList.add(bc);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Lỗi tải dữ liệu: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }
}