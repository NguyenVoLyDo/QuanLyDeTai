package com.example.quanlydetai.activitys;

import android.content.Intent;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class RegisterActivity extends AppCompatActivity {

    private EditText edtHoTen, edtTenDangNhap, edtEmail, edtPassword, edtMaSV, edtMaGV;
    private Spinner spinnerLoaiTaiKhoan;

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
        spinnerLoaiTaiKhoan = findViewById(R.id.spinnerLoaiTaiKhoan);
        Button btnRegister = findViewById(R.id.btnRegister);

        db = FirebaseFirestore.getInstance();

        // Spinner loại tài khoản
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.account_type_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLoaiTaiKhoan.setAdapter(adapter);

        spinnerLoaiTaiKhoan.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                String type = spinnerLoaiTaiKhoan.getSelectedItem().toString();
                if (type.equals("Sinh viên")) {
                    edtMaSV.setVisibility(View.VISIBLE);
                    edtMaGV.setVisibility(View.GONE);
                } else {
                    edtMaSV.setVisibility(View.GONE);
                    edtMaGV.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });

        btnRegister.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String hoTen = edtHoTen.getText().toString().trim();
        String tenDangNhap = edtTenDangNhap.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String loaiTK = spinnerLoaiTaiKhoan.getSelectedItem().toString();
        String maSV = loaiTK.equals("Sinh viên") ? edtMaSV.getText().toString().trim() : "";
        String maGV = loaiTK.equals("Giảng viên") ? edtMaGV.getText().toString().trim() : "";

        if (hoTen.isEmpty() || tenDangNhap.isEmpty() || email.isEmpty() || password.isEmpty()
                || (loaiTK.equals("Sinh viên") && maSV.isEmpty())
                || (loaiTK.equals("Giảng viên") && maGV.isEmpty())) {
            Toast.makeText(this, "Nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra trùng lặp
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
                if (!maSV.isEmpty() && tk.getMaSV().equals(maSV)) {
                    Toast.makeText(this, "Mã sinh viên đã tồn tại", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!maGV.isEmpty() && tk.getMaGV().equals(maGV)) {
                    Toast.makeText(this, "Mã giảng viên đã tồn tại", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            // Không trùng → tạo tài khoản
            String uid = db.collection("users").document().getId();
            TaiKhoan newTk = new TaiKhoan(uid, tenDangNhap, email, password, hoTen, maSV, maGV, loaiTK);

            db.collection("users").document(uid)
                    .set(newTk)
                    .addOnSuccessListener(aVoid -> {
                        // Lưu chi tiết theo loại
                        if (loaiTK.equals("Sinh viên")) {
                            SinhVien sv = new SinhVien(maSV, hoTen, email, "Chưa cập nhật", "Chưa cập nhật");
                            db.collection("sinhvien").document(maSV).set(sv);
                        } else if (loaiTK.equals("Giảng viên")) {
                            GiangVien gv = new GiangVien(maGV, hoTen, email, "Chưa cập nhật", "Chưa cập nhật");
                            db.collection("giangvien").document(maGV).set(gv);
                        }

                        Toast.makeText(this, "Đăng ký thành công! Vui lòng đăng nhập.", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(this, LoginActivity.class));
                        finish();
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Lỗi lưu dữ liệu", Toast.LENGTH_SHORT).show());

        }).addOnFailureListener(e -> Toast.makeText(this, "Lỗi kiểm tra dữ liệu", Toast.LENGTH_SHORT).show());
    }
}
