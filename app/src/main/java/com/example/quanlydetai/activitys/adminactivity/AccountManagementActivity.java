package com.example.quanlydetai.activitys.adminactivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlydetai.R;
import com.example.quanlydetai.adapters.TaiKhoanAdapter;
import com.example.quanlydetai.models.TaiKhoan;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class AccountManagementActivity extends AppCompatActivity {

    private List<TaiKhoan> taiKhoanList;
    private TaiKhoanAdapter adapter;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_management);

        ListView lvTaiKhoan = findViewById(R.id.lvTaiKhoan);
        FloatingActionButton fabAdd = findViewById(R.id.fabAddAccount);

        taiKhoanList = new ArrayList<>();
        adapter = new TaiKhoanAdapter(this, taiKhoanList);
        lvTaiKhoan.setAdapter(adapter);

        // mở form thêm tài khoản
        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(this, AccountFormActivity.class);
            startActivity(intent);
        });

        loadTaiKhoan();

        // click item để sửa hoặc xóa
        lvTaiKhoan.setOnItemClickListener((parent, view, position, id) -> {
            TaiKhoan selected = taiKhoanList.get(position);
            showOptionDialog(selected);
        });
    }

    private void showOptionDialog(TaiKhoan selected) {
        String[] options = {"Sửa tài khoản", "Xóa tài khoản"};
        new AlertDialog.Builder(this)
                .setTitle(selected.getHoTen())
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        Intent intent = new Intent(this, AccountFormActivity.class);
                        intent.putExtra("taiKhoan", selected);
                        startActivity(intent);
                    } else {
                        deleteAccount(selected);
                    }
                })
                .show();
    }

    private void deleteAccount(TaiKhoan tk) {
        db.collection("users").document(tk.getId())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    taiKhoanList.remove(tk);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(this, "Đã xóa tài khoản", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Lỗi khi xóa", Toast.LENGTH_SHORT).show());
    }

    private void loadTaiKhoan() {
        db.collection("users")
                .get()
                .addOnSuccessListener(snapshot -> {
                    taiKhoanList.clear();
                    snapshot.forEach(doc -> {
                        TaiKhoan tk = doc.toObject(TaiKhoan.class);
                        if (tk != null) {
                            tk.setId(doc.getId());
                            taiKhoanList.add(tk);
                        }
                    });
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Lỗi tải danh sách", Toast.LENGTH_SHORT).show());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTaiKhoan();
    }
}
