package com.example.quanlydetai.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlydetai.R;
import com.example.quanlydetai.models.GiangVien;
import com.example.quanlydetai.models.SinhVien;
import com.example.quanlydetai.models.TaiKhoan;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText edtHoTen, edtTenDangNhap, edtEmail, edtPassword, edtMaSV, edtMaGV;
    private MaterialAutoCompleteTextView spinnerLoaiTaiKhoan;
    private TextInputLayout tilMaSV, tilMaGV;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edtHoTen = findViewById(R.id.edtHoTen);
        edtTenDangNhap = findViewById(R.id.edtTenDangNhap);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtMaSV = findViewById(R.id.edtMaSV);
        edtMaGV = findViewById(R.id.edtMaGV);

        tilMaSV = findViewById(R.id.tilMaSV);
        tilMaGV = findViewById(R.id.tilMaGV);

        spinnerLoaiTaiKhoan = findViewById(R.id.spinnerLoaiTaiKhoan);
        MaterialButton btnRegister = findViewById(R.id.btnRegister);

        db = FirebaseFirestore.getInstance();

        // Dropdown Adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.account_type_array)
        );
        spinnerLoaiTaiKhoan.setAdapter(adapter);

        // Sự kiện chọn loại tài khoản
        spinnerLoaiTaiKhoan.setOnItemClickListener((parent, view, position, id) -> {
            String type = spinnerLoaiTaiKhoan.getText().toString();
            if (type.equals("Sinh viên")) {
                tilMaSV.setVisibility(View.VISIBLE);
                tilMaGV.setVisibility(View.GONE);
            } else {
                tilMaSV.setVisibility(View.GONE);
                tilMaGV.setVisibility(View.VISIBLE);
            }
        });

        btnRegister.setOnClickListener(v -> registerUser());

        TextView tvLoginNow = findViewById(R.id.tvLoginNow);
        tvLoginNow.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });

    }

    private void registerUser() {
        String hoTen = edtHoTen.getText().toString().trim();
        String tenDangNhap = edtTenDangNhap.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String loaiTK = spinnerLoaiTaiKhoan.getText().toString().trim();

        String maSV = loaiTK.equals("Sinh viên") ? edtMaSV.getText().toString().trim() : "";
        String maGV = loaiTK.equals("Giảng viên") ? edtMaGV.getText().toString().trim() : "";

        if (hoTen.isEmpty() || tenDangNhap.isEmpty() || email.isEmpty() || password.isEmpty() ||
                loaiTK.isEmpty() ||
                (loaiTK.equals("Sinh viên") && maSV.isEmpty()) ||
                (loaiTK.equals("Giảng viên") && maGV.isEmpty())) {

            Toast.makeText(this, "Nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra trùng dữ liệu
        db.collection("users").get().addOnSuccessListener(querySnapshot -> {
            for (QueryDocumentSnapshot doc : querySnapshot) {
                TaiKhoan tk = doc.toObject(TaiKhoan.class);
                if (tk.getTenDangNhap().equals(tenDangNhap)) {
                    Toast.makeText(this, "Tên đăng nhập đã tồn tại", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (tk.getEmail().equals(email)) {
                    Toast.makeText(this, "Email đã tồn tại", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!maSV.isEmpty() && maSV.equals(tk.getMaSV())) {
                    Toast.makeText(this, "Mã sinh viên đã tồn tại", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!maGV.isEmpty() && maGV.equals(tk.getMaGV())) {
                    Toast.makeText(this, "Mã giảng viên đã tồn tại", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            // Lưu dữ liệu
            String uid = db.collection("users").document().getId();
            TaiKhoan newTk = new TaiKhoan(uid, tenDangNhap, email, password, hoTen, maSV, maGV, loaiTK);

            db.collection("users").document(uid)
                    .set(newTk)
                    .addOnSuccessListener(aVoid -> {
                        if (loaiTK.equals("Sinh viên")) {
                            SinhVien sv = new SinhVien(maSV, hoTen, email, "Chưa cập nhật", "Chưa cập nhật");
                            db.collection("sinhvien").document(maSV).set(sv);
                        } else {
                            GiangVien gv = new GiangVien(maGV, hoTen, email, "Chưa cập nhật", "Chưa cập nhật");
                            db.collection("giangvien").document(maGV).set(gv);
                        }

                        Toast.makeText(this, "Đăng ký thành công! Vui lòng đăng nhập.", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(this, LoginActivity.class));
                        finish();
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Lỗi lưu tài khoản", Toast.LENGTH_SHORT).show());

        }).addOnFailureListener(e -> Toast.makeText(this, "Lỗi kiểm tra dữ liệu", Toast.LENGTH_SHORT).show());
    }
}
