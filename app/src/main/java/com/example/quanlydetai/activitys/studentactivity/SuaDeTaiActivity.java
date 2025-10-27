package com.example.quanlydetai.activitys.studentactivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.quanlydetai.R;
import com.example.quanlydetai.adapters.DeTaiGoiYAdapter;
import com.example.quanlydetai.models.DeTai;
import com.example.quanlydetai.models.DeTaiGoiY;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class SuaDeTaiActivity extends AppCompatActivity {

    private EditText edtTenDeTai, edtMoTa;
    private RecyclerView rvDeTaiGoiY;
    private SwipeRefreshLayout swipeRefreshGoiY;
    private FirebaseFirestore db;
    private String deTaiId;
    private DeTai deTai;
    private List<DeTaiGoiY> dsGoiY = new ArrayList<>();
    private DeTaiGoiYAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky_de_tai);

        deTaiId = getIntent().getStringExtra("deTaiId");

        edtTenDeTai = findViewById(R.id.edtTenDeTai);
        edtMoTa = findViewById(R.id.edtMoTa);
        Button btnLuu = findViewById(R.id.btnDangKy);

        rvDeTaiGoiY = findViewById(R.id.rvDanhSachDeTai);
        swipeRefreshGoiY = findViewById(R.id.swipeRefreshGoiY);
        rvDeTaiGoiY.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        adapter = new DeTaiGoiYAdapter(dsGoiY, (v, deTai) -> {
            edtTenDeTai.setText(deTai.getTenDeTai());
            edtMoTa.setText(deTai.getMoTa());
        });

        rvDeTaiGoiY.setAdapter(adapter);

        btnLuu.setOnClickListener(v -> updateData());
        swipeRefreshGoiY.setOnRefreshListener(this::loadDeTaiGoiY);

        loadDeTai();

        loadDeTaiGoiY();
    }

    private void loadDeTai() {
        db.collection("detai").document(deTaiId)
                .get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        deTai = doc.toObject(DeTai.class);
                        if (deTai != null) {
                            edtTenDeTai.setText(deTai.getTenDeTai());
                            edtMoTa.setText(deTai.getMoTa());
                        }
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Lỗi tải đề tài!", Toast.LENGTH_SHORT).show()
                );
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

    private void updateData() {
        db.collection("detai").document(deTaiId)
                .update("tenDeTai", edtTenDeTai.getText().toString(),
                        "moTa", edtMoTa.getText().toString())
                .addOnSuccessListener(a ->
                        Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show()
                );
    }
}

