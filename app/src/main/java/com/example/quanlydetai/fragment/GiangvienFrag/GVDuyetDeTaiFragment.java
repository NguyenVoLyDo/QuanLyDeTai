package com.example.quanlydetai.fragment.GiangvienFrag;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.quanlydetai.R;
import com.example.quanlydetai.adapters.DuyetDeTaiAdapter;
import com.example.quanlydetai.models.DeTai;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class GVDuyetDeTaiFragment extends Fragment {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private DuyetDeTaiAdapter adapter;
    private List<DeTai> deTaiList = new ArrayList<>();
    private FirebaseFirestore db;
    private TextView txtEmpty;
    private String maGV;

    public static GVDuyetDeTaiFragment newInstance(String maGV) {
        GVDuyetDeTaiFragment fragment = new GVDuyetDeTaiFragment();
        Bundle args = new Bundle();
        args.putString("maGV", maGV);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        requireActivity().setTitle("Duyệt đề tài");

        View view = inflater.inflate(R.layout.fragment_gv_duyet_detai, container, false);

        if (getArguments() != null) {
            maGV = getArguments().getString("maGV");
        }

        // Khởi tạo RecyclerView
        recyclerView = view.findViewById(R.id.recyclerDeTaiGV);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new DuyetDeTaiAdapter(getContext(), deTaiList);
        recyclerView.setAdapter(adapter);

        // SwipeRefresh
        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(this::loadDeTaiChuaDuyet);

        // Text hiển thị khi danh sách trống
        txtEmpty = view.findViewById(R.id.txtEmpty);

        db = FirebaseFirestore.getInstance();
        loadDeTaiChuaDuyet();

        return view;
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
                    swipeRefreshLayout.setRefreshing(false);

                    if (deTaiList.isEmpty()) {
                        recyclerView.setVisibility(View.GONE);
                        txtEmpty.setVisibility(View.VISIBLE);
                    } else {
                        recyclerView.setVisibility(View.VISIBLE);
                        txtEmpty.setVisibility(View.GONE);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Lỗi tải đề tài: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(false);
                });
    }
}
