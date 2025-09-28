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
        taiKhoanList = new ArrayList<>();
        adapter = new TaiKhoanAdapter(this, taiKhoanList);
        lvTaiKhoan.setAdapter(adapter);

        loadTaiKhoan();

        // Long click để Sửa / Xóa
        lvTaiKhoan.setOnItemLongClickListener((parent, view, position, id) -> {
            TaiKhoan selected = taiKhoanList.get(position);

            String[] options = {"Sửa", "Xóa"};
            new AlertDialog.Builder(AccountManagementActivity.this)
                    .setTitle(selected.getHoTen())
                    .setItems(options, (dialog, which) -> {
                        if(which == 0){
                            // Sửa: gán role
                            Intent intent = new Intent(AccountManagementActivity.this, AccountFormActivity.class);
                            intent.putExtra("taiKhoan", selected);
                            startActivity(intent);
                        } else if(which == 1){
                            // Xóa tài khoản Firestore
                            db.collection("users").document(selected.getId())
                                    .delete()
                                    .addOnSuccessListener(aVoid -> {
                                        taiKhoanList.remove(selected);
                                        adapter.notifyDataSetChanged();
                                        Toast.makeText(this, "Xóa tài khoản thành công", Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> Toast.makeText(this, "Lỗi xóa tài khoản", Toast.LENGTH_SHORT).show());
                        }
                    }).show();

            return true; // giữ sự kiện long click
        });
    }

    private void loadTaiKhoan(){
        db.collection("users")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    taiKhoanList.clear();
                    for(var doc : querySnapshot.getDocuments()){
                        TaiKhoan tk = doc.toObject(TaiKhoan.class);
                        if(tk != null){
                            tk.setId(doc.getId());
                            taiKhoanList.add(tk);
                        }
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Lỗi tải danh sách", Toast.LENGTH_SHORT).show());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTaiKhoan(); // reload khi quay lại để cập nhật role
    }
}