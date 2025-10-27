package com.example.quanlydetai.activitys.adminactivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlydetai.R;
import com.example.quanlydetai.models.TaiKhoan;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AccountFormActivity extends AppCompatActivity {

    private Spinner spinnerLoaiTK;
    private FirebaseFirestore db;
    private TaiKhoan editTaiKhoan;

    private EditText edtTenDangNhap, edtEmail, edtHoTen, edtMatKhau, edtMaSV, edtMaGV;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_form);

        // --- Ánh xạ các view ---
        edtTenDangNhap = findViewById(R.id.edtTenDangNhap);
        edtEmail = findViewById(R.id.edtEmail);
        edtHoTen = findViewById(R.id.edtHoTen);
        edtMatKhau = findViewById(R.id.edtMatKhau);
        edtMaSV = findViewById(R.id.edtMaSV);
        edtMaGV = findViewById(R.id.edtMaGV);
        spinnerLoaiTK = findViewById(R.id.spinnerLoaiTK);
        Button btnSave = findViewById(R.id.btnSave);

        db = FirebaseFirestore.getInstance();

        // --- Thiết lập spinner loại tài khoản ---
        String[] roles = getResources().getStringArray(R.array.roles_array);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, roles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLoaiTK.setAdapter(adapter);

        // Ẩn/hiện mã SV hoặc GV theo loại tài khoản
        spinnerLoaiTK.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                String loaiTK = spinnerLoaiTK.getSelectedItem().toString();
                if (loaiTK.equals("Sinh viên")) {
                    edtMaSV.setVisibility(View.VISIBLE);
                    edtMaGV.setVisibility(View.GONE);
                } else if (loaiTK.equals("Giảng viên")) {
                    edtMaSV.setVisibility(View.GONE);
                    edtMaGV.setVisibility(View.VISIBLE);
                } else {
                    edtMaSV.setVisibility(View.GONE);
                    edtMaGV.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) { }
        });

        // --- Nhận dữ liệu tài khoản nếu đang sửa ---
        editTaiKhoan = (TaiKhoan) getIntent().getSerializableExtra("taiKhoan");

        if (editTaiKhoan != null) {
            // ===== CHẾ ĐỘ SỬA =====
            edtTenDangNhap.setText(editTaiKhoan.getTenDangNhap());
            edtEmail.setText(editTaiKhoan.getEmail());
            edtHoTen.setText(editTaiKhoan.getHoTen());
            edtMatKhau.setText(editTaiKhoan.getPassword());
            edtMaSV.setText(editTaiKhoan.getMaSV());
            edtMaGV.setText(editTaiKhoan.getMaGV());

            // Khóa tên đăng nhập & email
            edtTenDangNhap.setEnabled(false);
            edtEmail.setEnabled(false);

            // Chọn loại TK
            int pos = adapter.getPosition(editTaiKhoan.getTenLoaiTK());
            if (pos >= 0) spinnerLoaiTK.setSelection(pos);

            btnSave.setText("Cập nhật tài khoản");
            btnSave.setOnClickListener(v -> updateTaiKhoan());
        } else {
            // ===== CHẾ ĐỘ THÊM MỚI =====
            btnSave.setText("Thêm tài khoản");
            btnSave.setOnClickListener(v -> addTaiKhoan());
        }
    }

    private void addTaiKhoan() {
        String tenDangNhap = edtTenDangNhap.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String hoTen = edtHoTen.getText().toString().trim();
        String matKhau = edtMatKhau.getText().toString().trim();
        String loaiTK = spinnerLoaiTK.getSelectedItem().toString();
        String maSV = edtMaSV.getText().toString().trim();
        String maGV = edtMaGV.getText().toString().trim();

        if (tenDangNhap.isEmpty() || email.isEmpty() || hoTen.isEmpty() || matKhau.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (loaiTK.equals("Sinh viên") && maSV.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập mã sinh viên!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (loaiTK.equals("Giảng viên") && maGV.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập mã giảng viên!", Toast.LENGTH_SHORT).show();
            return;
        }

        String uid = db.collection("users").document().getId();
        TaiKhoan newTk = new TaiKhoan(uid, tenDangNhap, email, matKhau, hoTen, maSV, maGV, loaiTK);

        db.collection("users")
                .add(newTk)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Thêm tài khoản thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Lỗi khi thêm tài khoản: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void updateTaiKhoan() {
        if (editTaiKhoan == null || editTaiKhoan.getId() == null) {
            Toast.makeText(this, "Không tìm thấy tài khoản để cập nhật!", Toast.LENGTH_SHORT).show();
            return;
        }

        String matKhau = edtMatKhau.getText().toString().trim();
        String loaiTK = spinnerLoaiTK.getSelectedItem().toString();
        String maSV = edtMaSV.getText().toString().trim();
        String maGV = edtMaGV.getText().toString().trim();

        DocumentReference docRef = db.collection("users").document(editTaiKhoan.getId());
        Map<String, Object> updates = new HashMap<>();
        updates.put("password", matKhau);
        updates.put("tenLoaiTK", loaiTK);
        updates.put("maSV", maSV);
        updates.put("maGV", maGV);

        docRef.update(updates)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Cập nhật tài khoản thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Lỗi khi cập nhật: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
