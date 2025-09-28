package com.example.quanlydetai.activitys.adminactivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlydetai.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class SystemInfoActivity extends AppCompatActivity {

    private TextView tvTotalStudents, tvTotalLecturers, tvTotalTopics;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_info);

        tvTotalStudents = findViewById(R.id.tvTotalStudents);
        tvTotalLecturers = findViewById(R.id.tvTotalLecturers);
        tvTotalTopics = findViewById(R.id.tvTotalTopics);
        Button btnRefresh = findViewById(R.id.btnRefresh);

        db = FirebaseFirestore.getInstance();

        // Tự động cập nhật khi Firestore thay đổi
        addRealtimeListeners();

        // Nút Refresh
        btnRefresh.setOnClickListener(v -> loadSystemInfo());
    }

    private void addRealtimeListeners() {
        // Sinh viên
        db.collection("users").whereEqualTo("tenLoaiTK", "Sinh viên")
                .addSnapshotListener((value, error) -> {
                    if (error != null) return;
                    if (value != null) tvTotalStudents.setText("Số sinh viên: " + value.size());
                });

        // Giảng viên
        db.collection("users").whereEqualTo("tenLoaiTK", "Giảng viên")
                .addSnapshotListener((value, error) -> {
                    if (error != null) return;
                    if (value != null) tvTotalLecturers.setText("Số giảng viên: " + value.size());
                });

        // Đề tài
        db.collection("detai")
                .addSnapshotListener((value, error) -> {
                    if (error != null) return;
                    if (value != null) tvTotalTopics.setText("Số đề tài: " + value.size());
                });
    }

    private void loadSystemInfo() {
        // Chỉ để làm refresh thủ công
        db.collection("users").whereEqualTo("tenLoaiTK", "Sinh viên")
                .get()
                .addOnSuccessListener(QuerySnapshot::size)
                .addOnFailureListener(e -> Toast.makeText(this, "Lỗi load sinh viên", Toast.LENGTH_SHORT).show());

        db.collection("users").whereEqualTo("tenLoaiTK", "Giảng viên")
                .get()
                .addOnSuccessListener(querySnapshot -> tvTotalLecturers.setText(
                        "Số giảng viên: " + querySnapshot.size()
                ))
                .addOnFailureListener(e -> Toast.makeText(this, "Lỗi load giảng viên", Toast.LENGTH_SHORT).show());

        db.collection("detai")
                .get()
                .addOnSuccessListener(querySnapshot -> tvTotalTopics.setText(
                        "Số đề tài: " + querySnapshot.size()
                ))
                .addOnFailureListener(e -> Toast.makeText(this, "Lỗi load đề tài", Toast.LENGTH_SHORT).show());
    }
}