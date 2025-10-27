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
import com.example.quanlydetai.models.GiangVien;
import com.example.quanlydetai.models.SinhVien;
import com.example.quanlydetai.models.TaiKhoan;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AccountFormActivity extends AppCompatActivity {

    private MaterialAutoCompleteTextView spinnerLoaiTK;
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
        edtMatKhau = findViewById(R.id.edtPassword);
        TextInputLayout tilMaSV = findViewById(R.id.tilMaSV);
        TextInputLayout tilMaGV = findViewById(R.id.tilMaGV);
        edtMaSV = findViewById(R.id.edtMaSV);
        edtMaGV = findViewById(R.id.edtMaGV);
        spinnerLoaiTK = findViewById(R.id.spinnerLoaiTaiKhoan);
        Button btnSave = findViewById(R.id.btnSave);

        db = FirebaseFirestore.getInstance();

        // --- Thiết lập spinner loại tài khoản ---
        String[] roles = getResources().getStringArray(R.array.roles_array);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                roles
        );
        spinnerLoaiTK.setAdapter(adapter);

        // Ẩn/hiện mã SV hoặc GV theo loại tài khoản
        spinnerLoaiTK.setOnItemClickListener((parent, view, position, id) -> {
            String loaiTK = spinnerLoaiTK.getText().toString();
            if (loaiTK.equals("Sinh viên")) {
                tilMaSV.setVisibility(View.VISIBLE);
                tilMaGV.setVisibility(View.GONE);
            } else if (loaiTK.equals("Giảng viên")) {
                tilMaSV.setVisibility(View.GONE);
                tilMaGV.setVisibility(View.VISIBLE);
            } else {
                tilMaSV.setVisibility(View.GONE);
                tilMaGV.setVisibility(View.GONE);
            }
        });

        // --- Nhận dữ liệu tài khoản nếu đang sửa ---
        editTaiKhoan = (TaiKhoan) getIntent().getSerializableExtra("taiKhoan");

        if (editTaiKhoan != null) {
            // ===== CHẾ ĐỘ SỬA =====
            edtTenDangNhap.setText(editTaiKhoan.getTenDangNhap());
            edtEmail.setText(editTaiKhoan.getEmail());
            edtHoTen.setText(editTaiKhoan.getHoTen());
            edtMatKhau.setText(editTaiKhoan.getPassword());
            spinnerLoaiTK = findViewById(R.id.spinnerLoaiTaiKhoan);
            edtMaSV.setText(editTaiKhoan.getMaSV());
            edtMaGV.setText(editTaiKhoan.getMaGV());

            // Khóa tên đăng nhập & email
            edtMaSV.setEnabled(false);
            edtMaGV.setEnabled(false);
            spinnerLoaiTK.setEnabled(false);

            spinnerLoaiTK.setHint("Chọn loại tài khoản");
            // Chọn loại TK
            int pos = adapter.getPosition(editTaiKhoan.getTenLoaiTK());
            if (pos >= 0) spinnerLoaiTK.setText(adapter.getItem(pos), false);
            if (editTaiKhoan.getTenLoaiTK().equals("Sinh viên")) {
                tilMaSV.setVisibility(View.VISIBLE);
                tilMaGV.setVisibility(View.GONE);
            } else if (editTaiKhoan.getTenLoaiTK().equals("Giảng viên")) {
                tilMaSV.setVisibility(View.GONE);
                tilMaGV.setVisibility(View.VISIBLE);
            }

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
        String loaiTK = spinnerLoaiTK.getText().toString();
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
                .document(uid)
                .set(newTk)
                .addOnSuccessListener(documentReference -> {
                    if (loaiTK.equals("Sinh viên")) {
                        SinhVien sv = new SinhVien(maSV, hoTen, email, "Chưa cập nhật", "Chưa cập nhật");
                        db.collection("sinhvien").document(maSV).set(sv);
                    } else {
                        GiangVien gv = new GiangVien(maGV, hoTen, email, "Chưa cập nhật", "Chưa cập nhật");
                        db.collection("giangvien").document(maGV).set(gv);
                    }

                    Toast.makeText(this, "Thêm tài khoản thành công", Toast.LENGTH_LONG).show();
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

        String hoten = edtHoTen.getText().toString();
        String tenDangNhap = edtTenDangNhap.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String matKhau = edtMatKhau.getText().toString().trim();

        DocumentReference docRef = db.collection("users").document(editTaiKhoan.getId());
        Map<String, Object> updates = new HashMap<>();
        updates.put("hoTen", hoten);
        updates.put("tenDangNhap", tenDangNhap);
        updates.put("email", email);
        updates.put("password", matKhau);

        docRef.update(updates)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Cập nhật tài khoản thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Lỗi khi cập nhật: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
