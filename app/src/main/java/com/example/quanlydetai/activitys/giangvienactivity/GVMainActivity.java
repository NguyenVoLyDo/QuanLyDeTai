package com.example.quanlydetai.activitys.giangvienactivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.quanlydetai.R;
import com.example.quanlydetai.activitys.ChangePasswordActivity;
import com.example.quanlydetai.activitys.LoginActivity;
import com.example.quanlydetai.activitys.ProfileActivity;
import com.example.quanlydetai.fragment.GiangvienFrag.ChamBaoCaoFragment;
import com.example.quanlydetai.fragment.GiangvienFrag.GVDuyetDeTaiFragment;
import com.example.quanlydetai.fragment.GiangvienFrag.GoiYDeTaiFragment;
import com.google.android.material.navigation.NavigationView;

public class GVMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private String maGV;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gvmain);

        maGV = getIntent().getStringExtra("maGV");
        uid = getIntent().getStringExtra("id");

        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Drawer
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationViewRight = findViewById(R.id.nav_view_gv_right);
        NavigationView navigationViewLeft = findViewById(R.id.nav_view_gv_left);

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
        replaceFragment(GVDuyetDeTaiFragment.newInstance(maGV));

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

        if (id == R.id.nav_duyetdetai) {
            replaceFragment(GVDuyetDeTaiFragment.newInstance(maGV));
        } else if (id == R.id.nav_cham_baocao) {
            replaceFragment(ChamBaoCaoFragment.newInstance(maGV));
        } else if (id == R.id.nav_thongbao) {
            startActivity(new Intent(this, GuiThongBaoActivity.class).putExtra("maGV", maGV));
        } else if (id == R.id.nav_them_goiy) {
            replaceFragment(GoiYDeTaiFragment.newInstance(maGV));
        } else if (id == R.id.nav_profile) {
            startActivity(new Intent(this, ProfileActivity.class)
                    .putExtra("maGV", maGV)
                    .putExtra("loaiTK", "Giảng viên"));
        } else if (id == R.id.nav_change_password) {
            startActivity(new Intent(this, ChangePasswordActivity.class)
                    .putExtra("userId", uid)
                    .putExtra("loaiTK", "Giảng viên"));
        } else if (id == R.id.nav_logout) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
