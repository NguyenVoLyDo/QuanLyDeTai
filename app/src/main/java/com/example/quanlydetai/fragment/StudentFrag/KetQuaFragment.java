package com.example.quanlydetai.fragment.StudentFrag;

import android.os.Bundle;
import android.widget.Toast;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlydetai.R;
import com.example.quanlydetai.adapters.KetQuaAdapter;
import com.example.quanlydetai.models.BaoCao;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class KetQuaFragment extends Fragment {

    private RecyclerView recyclerView;
    private KetQuaAdapter adapter;
    private final List<BaoCao> baoCaoList = new ArrayList<>();
    private FirebaseFirestore db;
    private String sinhVienId;

    public static KetQuaFragment newInstance(String maSV) {
        KetQuaFragment fragment = new KetQuaFragment();
        Bundle args = new Bundle();
        args.putString("maSV", maSV);
        fragment.setArguments(args);
        return fragment;
    }

    public KetQuaFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_ket_qua, container, false);

        if (getArguments() != null) {
            sinhVienId = getArguments().getString("maSV");
        }

        db = FirebaseFirestore.getInstance();

        recyclerView = view.findViewById(R.id.recyclerKetQua);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new KetQuaAdapter(baoCaoList, requireContext());
        recyclerView.setAdapter(adapter);

        loadKetQua();
        return view;
    }

    private void loadKetQua() {
        db.collection("baocao")
                .whereEqualTo("sinhVienId", sinhVienId)
                .whereEqualTo("trangThai", "Đã chấm")
                .get()
                .addOnSuccessListener(query -> {
                    baoCaoList.clear();
                    for (QueryDocumentSnapshot doc : query) {
                        BaoCao bc = doc.toObject(BaoCao.class);
                        bc.setBaoCaoId(doc.getId());
                        baoCaoList.add(bc);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(getContext(), "Lỗi tải kết quả: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }
}
