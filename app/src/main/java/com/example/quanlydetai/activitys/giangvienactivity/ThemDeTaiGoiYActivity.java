package com.example.quanlydetai.activitys.giangvienactivity;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.quanlydetai.R;
import com.example.quanlydetai.models.DeTaiGoiY;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.FirebaseFirestore;

public class ThemDeTaiGoiYActivity extends AppCompatActivity {

    private TextInputEditText edtTenDeTai, edtMoTa;
    private FirebaseFirestore db;
    private String maGV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_de_tai_goi_y);

        maGV = getIntent().getStringExtra("maGV");

        edtTenDeTai = findViewById(R.id.edtTenDeTai);
        edtMoTa = findViewById(R.id.edtMoTa);
        MaterialButton btnAdd = findViewById(R.id.btnThem);

        btnAdd.setText("Thêm đề tài gợi ý");

        db = FirebaseFirestore.getInstance();

        btnAdd.setOnClickListener(v -> addDeTaiGoiY());
    }

    private void addDeTaiGoiY() {
        String ten = edtTenDeTai.getText().toString().trim();
        String moTa = edtMoTa.getText().toString().trim();

        if (ten.isEmpty() || moTa.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        String id = db.collection("detai_goiy").document().getId();
        DeTaiGoiY deTai = new DeTaiGoiY(id, ten, moTa, maGV);

        db.collection("detai_goiy").document(id)
                .set(deTai)
                .addOnSuccessListener(a -> {
                    Toast.makeText(this, "Thêm thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }
}
