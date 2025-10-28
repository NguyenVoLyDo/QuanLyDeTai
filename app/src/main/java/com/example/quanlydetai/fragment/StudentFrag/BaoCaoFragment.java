package com.example.quanlydetai.fragment.StudentFrag;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.quanlydetai.R;
import com.example.quanlydetai.adapters.BaoCaoAdapter;
import com.example.quanlydetai.models.BaoCao;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BaoCaoFragment extends Fragment {

    private EditText edtTenBaoCao, edtLinkFile;
    private SwipeRefreshLayout swipeRefresh;
    private MaterialAutoCompleteTextView spinnerDeTai;
    private List<String> deTaiList = new ArrayList<>();
    private List<String> deTaiIdList = new ArrayList<>();
    private BaoCaoAdapter adapter;
    private final List<BaoCao> baoCaoList = new ArrayList<>();
    private FirebaseFirestore db;

    private String sinhVienId;
    private String deTaiId;

    public static BaoCaoFragment newInstance(String maSV) {
        BaoCaoFragment fragment = new BaoCaoFragment();
        Bundle args = new Bundle();
        args.putString("maSV", maSV);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        requireActivity().setTitle("Báo cáo");

        View view = inflater.inflate(R.layout.fragment_bao_cao, container, false);

        if (getArguments() != null) {
            sinhVienId = getArguments().getString("maSV");
        }

        edtTenBaoCao = view.findViewById(R.id.edtTenBaoCao);
        edtLinkFile = view.findViewById(R.id.edtLinkFile);
        Button btnNopBaoCao = view.findViewById(R.id.btnNopBaoCao);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerBaoCao);
        swipeRefresh = view.findViewById(R.id.swipeRefresh);
        spinnerDeTai = view.findViewById(R.id.spinnerDeTai);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new BaoCaoAdapter(baoCaoList);
        recyclerView.setAdapter(adapter);

        swipeRefresh.setOnRefreshListener(this::loadBaoCao);

        db = FirebaseFirestore.getInstance();
        fetchApprovedDeTai();

        btnNopBaoCao.setOnClickListener(v -> nopBaoCao());

        loadBaoCao();
        return view;
    }

    private void fetchApprovedDeTai() {
        db.collection("detai")
                .whereEqualTo("sinhVienId", sinhVienId)
                .whereEqualTo("trangThai", "approved")
                .get()
                .addOnSuccessListener(detaiQuery -> {

                    // Lấy danh sách deTaiId đã nộp báo cáo
                    db.collection("baocao")
                            .whereEqualTo("sinhVienId", sinhVienId)
                            .get()
                            .addOnSuccessListener(baoCaoQuery -> {
                                List<String> daNopDeTaiId = new ArrayList<>();
                                for (QueryDocumentSnapshot bcDoc : baoCaoQuery) {
                                    daNopDeTaiId.add(bcDoc.getString("deTaiId"));
                                }

                                deTaiList.clear();
                                deTaiIdList.clear();
                                for (QueryDocumentSnapshot doc : detaiQuery) {
                                    String id = doc.getId();
                                    if (!daNopDeTaiId.contains(id)) { // chỉ thêm các đề tài chưa nộp
                                        deTaiList.add(doc.getString("tenDeTai"));
                                        deTaiIdList.add(id);
                                    }
                                }

                                if (!deTaiList.isEmpty()) {
                                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                                            R.layout.list_item_dropdown, deTaiList);
                                    spinnerDeTai.setAdapter(adapter);

                                    // Chọn mặc định đề tài đầu tiên
                                    spinnerDeTai.setText(deTaiList.get(0), false);
                                    deTaiId = deTaiIdList.get(0);
                                    spinnerDeTai.setOnClickListener(v -> spinnerDeTai.showDropDown());
                                } else {
                                    spinnerDeTai.setText("", false);
                                    deTaiId = null;
                                }

                                // Listener khi chọn đề tài
                                spinnerDeTai.setOnItemClickListener((parent, view, position, id) -> {
                                    if (position >= 0 && position < deTaiIdList.size()) {
                                        deTaiId = deTaiIdList.get(position);
                                    }
                                });
                            });
                });
    }

    private void nopBaoCao() {
        if (deTaiId == null) {
            Toast.makeText(getContext(), "Bạn chưa có đề tài để nộp!", Toast.LENGTH_LONG).show();
            return;
        }
        saveBaoCao();
    }

    private void saveBaoCao() {
        String tenBaoCao = edtTenBaoCao.getText().toString().trim();
        String linkFile = edtLinkFile.getText().toString().trim();

        if (tenBaoCao.isEmpty() || linkFile.isEmpty()) {
            Toast.makeText(getContext(), "Nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        String ngayNop = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String baoCaoId = db.collection("baocao").document().getId();

        BaoCao baoCao = new BaoCao();
        baoCao.setBaoCaoId(baoCaoId);
        baoCao.setTenBaoCao(tenBaoCao);
        baoCao.setLinkFile(linkFile);
        baoCao.setSinhVienId(sinhVienId);
        baoCao.setDeTaiId(deTaiId);
        baoCao.setNgayNop(ngayNop);
        baoCao.setTrangThai("Chưa chấm");

        db.collection("baocao").document(baoCaoId).set(baoCao)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Đã nộp báo cáo!", Toast.LENGTH_SHORT).show();
                    edtTenBaoCao.setText("");
                    edtLinkFile.setText("");
                    fetchApprovedDeTai(); // cập nhật lại danh sách đề tài chưa nộp
                    loadBaoCao();
                });
    }

    private void loadBaoCao() {
        swipeRefresh.setRefreshing(true);

        db.collection("baocao")
                .whereEqualTo("sinhVienId", sinhVienId)
                .get()
                .addOnSuccessListener(query -> {
                    baoCaoList.clear();
                    for (QueryDocumentSnapshot doc : query) {
                        BaoCao bc = doc.toObject(BaoCao.class);
                        baoCaoList.add(bc);
                    }
                    adapter.notifyDataSetChanged();
                    swipeRefresh.setRefreshing(false);
                })
                .addOnFailureListener(e -> swipeRefresh.setRefreshing(false));
    }
}
