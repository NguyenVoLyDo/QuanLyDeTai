package com.example.quanlydetai.activitys.adminactivity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlydetai.R;
import com.example.quanlydetai.activitys.LoginActivity;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

public class AdminMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        // Existing buttons
        MaterialCardView btnManageAccounts = findViewById(R.id.btnManageAccounts);
        MaterialCardView btnManageSystemInfo = findViewById(R.id.btnManageSystemInfo);
        MaterialCardView btnGuiThongBaoAdmin = findViewById(R.id.btnGuiThongBaoAdmin);

        //  New logout button
        MaterialButton btnLogoutAdmin = findViewById(R.id.btnLogoutAdmin);


        //  Open account management
        btnManageAccounts.setOnClickListener(view ->
                startActivity(new Intent(AdminMainActivity.this, AccountManagementActivity.class))
        );

        //  Open system info management
        btnManageSystemInfo.setOnClickListener(view ->
                startActivity(new Intent(AdminMainActivity.this, SystemInfoActivity.class))
        );

        //  Open send notification screen
        btnGuiThongBaoAdmin.setOnClickListener(view -> {
            Intent intent = new Intent(AdminMainActivity.this, GuiThongBaoAdminActivity.class);
            intent.putExtra("maAdmin", "ADMIN001");
            startActivity(intent);
        });

        //  Handle logout
        btnLogoutAdmin.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut(); // Logout from Firebase
            Intent intent = new Intent(AdminMainActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); // Close admin screen
        });
    }
}
