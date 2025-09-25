package com.example.quanlydetai.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlydetai.R;
import com.example.quanlydetai.activitys.adminactivity.AdminMainActivity;
import com.example.quanlydetai.activitys.giangvienactivity.GVMainActivity;
import com.example.quanlydetai.activitys.studentactivity.StudentMainActivity;
import com.example.quanlydetai.models.TaiKhoan;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class LoginActivity extends AppCompatActivity {

    private EditText edtUsername, edtPassword;
    private Button btnLogin, btnRegister;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        db = FirebaseFirestore.getInstance();

        btnLogin.setOnClickListener(v -> loginUser());
        btnRegister.setOnClickListener(v -> startActivity(new Intent(this, RegisterActivity.class)));
    }

    private void loginUser() {
        String input = edtUsername.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (input.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        // Thử tìm theo tên đăng nhập trước
        db.collection("users")
                .whereEqualTo("tenDangNhap", input)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                        if (!querySnapshot.isEmpty()) {
                        checkPasswordAndLogin(querySnapshot, password);
                    } else {
                        // Nếu không có, thử theo email
                        db.collection("users")
                                .whereEqualTo("email", input)
                                .get()
                                .addOnSuccessListener(emailSnapshot -> {
                                    if (!emailSnapshot.isEmpty()) {
                                        checkPasswordAndLogin(emailSnapshot, password);
                                    } else {
                                        Toast.makeText(this, "Tài khoản không tồn tại", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(e ->
                                        Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                                );
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }

    private void checkPasswordAndLogin(Iterable<QueryDocumentSnapshot> snapshots, String password) {
        TaiKhoan tk = snapshots.iterator().next().toObject(TaiKhoan.class);

        if (!tk.getPassword().equals(password)) {
            Toast.makeText(this, "Sai mật khẩu", Toast.LENGTH_SHORT).show();
            return;
        }

        switch (tk.getTenLoaiTK()) {
            case "Sinh viên":
                Intent intentSV = new Intent(this, StudentMainActivity.class);
                intentSV.putExtra("maSV", tk.getMaSV());
                intentSV.putExtra("hoTen", tk.getHoTen());
                startActivity(intentSV);
                break;
            case "Giảng viên":
                Intent intentGV = new Intent(this, GVMainActivity.class);
                intentGV.putExtra("maGV", tk.getMaGV());
                intentGV.putExtra("hoTen", tk.getHoTen());
                startActivity(intentGV);
                break;
            case "Admin":
                startActivity(new Intent(this, AdminMainActivity.class));
                break;
            default:
                Toast.makeText(this, "Tài khoản chưa được phân quyền", Toast.LENGTH_SHORT).show();
        }
        finish();
    }
}
