package com.example.quanlydetai.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlydetai.R;
import com.example.quanlydetai.activitys.giangvienactivity.GVMainActivity;
import com.example.quanlydetai.activitys.studentactivity.StudentMainActivity;
import com.google.firebase.firestore.FirebaseFirestore;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText edtOldPass, edtNewPass, edtConfirmPass;
    private Button btnChangePassword;
    private FirebaseFirestore db;
    private String userId; // nhận từ intent
    private String loaiTK; // nhận từ intent

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        edtOldPass = findViewById(R.id.edtOldPass);
        edtNewPass = findViewById(R.id.edtNewPass);
        edtConfirmPass = findViewById(R.id.edtConfirmPass);
        btnChangePassword = findViewById(R.id.btnChangePassword);

        db = FirebaseFirestore.getInstance();
        userId = getIntent().getStringExtra("userId");
        loaiTK = getIntent().getStringExtra("loaiTK");

        btnChangePassword.setOnClickListener(v -> changePassword());
    }

    private void changePassword() {
        String oldPass = edtOldPass.getText().toString().trim();
        String newPass = edtNewPass.getText().toString().trim();
        String confirmPass = edtConfirmPass.getText().toString().trim();

        if (oldPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPass.equals(confirmPass)) {
            Toast.makeText(this, "Mật khẩu xác nhận không khớp", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra mật khẩu cũ
        db.collection("users").document(userId).get()
                .addOnSuccessListener(doc -> {
                    String currentPass = doc.getString("password");
                    if (currentPass != null && currentPass.equals(oldPass)) {
                        // Update mật khẩu
                        db.collection("users").document(userId)
                                .update("password", newPass)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                                    if ("Sinh viên".equals(loaiTK)) {
                                        startActivity(new Intent(this, StudentMainActivity.class)
                                                .putExtra("maSV", doc.getString("maSV"))
                                                .putExtra("id", userId));
                                    } else if ("Giảng viên".equals(loaiTK)) {
                                        startActivity(new Intent(this, GVMainActivity.class)
                                                .putExtra("maGV", doc.getString("maGV"))
                                                .putExtra("id", userId));
                                    }
                                    finish();
                                })
                                .addOnFailureListener(e ->
                                        Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                    } else {
                        Toast.makeText(this, "Mật khẩu cũ không đúng", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Lỗi kiểm tra mật khẩu: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }
}
