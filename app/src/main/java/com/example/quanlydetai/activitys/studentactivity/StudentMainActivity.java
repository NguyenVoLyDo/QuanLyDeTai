package com.example.quanlydetai.activitys.studentactivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlydetai.R;
import com.example.quanlydetai.activitys.ChangePasswordActivity;
import com.example.quanlydetai.activitys.LoginActivity;
import com.example.quanlydetai.activitys.ProfileActivity;
import com.example.quanlydetai.adapters.DangKyDeTaiAdapter;
import com.example.quanlydetai.models.DeTai;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class StudentMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationViewLeft, navigationViewRight;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private DangKyDeTaiAdapter adapter;
    private List<DeTai> deTaiList = new ArrayList<>();
    private ImageView imgAvatar;
    private FirebaseFirestore db;

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

        // Drawer
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationViewLeft = findViewById(R.id.navigationViewLeft);
        navigationViewRight = findViewById(R.id.navigationViewRight);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationViewLeft.setNavigationItemSelectedListener(this);
        navigationViewRight.setNavigationItemSelectedListener(this);

        imgAvatar = findViewById(R.id.imgAvatar);
        imgAvatar.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.END));

        // RecyclerView
        recyclerView = findViewById(R.id.recyclerDeTai);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DangKyDeTaiAdapter(deTaiList);
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(this::loadDeTai);

        // Floating Action Button
        FloatingActionButton fab = findViewById(R.id.fabAddDeTai);
        fab.setOnClickListener(v ->
                startActivity(new Intent(this, DangKyDeTaiActivity.class).putExtra("maSV", maSV))
        );

        db = FirebaseFirestore.getInstance();
        loadDeTai();
    }

    private void loadDeTai() {
        db.collection("detai")
                .whereEqualTo("sinhVienId", maSV)
                .get()
                .addOnSuccessListener(query -> {
                    deTaiList.clear();
                    for (QueryDocumentSnapshot doc : query) {
                        DeTai dt = doc.toObject(DeTai.class);
                        deTaiList.add(dt);
                    }
                    adapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false); // tắt loading
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Lỗi tải đề tài: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(false); // tắt loading
                });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_thongbao) {
            startActivity(new Intent(this, ThongBaoActivity.class).putExtra("maSV", maSV));
        } else if (id == R.id.nav_baocao) {
            startActivity(new Intent(this, BaoCaoActivity.class).putExtra("maSV", maSV));
        } else if (id == R.id.nav_ketqua) {
            startActivity(new Intent(this, KetQuaActivity.class).putExtra("maSV", maSV));
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
