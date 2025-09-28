package com.example.quanlydetai.activitys.studentactivity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlydetai.R;
import com.example.quanlydetai.adapters.KetQuaAdapter;
import com.example.quanlydetai.models.BaoCao;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class KetQuaActivity extends AppCompatActivity {
    private KetQuaAdapter adapter;
    private final List<BaoCao> baoCaoList = new ArrayList<>();
    private FirebaseFirestore db;

    private String sinhVienId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ket_qua);

        sinhVienId = getIntent().getStringExtra("maSV");

        RecyclerView recyclerView = findViewById(R.id.recyclerKetQua);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new KetQuaAdapter(baoCaoList, this); // truyền context để mở link
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        loadKetQua();
    }

    private void loadKetQua() {
        db.collection("baocao")
                .whereEqualTo("sinhVienId", sinhVienId)
                .whereEqualTo("trangThai", "Đã chấm")  // chỉ lấy báo cáo đã chấm
                .get()
                .addOnSuccessListener(query -> {
                    baoCaoList.clear();
                    for (QueryDocumentSnapshot doc : query) {
                        BaoCao bc = doc.toObject(BaoCao.class);
                        bc.setDeTaiId(doc.getId());
                        baoCaoList.add(bc);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Lỗi tải kết quả: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }
}