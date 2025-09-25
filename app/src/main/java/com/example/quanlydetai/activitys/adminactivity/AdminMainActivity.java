package com.example.quanlydetai.activitys.adminactivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import com.example.quanlydetai.R;

public class AdminMainActivity extends AppCompatActivity {

    private Button btnManageAccounts, btnManageSystemInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        btnManageAccounts = findViewById(R.id.btnManageAccounts);
        btnManageSystemInfo = findViewById(R.id.btnManageSystemInfo);

        // Mở Activity quản lý tài khoản
        btnManageAccounts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminMainActivity.this, AccountManagementActivity.class));
            }
        });

        // Mở Activity quản lý thông tin hệ thống
        btnManageSystemInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminMainActivity.this, SystemInfoActivity.class));
            }
        });
    }
}