package com.example.quanlydetai.fragment.StudentFrag;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlydetai.R;
import com.example.quanlydetai.adapters.ThongBaoAdapter;
import com.example.quanlydetai.models.ThongBao;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ThongBaoFragment extends Fragment {

    private RecyclerView recyclerView;
    private ThongBaoAdapter adapter;
    private List<ThongBao> thongBaoList = new ArrayList<>();
    private FirebaseFirestore db;
    private String maSV;

    public static ThongBaoFragment newInstance(String maSV) {
        ThongBaoFragment fragment = new ThongBaoFragment();
        Bundle args = new Bundle();
        args.putString("maSV", maSV);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        requireActivity().setTitle("Thông báo");

        View view = inflater.inflate(R.layout.fragment_thongbao, container, false);

        if (getArguments() != null)
            maSV = getArguments().getString("maSV");

        recyclerView = view.findViewById(R.id.recyclerThongBao);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ThongBaoAdapter(thongBaoList);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        loadThongBao();

        return view;
    }

    private void loadThongBao() {
        db.collection("thongbao")
                .whereIn("nguoiNhan", Arrays.asList(maSV, "all"))
                .get()
                .addOnSuccessListener(snapshot -> {
                    thongBaoList.clear();
                    for (QueryDocumentSnapshot doc : snapshot)
                        thongBaoList.add(doc.toObject(ThongBao.class));

                    adapter.notifyDataSetChanged();
                });
    }
}

