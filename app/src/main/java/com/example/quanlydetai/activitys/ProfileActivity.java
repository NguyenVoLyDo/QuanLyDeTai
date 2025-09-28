package com.example.quanlydetai.activitys;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlydetai.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class ProfileActivity extends AppCompatActivity {

    private TextView txtHoTen, txtMa, txtEmail;

    private FirebaseFirestore db;
    private String loaiTK;  // "Sinh viên" hoặc "Giảng viên"
    private String maSV, maGV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ImageView imgAvatar = findViewById(R.id.imgAvatar);
        txtHoTen = findViewById(R.id.txtHoTen);
        txtMa = findViewById(R.id.txtMa);     // hiển thị: Mã SV hoặc Mã GV
        txtEmail = findViewById(R.id.txtEmail);

        db = FirebaseFirestore.getInstance();

        // Nhận dữ liệu từ intent
        loaiTK = getIntent().getStringExtra("loaiTK");
        maSV = getIntent().getStringExtra("maSV");
        maGV = getIntent().getStringExtra("maGV");

        loadProfile();
    }

    private void loadProfile() {
        if (loaiTK == null) {
            Toast.makeText(this, "Thiếu loại tài khoản!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (loaiTK.equals("Sinh viên") && maSV != null) {
            db.collection("users")
                    .whereEqualTo("maSV", maSV)
                    .get()
                    .addOnSuccessListener(query -> {
                        for (QueryDocumentSnapshot doc : query) {
                            txtHoTen.setText(doc.getString("hoTen"));
                            txtMa.setText("Mã SV: " + doc.getString("maSV"));
                            txtEmail.setText(doc.getString("email"));
                        }
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                    );

        } else if (loaiTK.equals("Giảng viên") && maGV != null) {
            db.collection("users")
                    .whereEqualTo("maGV", maGV)
                    .get()
                    .addOnSuccessListener(query -> {
                        for (QueryDocumentSnapshot doc : query) {
                            txtHoTen.setText(doc.getString("hoTen"));
                            txtMa.setText("Mã GV: " + doc.getString("maGV"));
                            txtEmail.setText(doc.getString("email"));
                        }
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                    );
        }
    }
}
