package com.example.quanlydetai.fragment.StudentFrag;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.quanlydetai.R;
import com.example.quanlydetai.activitys.studentactivity.DangKyDeTaiActivity;
import com.example.quanlydetai.adapters.HienThiDeTaiAdapter;
import com.example.quanlydetai.models.DeTai;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class StudentDeTaiFragment extends Fragment {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private HienThiDeTaiAdapter adapter;
    private List<DeTai> deTaiList = new ArrayList<>();
    private TextView txtEmpty;
    private FirebaseFirestore db;
    private String maSV;

    public StudentDeTaiFragment() {
        // Required empty constructor
    }

    public static StudentDeTaiFragment newInstance(String maSV) {
        StudentDeTaiFragment fragment = new StudentDeTaiFragment();
        Bundle args = new Bundle();
        args.putString("maSV", maSV);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_student_detai, container, false);

        if (getArguments() != null) {
            maSV = getArguments().getString("maSV");
        }

        db = FirebaseFirestore.getInstance();

        recyclerView = view.findViewById(R.id.recyclerDeTai);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);
        txtEmpty = view.findViewById(R.id.txtEmpty); //  Text thông báo trống

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new HienThiDeTaiAdapter(deTaiList);
        recyclerView.setAdapter(adapter);


        swipeRefreshLayout.setOnRefreshListener(this::loadDeTai);

        FloatingActionButton fab = view.findViewById(R.id.fabAddDeTai);
        fab.setOnClickListener(v ->
                startActivity(new Intent(getActivity(), DangKyDeTaiActivity.class).putExtra("maSV", maSV))
        );

        loadDeTai();

        return view;
    }

    private void loadDeTai() {
        swipeRefreshLayout.setRefreshing(true);

        db.collection("detai")
                .whereEqualTo("sinhVienId", maSV)
                .get()
                .addOnSuccessListener(query -> {
                    deTaiList.clear();
                    for (QueryDocumentSnapshot doc : query) {
                        DeTai dt = doc.toObject(DeTai.class);
                        deTaiList.add(dt);
                    }
                    adapter.notifyDataSetChanged();
                    updateUI();

                })
                .addOnFailureListener(e -> {
                    Log.e("FIRESTORE_ERROR", e.getMessage());
                    updateUI();
                });
    }

    private void updateUI() {
        adapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);

        if (deTaiList.isEmpty()) {
            txtEmpty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            txtEmpty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
}
