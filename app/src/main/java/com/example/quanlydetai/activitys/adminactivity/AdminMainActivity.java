package com.example.quanlydetai.activitys.adminactivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlydetai.R;
import com.google.android.material.card.MaterialCardView;

public class AdminMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);


        // Existing buttons
        MaterialCardView btnManageAccounts = findViewById(R.id.btnManageAccounts);
        MaterialCardView btnManageSystemInfo = findViewById(R.id.btnManageSystemInfo);

        // ✅ New button for sending notifications
        MaterialCardView btnGuiThongBaoAdmin = findViewById(R.id.btnGuiThongBaoAdmin);

        // 👉 Open account management
        btnManageAccounts.setOnClickListener(view ->
                startActivity(new Intent(AdminMainActivity.this, AccountManagementActivity.class))
        );

        // 👉 Open system info management
        btnManageSystemInfo.setOnClickListener(view ->
                startActivity(new Intent(AdminMainActivity.this, SystemInfoActivity.class))
        );

        // ✅ Open send notification screen
        btnGuiThongBaoAdmin.setOnClickListener(view -> {
            Intent intent = new Intent(AdminMainActivity.this, GuiThongBaoAdminActivity.class);
            intent.putExtra("maAdmin", "ADMIN001"); // optional admin ID
            startActivity(intent);
        });

    }
}
