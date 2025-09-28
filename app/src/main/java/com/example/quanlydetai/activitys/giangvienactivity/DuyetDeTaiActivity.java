package com.example.quanlydetai.activitys.giangvienactivity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlydetai.R;
import com.example.quanlydetai.adapters.DuyetDeTaiAdapter;
import com.example.quanlydetai.models.DeTai;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class DuyetDeTaiActivity extends AppCompatActivity {

    private DuyetDeTaiAdapter adapter;
    private List<DeTai> deTaiList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duyet_de_tai);

        RecyclerView recyclerView = findViewById(R.id.recyclerDeTaiGV);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        deTaiList = new ArrayList<>();
        adapter = new DuyetDeTaiAdapter(this, deTaiList);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        loadDeTai();
    }

    private void loadDeTai() {
        db.collection("detai")
                .whereEqualTo("trangThai", "pending")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    deTaiList.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        DeTai dt = doc.toObject(DeTai.class);
                        dt.setDeTaiId(doc.getId()); // lấy document ID
                        deTaiList.add(dt);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Lỗi tải dữ liệu: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }
}