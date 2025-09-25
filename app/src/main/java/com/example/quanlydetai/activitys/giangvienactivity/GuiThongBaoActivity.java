package com.example.quanlydetai.activitys.giangvienactivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlydetai.R;
import com.example.quanlydetai.models.ThongBao;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.UUID;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class GuiThongBaoActivity extends AppCompatActivity {

    private EditText edtTieuDe, edtNoiDung,edtMaSV;
    private RadioGroup radioGroup;
    private RadioButton radioTatCa, radioTheoLop;
    private Button btnGuiThongBao;

    private FirebaseFirestore db;

    private String gvId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gui_thong_bao);
        gvId = getIntent().getStringExtra("maGV");


        edtTieuDe = findViewById(R.id.edtTieuDe);
        edtNoiDung = findViewById(R.id.edtNoiDung);
        radioGroup = findViewById(R.id.radioGroup);
        edtMaSV = findViewById(R.id.edtMaSV);
        radioTatCa = findViewById(R.id.radioTatCa);
        btnGuiThongBao = findViewById(R.id.btnGuiThongBao);

        db = FirebaseFirestore.getInstance();

        btnGuiThongBao.setOnClickListener(v -> guiThongBao());
    }

    private void guiThongBao() {
        String tieuDe = edtTieuDe.getText().toString().trim();
        String noiDung = edtNoiDung.getText().toString().trim();
        String maSV = edtMaSV.getText().toString().trim();

        if(tieuDe.isEmpty() || noiDung.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        String nguoiNhan;
        if (radioTatCa.isChecked()) {
             nguoiNhan = "all";  // gửi cho tất cả
        } else if (!maSV.isEmpty()) {
             nguoiNhan = maSV ; // gửi riêng cho mã SV
        } else {
            Toast.makeText(this, "Chọn đối tượng nhận hoặc nhập mã SV", Toast.LENGTH_SHORT).show();
            return;
        }
        String ngayGui = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String id = UUID.randomUUID().toString();

        ThongBao tb = new ThongBao(id, tieuDe, noiDung, ngayGui, gvId, nguoiNhan);

        db.collection("thongbao")
                .document(id)
                .set(tb)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Đã gửi thông báo", Toast.LENGTH_SHORT).show();
                    edtTieuDe.setText("");
                    edtNoiDung.setText("");
                    radioTatCa.setChecked(true);
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }

}