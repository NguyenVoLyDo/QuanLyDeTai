package com.example.quanlydetai.fragment.GiangvienFrag;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.quanlydetai.R;
import com.example.quanlydetai.adapters.ChamBaoCaoAdapter;
import com.example.quanlydetai.models.BaoCao;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ChamBaoCaoFragment extends Fragment {

    private ChamBaoCaoAdapter adapter;
    private List<BaoCao> baoCaoList;
    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView txtEmpty;

    public ChamBaoCaoFragment() {
        // Required empty public constructor
    }

    public static ChamBaoCaoFragment newInstance(String maGV) {
        return new ChamBaoCaoFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        requireActivity().setTitle("Chấm báo cáo");

        View view = inflater.inflate(R.layout.fragment_cham_bao_cao, container, false);

        recyclerView = view.findViewById(R.id.recyclerChamBaoCao);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        txtEmpty = view.findViewById(R.id.txtEmpty);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);

        baoCaoList = new ArrayList<>();
        boolean isGiangVien = true;
        adapter = new ChamBaoCaoAdapter(getContext(), baoCaoList, isGiangVien);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        loadBaoCao();

        swipeRefreshLayout.setOnRefreshListener(this::loadBaoCao);

        return view;
    }

    private void loadBaoCao() {
        swipeRefreshLayout.setRefreshing(true);

        db.collection("baocao")
                .whereEqualTo("trangThai", "Chưa chấm")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    baoCaoList.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        BaoCao bc = doc.toObject(BaoCao.class);
                        bc.setDeTaiId(doc.getId());
                        baoCaoList.add(bc);
                    }
                    adapter.notifyDataSetChanged();

                    // Hiển thị EmptyView nếu danh sách trống
                    if (baoCaoList.isEmpty()) {
                        recyclerView.setVisibility(View.GONE);
                        txtEmpty.setVisibility(View.VISIBLE);
                    } else {
                        recyclerView.setVisibility(View.VISIBLE);
                        txtEmpty.setVisibility(View.GONE);
                    }

                    swipeRefreshLayout.setRefreshing(false);
                })
                .addOnFailureListener(e -> {
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(getContext(), "Lỗi tải dữ liệu: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
