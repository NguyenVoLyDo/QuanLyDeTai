package com.example.quanlydetai.activitys.adminactivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlydetai.R;
import com.example.quanlydetai.adapters.TaiKhoanAdapter;
import com.example.quanlydetai.interfaces.OnAccountActionListener;
import com.example.quanlydetai.models.TaiKhoan;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class AccountManagementActivity extends AppCompatActivity implements OnAccountActionListener {

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
        adapter = new TaiKhoanAdapter(this, taiKhoanList, this);
        lvTaiKhoan.setAdapter(adapter);


        // mở form thêm tài khoản
        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(this, AccountFormActivity.class);
            startActivity(intent);
        });

        loadTaiKhoan();

    }

    @Override
    public void onEdit(TaiKhoan taiKhoan) {
        Intent intent = new Intent(this, AccountFormActivity.class);
        intent.putExtra("taiKhoan", taiKhoan);
        startActivity(intent);
    }

    public void onDelete(TaiKhoan taiKhoan) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận")
                .setMessage("Bạn có chắc muốn xóa tài khoản này?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    db.collection("users").document(taiKhoan.getId())
                            .delete()
                            .addOnSuccessListener(aVoid -> {
                                taiKhoanList.remove(taiKhoan);
                                adapter.notifyDataSetChanged();
                                Toast.makeText(this, "Đã xóa tài khoản", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e ->
                                    Toast.makeText(this, "Lỗi khi xóa", Toast.LENGTH_SHORT).show());
                })
                .setNegativeButton("Hủy", null)
                .show();
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
