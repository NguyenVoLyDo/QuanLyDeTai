package com.example.quanlydetai.activitys.giangvienactivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.DividerItemDecoration;
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
import com.example.quanlydetai.adapters.DuyetDeTaiAdapter;
import com.example.quanlydetai.models.DeTai;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class GVMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private DuyetDeTaiAdapter adapter;
    private final List<DeTai> deTaiList = new ArrayList<>();
    private FirebaseFirestore db;

    private TextView txtEmpty;

    private String maGV;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gvmain);

        maGV = getIntent().getStringExtra("maGV");
        uid = getIntent().getStringExtra("id");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        recyclerView = findViewById(R.id.recyclerDeTaiGV);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DuyetDeTaiAdapter(this, deTaiList);
        recyclerView.setAdapter(adapter);

        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        divider.setDrawable(getDrawable(R.drawable.divider));
        recyclerView.addItemDecoration(divider);

        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(this::loadDeTaiChuaDuyet);
        txtEmpty = findViewById(R.id.txtEmpty);

        db = FirebaseFirestore.getInstance();
        loadDeTaiChuaDuyet();
    }

    private void loadDeTaiChuaDuyet() {
        db.collection("detai")
                .whereEqualTo("trangThai", "pending")
                .get()
                .addOnSuccessListener(query -> {
                    deTaiList.clear();
                    for (QueryDocumentSnapshot doc : query) {
                        DeTai dt = doc.toObject(DeTai.class);
                        dt.setDeTaiId(doc.getId());
                        deTaiList.add(dt);
                    }
                    adapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false); // tắt loading
                    if (deTaiList.isEmpty()) {
                        recyclerView.setVisibility(View.GONE);
                        txtEmpty.setVisibility(View.VISIBLE);
                    } else {
                        recyclerView.setVisibility(View.VISIBLE);
                        txtEmpty.setVisibility(View.GONE);
                    }
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
            startActivity(new Intent(this, GuiThongBaoActivity.class).putExtra("maGV", maGV));
        } else if (id == R.id.nav_cham_baocao) {
            startActivity(new Intent(this, ChamBaoCaoActivity.class).putExtra("maGV", maGV));
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
