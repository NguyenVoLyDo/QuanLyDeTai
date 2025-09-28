package com.example.quanlydetai.activitys.adminactivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import com.example.quanlydetai.R;

public class AdminMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        Button btnManageAccounts = findViewById(R.id.btnManageAccounts);
        Button btnManageSystemInfo = findViewById(R.id.btnManageSystemInfo);

        // Mở Activity quản lý tài khoản
        btnManageAccounts.setOnClickListener(view -> startActivity(new Intent(AdminMainActivity.this, AccountManagementActivity.class)));

        // Mở Activity quản lý thông tin hệ thống
        btnManageSystemInfo.setOnClickListener(view -> startActivity(new Intent(AdminMainActivity.this, SystemInfoActivity.class)));
    }
}