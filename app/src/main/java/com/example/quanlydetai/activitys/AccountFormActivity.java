package com.example.quanlydetai.activitys;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlydetai.R;
import com.example.quanlydetai.models.GiangVien;
import com.example.quanlydetai.models.SinhVien;
import com.example.quanlydetai.models.TaiKhoan;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AccountFormActivity extends AppCompatActivity {

    private TextInputEditText edtHoTen, edtTenDangNhap, edtEmail, edtPassword, edtMaSV, edtMaGV;
    private MaterialAutoCompleteTextView spinnerLoaiTK;
    private TextInputLayout tilMaSV, tilMaGV;
    private MaterialButton btnRegister;

    private FirebaseFirestore db;
    private TaiKhoan editTaiKhoan;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_form); // dùng lại layout đăng ký

        db = FirebaseFirestore.getInstance();

        // --- Ánh xạ view ---
        edtHoTen = findViewById(R.id.edtHoTen);
        edtTenDangNhap = findViewById(R.id.edtTenDangNhap);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtMaSV = findViewById(R.id.edtMaSV);
        edtMaGV = findViewById(R.id.edtMaGV);
        tilMaSV = findViewById(R.id.tilMaSV);
        tilMaGV = findViewById(R.id.tilMaGV);
        spinnerLoaiTK = findViewById(R.id.spinnerLoaiTaiKhoan);
        btnRegister = findViewById(R.id.btnRegister);

        // --- Cấu hình dropdown loại tài khoản ---
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.roles_array)
        );
        spinnerLoaiTK.setAdapter(adapter);

        // Hiển thị mã SV/GV theo loại tài khoản
        spinnerLoaiTK.setOnItemClickListener((parent, view, position, id) -> {
            String loai = spinnerLoaiTK.getText().toString();
            if (loai.equals("Sinh viên")) {
                tilMaSV.setVisibility(android.view.View.VISIBLE);
                tilMaGV.setVisibility(android.view.View.GONE);
            } else if (loai.equals("Giảng viên")) {
                tilMaGV.setVisibility(android.view.View.VISIBLE);
                tilMaSV.setVisibility(android.view.View.GONE);
            } else {
                tilMaGV.setVisibility(android.view.View.GONE);
                tilMaSV.setVisibility(android.view.View.GONE);
            }
        });

        // --- Kiểm tra xem có đang sửa không ---
        editTaiKhoan = (TaiKhoan) getIntent().getSerializableExtra("taiKhoan");

        if (editTaiKhoan != null) {
            // ==== CHẾ ĐỘ SỬA ====
            edtHoTen.setText(editTaiKhoan.getHoTen());
            edtTenDangNhap.setText(editTaiKhoan.getTenDangNhap());
            edtEmail.setText(editTaiKhoan.getEmail());
            edtPassword.setText(editTaiKhoan.getPassword());
            edtMaSV.setText(editTaiKhoan.getMaSV());
            edtMaGV.setText(editTaiKhoan.getMaGV());
            spinnerLoaiTK.setText(editTaiKhoan.getTenLoaiTK(), false);

            // Khóa tên đăng nhập + email
            edtTenDangNhap.setEnabled(false);
            edtEmail.setEnabled(false);

            btnRegister.setText("Cập nhật tài khoản");
            btnRegister.setOnClickListener(v -> updateTaiKhoan());
        } else {
            // ==== CHẾ ĐỘ THÊM MỚI ====
            btnRegister.setText("Thêm tài khoản");
            btnRegister.setOnClickListener(v -> addTaiKhoan());
        }
    }

    private void addTaiKhoan() {
        String tenDangNhap = edtTenDangNhap.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String hoTen = edtHoTen.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String loai = spinnerLoaiTK.getText().toString().trim();
        String maSV = edtMaSV.getText().toString().trim();
        String maGV = edtMaGV.getText().toString().trim();

        if (tenDangNhap.isEmpty() || email.isEmpty() || hoTen.isEmpty() || password.isEmpty() || loai.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (loai.equals("Sinh viên") && maSV.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập mã sinh viên!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (loai.equals("Giảng viên") && maGV.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập mã giảng viên!", Toast.LENGTH_SHORT).show();
            return;
        }

        String uid = db.collection("users").document().getId();
        TaiKhoan newTk = new TaiKhoan(uid, tenDangNhap, email, password, hoTen, maSV, maGV, loai);

        db.collection("users").document(uid)
                .set(newTk)
                .addOnSuccessListener(aVoid -> {
                    if (loai.equals("Sinh viên")) {
                        SinhVien sv = new SinhVien(maSV, hoTen, email, "Chưa cập nhật", "Chưa cập nhật");
                        db.collection("sinhvien").document(maSV).set(sv);
                    } else if (loai.equals("Giảng viên")) {
                        GiangVien gv = new GiangVien(maGV, hoTen, email, "Chưa cập nhật", "Chưa cập nhật");
                        db.collection("giangvien").document(maGV).set(gv);
                    }

                    Toast.makeText(this, "Thêm tài khoản thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Lỗi khi thêm: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void updateTaiKhoan() {
        if (editTaiKhoan == null || editTaiKhoan.getId() == null) {
            Toast.makeText(this, "Không tìm thấy tài khoản để cập nhật!", Toast.LENGTH_SHORT).show();
            return;
        }

        String password = edtPassword.getText().toString().trim();
        String loai = spinnerLoaiTK.getText().toString().trim();
        String maSV = edtMaSV.getText().toString().trim();
        String maGV = edtMaGV.getText().toString().trim();
        String hoTen = edtHoTen.getText().toString().trim();

        DocumentReference docRef = db.collection("users").document(editTaiKhoan.getId());
        Map<String, Object> updates = new HashMap<>();
        updates.put("password", password);
        updates.put("tenLoaiTK", loai);
        updates.put("maSV", maSV);
        updates.put("maGV", maGV);
        updates.put("hoTen", hoTen);

        docRef.update(updates)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Lỗi khi cập nhật: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
