package com.example.quanlydetai.activitys.adminactivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.quanlydetai.R;
import com.example.quanlydetai.models.ThongBao;
import com.google.firebase.firestore.FirebaseFirestore;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class GuiThongBaoAdminActivity extends AppCompatActivity {

    private EditText edtTieuDe, edtNoiDung, edtMaSV;
    private RadioGroup radioGroup;
    private RadioButton radioTatCa, radioMotSV;
    private Button btnGuiThongBao;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gui_thong_bao_admin);

        db = FirebaseFirestore.getInstance();

        edtTieuDe = findViewById(R.id.edtTieuDe);
        edtNoiDung = findViewById(R.id.edtNoiDung);
        edtMaSV = findViewById(R.id.edtMaSV);
        radioGroup = findViewById(R.id.radioGroup);
        radioTatCa = findViewById(R.id.radioTatCa);
        radioMotSV = findViewById(R.id.radioSinhVienCuThe);
        btnGuiThongBao = findViewById(R.id.btnGuiThongBao);

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioSinhVienCuThe) {
                edtMaSV.setVisibility(View.VISIBLE);
            } else {
                edtMaSV.setVisibility(View.GONE);
            }
        });

        btnGuiThongBao.setOnClickListener(v -> guiThongBao());
    }

    private void guiThongBao() {
        String tieuDe = edtTieuDe.getText().toString().trim();
        String noiDung = edtNoiDung.getText().toString().trim();
        String maSV = edtMaSV.getText().toString().trim();
        String nguoiNhan;

        if (TextUtils.isEmpty(tieuDe) || TextUtils.isEmpty(noiDung)) {
            Toast.makeText(this, "Vui lòng nhập tiêu đề và nội dung", Toast.LENGTH_SHORT).show();
            return;
        }

        if (radioTatCa.isChecked()) {
            nguoiNhan = "Tất cả sinh viên";
        } else if (radioMotSV.isChecked()) {
            if (TextUtils.isEmpty(maSV)) {
                Toast.makeText(this, "Vui lòng nhập mã sinh viên", Toast.LENGTH_SHORT).show();
                return;
            }
            nguoiNhan = maSV;
        } else {
            Toast.makeText(this, "Vui lòng chọn người nhận", Toast.LENGTH_SHORT).show();
            return;
        }

        String id = UUID.randomUUID().toString();
        String ngayGui = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());
        String nguoiGui = "Admin";

        ThongBao tb = new ThongBao(id, tieuDe, noiDung, ngayGui, nguoiGui, nguoiNhan);

        db.collection("thongbao")
                .document(id)
                .set(tb)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Gửi thông báo thành công!", Toast.LENGTH_SHORT).show();
                    edtTieuDe.setText("");
                    edtNoiDung.setText("");
                    edtMaSV.setText("");
                    radioTatCa.setChecked(true);
                    edtMaSV.setVisibility(View.GONE);
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Lỗi khi gửi thông báo: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }
}
