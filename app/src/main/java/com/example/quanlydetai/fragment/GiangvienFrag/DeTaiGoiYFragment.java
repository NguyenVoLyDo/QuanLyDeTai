package com.example.quanlydetai.fragment.GiangvienFrag;

import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.*;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.quanlydetai.R;
import com.example.quanlydetai.activitys.giangvienactivity.ThemDeTaiGoiYActivity;
import com.example.quanlydetai.adapters.DeTaiGoiYAdapter;
import com.example.quanlydetai.models.DeTaiGoiY;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import java.util.*;

public class DeTaiGoiYFragment extends Fragment {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private DeTaiGoiYAdapter adapter;
    private List<DeTaiGoiY> list = new ArrayList<>();
    private TextView txtEmpty;
    private FirebaseFirestore db;
    private String maGV;

    public static DeTaiGoiYFragment newInstance(String maGV) {
        DeTaiGoiYFragment fragment = new DeTaiGoiYFragment();
        Bundle args = new Bundle();
        args.putString("maGV", maGV);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        requireActivity().setTitle("Đề tài gợi ý cho sinh viên");

        View view = inflater.inflate(R.layout.fragment_goi_y_detai, container, false);

        if (getArguments() != null) {
            maGV = getArguments().getString("maGV", "");
        }

        db = FirebaseFirestore.getInstance();
        recyclerView = view.findViewById(R.id.recyclerDeTai);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);
        txtEmpty = view.findViewById(R.id.txtEmpty);

        adapter = new DeTaiGoiYAdapter(list, (v, deTai) -> showOptionMenu(v, deTai));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(this::loadData);

        FloatingActionButton fab = view.findViewById(R.id.fabAddDeTai);
        fab.setOnClickListener(v ->
                startActivity(new Intent(getActivity(), ThemDeTaiGoiYActivity.class)
                        .putExtra("maGV", maGV))
        );

        loadData();
        return view;
    }

    private void showOptionMenu(View anchor, DeTaiGoiY deTai) {
        PopupMenu popup = new PopupMenu(getContext(), anchor);
        popup.getMenu().add("Sửa đề tài");
        popup.getMenu().add("Xóa đề tài");

        popup.setOnMenuItemClickListener(item -> {
            if (item.getTitle().equals("Xóa đề tài")) {
                confirmDelete(deTai);
                return true;
            }
            return false;
        });

        popup.show();
    }

    private void confirmDelete(DeTaiGoiY deTai) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Xác nhận xoá")
                .setMessage("Bạn có chắc muốn xoá \"" + deTai.getTenDeTai() + "\" không?")
                .setPositiveButton("Xoá", (d, w) -> deleteDeTai(deTai))
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void deleteDeTai(DeTaiGoiY deTai) {
        db.collection("detai_goiy").document(deTai.getDeTaiGoiYId()).delete()
                .addOnSuccessListener(a -> {
                    Toast.makeText(getContext(), "Đã xoá!", Toast.LENGTH_SHORT).show();
                    loadData();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(getContext(), "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void loadData() {
        swipeRefreshLayout.setRefreshing(true);
        db.collection("detai_goiy")
                .whereEqualTo("giangVienId", maGV)
                .get()
                .addOnSuccessListener(q -> {
                    list.clear();
                    for (QueryDocumentSnapshot doc : q) {
                        list.add(doc.toObject(DeTaiGoiY.class));
                    }
                    updateUI();
                })
                .addOnFailureListener(e -> updateUI());
    }

    private void updateUI() {
        adapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
        txtEmpty.setVisibility(list.isEmpty() ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(list.isEmpty() ? View.GONE : View.VISIBLE);
    }
}
