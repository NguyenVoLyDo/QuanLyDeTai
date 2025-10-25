package com.example.quanlydetai.activitys.studentactivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.quanlydetai.R;
import com.example.quanlydetai.activitys.ChangePasswordActivity;
import com.example.quanlydetai.activitys.LoginActivity;
import com.example.quanlydetai.activitys.ProfileActivity;
import com.example.quanlydetai.fragment.StudentFrag.BaoCaoFragment;
import com.example.quanlydetai.fragment.StudentFrag.KetQuaFragment;
import com.example.quanlydetai.fragment.StudentFrag.StudentDeTaiFragment;
import com.example.quanlydetai.fragment.StudentFrag.ThongBaoFragment;
import com.google.android.material.navigation.NavigationView;

public class StudentMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private String maSV; // nhận từ Login
    private String uid; // nhận từ Login

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);

        maSV = getIntent().getStringExtra("maSV");
        uid = getIntent().getStringExtra("id");

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // Drawer
        drawerLayout = findViewById(R.id.drawerLayout);
        NavigationView navigationViewLeft = findViewById(R.id.navigationViewLeft);
        NavigationView navigationViewRight = findViewById(R.id.navigationViewRight);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationViewLeft.setNavigationItemSelectedListener(this);
        navigationViewRight.setNavigationItemSelectedListener(this);

        ImageView imgAvatar = findViewById(R.id.imgAvatar);
        imgAvatar.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.END));

        replaceFragment(StudentDeTaiFragment.newInstance(maSV));
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_detai) {
            replaceFragment(StudentDeTaiFragment.newInstance(maSV));
        }else if (id == R.id.nav_thongbao) {
            replaceFragment(ThongBaoFragment.newInstance(maSV));
        } else if (id == R.id.nav_baocao) {
            replaceFragment(BaoCaoFragment.newInstance(maSV));
        } else if (id == R.id.nav_ketqua) {
            replaceFragment(KetQuaFragment.newInstance(maSV));
        } else if (id == R.id.nav_profile) {
            startActivity(new Intent(this, ProfileActivity.class)
                    .putExtra("maSV", maSV)
                    .putExtra("loaiTK", "Sinh viên"));
        } else if (id == R.id.nav_change_password) {
            startActivity(new Intent(this, ChangePasswordActivity.class)
                    .putExtra("userId", uid)
                    .putExtra("loaiTK", "Sinh viên"));
        } else if (id == R.id.nav_logout) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
