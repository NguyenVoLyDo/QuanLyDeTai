package com.example.quanlydetai.activitys.studentactivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlydetai.R;
import com.example.quanlydetai.adapters.BaoCaoAdapter;
import com.example.quanlydetai.models.BaoCao;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BaoCaoActivity extends AppCompatActivity {

    private EditText edtTenBaoCao, edtLinkFile;
    private Button btnNopBaoCao;
    private RecyclerView recyclerView;

    private BaoCaoAdapter adapter;
    private List<BaoCao> baoCaoList = new ArrayList<>();
    private FirebaseFirestore db;

    private String sinhVienId;
    private String deTaiId; // chỉ lưu id đề tài hợp lệ (đã duyệt) của SV

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bao_cao);

        sinhVienId = getIntent().getStringExtra("maSV");

        edtTenBaoCao = findViewById(R.id.edtTenBaoCao);
        edtLinkFile = findViewById(R.id.edtLinkFile);
        btnNopBaoCao = findViewById(R.id.btnNopBaoCao);
        recyclerView = findViewById(R.id.recyclerBaoCao);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BaoCaoAdapter(baoCaoList);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        // Lấy đề tài đã duyệt của sinh viên
        fetchApprovedDeTai();

        btnNopBaoCao.setOnClickListener(v -> nopBaoCao());

        loadBaoCao();
    }

    private void fetchApprovedDeTai() {
        db.collection("detai")
                .whereEqualTo("sinhVienId", sinhVienId)
                .whereEqualTo("trangThai", "approved") // chỉ lấy đề tài đã duyệt
                .get()
                .addOnSuccessListener(query -> {
                    if (!query.isEmpty()) {
                        // nếu 1 SV chỉ được duyệt 1 đề tài → lấy document đầu tiên
                        deTaiId = query.getDocuments().get(0).getId();
                    } else {
                        deTaiId = null;
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Lỗi lấy đề tài: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }

    private void nopBaoCao() {
        if (deTaiId == null) {
            Toast.makeText(this, "Bạn chưa có đề tài được duyệt, không thể nộp báo cáo!", Toast.LENGTH_LONG).show();
            return;
        }

        saveBaoCao();
    }

    private void saveBaoCao() {
        String tenBaoCao = edtTenBaoCao.getText().toString().trim();
        String linkFile = edtLinkFile.getText().toString().trim();

        if (tenBaoCao.isEmpty() || linkFile.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        String ngayNop = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        String baoCaoId = db.collection("baocao").document().getId();
        BaoCao baoCao = new BaoCao();
        baoCao.setBaoCaoId(baoCaoId);
        baoCao.setTenBaoCao(tenBaoCao);
        baoCao.setLinkFile(linkFile);
        baoCao.setSinhVienId(sinhVienId);
        baoCao.setDeTaiId(deTaiId);
        baoCao.setNgayNop(ngayNop);
        baoCao.setTrangThai("Chưa chấm");

        db.collection("baocao").document(baoCaoId).set(baoCao)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Nộp báo cáo thành công!", Toast.LENGTH_SHORT).show();
                    loadBaoCao();
                    edtTenBaoCao.setText("");
                    edtLinkFile.setText("");
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }

    private void loadBaoCao() {
        db.collection("baocao")
                .whereEqualTo("sinhVienId", sinhVienId)
                .get()
                .addOnSuccessListener(query -> {
                    baoCaoList.clear();
                    for (QueryDocumentSnapshot doc : query) {
                        BaoCao bc = doc.toObject(BaoCao.class);
                        bc.setBaoCaoId(doc.getId());
                        baoCaoList.add(bc);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Lỗi tải báo cáo: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }
}
