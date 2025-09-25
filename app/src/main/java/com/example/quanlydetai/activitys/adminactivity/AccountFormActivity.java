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
import com.google.firebase.firestore.FirebaseFirestore;

public class AccountFormActivity extends AppCompatActivity {

    private EditText edtTenDangNhap, edtEmail, edtHoTen;
    private Spinner spinnerLoaiTK;
    private Button btnSave;

    private FirebaseFirestore db;
    private TaiKhoan editTaiKhoan;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_form);

        edtTenDangNhap = findViewById(R.id.edtTenDangNhap);
        edtEmail = findViewById(R.id.edtEmail);
        edtHoTen = findViewById(R.id.edtHoTen);
        spinnerLoaiTK = findViewById(R.id.spinnerLoaiTK);
        btnSave = findViewById(R.id.btnSave);

        db = FirebaseFirestore.getInstance();

        // Lấy danh sách role từ resource
        String[] roles = getResources().getStringArray(R.array.roles_array);

        // Lấy tài khoản cần sửa từ Intent
        editTaiKhoan = (TaiKhoan) getIntent().getSerializableExtra("taiKhoan");

        if (editTaiKhoan != null) {
            edtTenDangNhap.setText(editTaiKhoan.getTenDangNhap());
            edtEmail.setText(editTaiKhoan.getEmail());
            edtHoTen.setText(editTaiKhoan.getHoTen());

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            if (editTaiKhoan.getTenLoaiTK().equalsIgnoreCase("Admin")) {
                // Không cho sửa nếu tài khoản là Admin khác
                spinnerLoaiTK.setEnabled(false);
                btnSave.setEnabled(false);
                Toast.makeText(this, "Không được sửa role của Admin khác", Toast.LENGTH_SHORT).show();
            } else {
                // Chỉ thêm role không phải Admin
                for (String role : roles) {
                    if (!role.equalsIgnoreCase("Admin")) {
                        adapter.add(role);
                    }
                }
                spinnerLoaiTK.setAdapter(adapter);

                int pos = adapter.getPosition(editTaiKhoan.getTenLoaiTK());
                if (pos >= 0) spinnerLoaiTK.setSelection(pos);
            }
        }

        btnSave.setOnClickListener(v -> saveTaiKhoan());
    }

    private void saveTaiKhoan() {
        if (editTaiKhoan == null) return;

        // Phòng ngừa: nếu tài khoản là Admin khác → không lưu
        if (editTaiKhoan.getTenLoaiTK().equalsIgnoreCase("Admin")) {
            Toast.makeText(this, "Không được sửa role của Admin khác", Toast.LENGTH_SHORT).show();
            return;
        }

        String tenLoaiTK = spinnerLoaiTK.getSelectedItem().toString();

        // Phòng ngừa: không được chọn Admin
        if (tenLoaiTK.equalsIgnoreCase("Admin")) {
            Toast.makeText(this, "Không được cấp role Admin", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("users").document(editTaiKhoan.getId())
                .update("tenLoaiTK", tenLoaiTK)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Cập nhật role thành công", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Lỗi cập nhật role", Toast.LENGTH_SHORT).show());
    }
}
