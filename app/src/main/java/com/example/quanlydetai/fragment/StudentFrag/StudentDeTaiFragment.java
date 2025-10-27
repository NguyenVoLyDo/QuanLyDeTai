package com.example.quanlydetai.fragment.StudentFrag;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler; // Thêm Handler
import android.os.Looper;  // Thêm Looper
import android.text.Editable; // Thêm Editable
import android.text.TextWatcher; // Thêm TextWatcher
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.quanlydetai.activitys.studentactivity.SuaDeTaiActivity;
import com.google.android.material.textfield.TextInputEditText; // Thêm TextInputEditText

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.quanlydetai.R;
import com.example.quanlydetai.activitys.studentactivity.DangKyDeTaiActivity;
import com.example.quanlydetai.adapters.HienThiDeTaiAdapter; // Đảm bảo đúng Adapter
import com.example.quanlydetai.models.DeTai;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class StudentDeTaiFragment extends Fragment {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private HienThiDeTaiAdapter adapter; // Adapter có Filterable
    private TextView txtEmpty;
    private FirebaseFirestore db;
    private String maSV;
    private TextInputEditText edtSearch; // Biến cho EditText tìm kiếm trong Fragment
    private Handler searchHandler = new Handler(Looper.getMainLooper()); // Handler cho debounce
    private Runnable searchRunnable; // Runnable cho debounce

    public StudentDeTaiFragment() { }

    public static StudentDeTaiFragment newInstance(String maSV) {
        StudentDeTaiFragment fragment = new StudentDeTaiFragment();
        Bundle args = new Bundle();
        args.putString("maSV", maSV);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            maSV = getArguments().getString("maSV");
        }
        db = FirebaseFirestore.getInstance();
        // Khởi tạo adapter với list rỗng
        adapter = new HienThiDeTaiAdapter(new ArrayList<>(), (view, deTai) -> {
            showOptionMenu(view, deTai);
        });
    }

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Đảm bảo layout này có EditText với ID edtSearchMain (hoặc edtSearch)
        View view = inflater.inflate(R.layout.fragment_student_detai, container, false);

        // Ánh xạ Views
        recyclerView = view.findViewById(R.id.recyclerDeTai);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);
        txtEmpty = view.findViewById(R.id.txtEmpty);
        FloatingActionButton fab = view.findViewById(R.id.fabAddDeTai);
        edtSearch = view.findViewById(R.id.edtSearch);

        edtSearch.setOnClickListener(v -> {
            edtSearch.setFocusable(true);
            edtSearch.setFocusableInTouchMode(true);
            edtSearch.requestFocus();
        });

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        // Setup SwipeRefresh
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setOnRefreshListener(this::loadDeTai);
        }

        // Setup FAB
        if (fab != null) {
            fab.setOnClickListener(v ->
                    startActivity(new Intent(getActivity(), DangKyDeTaiActivity.class).putExtra("maSV", maSV))
            );
        }

        // --- THÊM: Listener cho thanh tìm kiếm ---
        if (edtSearch != null) {
            edtSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    // Hủy bỏ runnable cũ khi người dùng gõ tiếp
                    if (searchRunnable != null) {
                        searchHandler.removeCallbacks(searchRunnable);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                    // Tạo runnable mới để lọc sau 500ms
                    searchRunnable = () -> {
                        filterListInternal(s.toString()); // Gọi hàm lọc nội bộ
                    };
                    searchHandler.postDelayed(searchRunnable, 500); // Đặt lịch
                }
            });
        } else {
            Log.e("FragmentSetup", "EditText for search (edtSearchMain or edtSearch) not found in layout!");
            // Có thể hiện Toast báo lỗi ở đây
        }


        loadDeTai(); // Tải dữ liệu lần đầu

        return view;
    }

    private void loadDeTai() {
        if (swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(true);
        if (txtEmpty != null) txtEmpty.setVisibility(View.GONE);
        if (recyclerView != null) recyclerView.setVisibility(View.VISIBLE);

        db.collection("detai")
                .whereEqualTo("sinhVienId", maSV)
                .get()
                .addOnCompleteListener(task -> {
                    if (swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(false);
                    List<DeTai> tempList = new ArrayList<>();
                    if (task.isSuccessful() && task.getResult() != null) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            tempList.add(doc.toObject(DeTai.class));
                        }
                        Log.d("LOAD_DATA", "Firestore success! Found " + tempList.size() + " items.");
                    } else {
                        Log.e("FIRESTORE_ERROR", "Error loading data: ", task.getException());
                        if (txtEmpty != null) txtEmpty.setText("Lỗi tải dữ liệu");
                    }
                    if (adapter != null) {
                        adapter.updateList(tempList); // Cập nhật adapter
                    }
                    updateUIState();
                });
    }

    private void showOptionMenu(View anchor, DeTai deTai) {
        androidx.appcompat.widget.PopupMenu popupMenu =
                new androidx.appcompat.widget.PopupMenu(getContext(), anchor);
        popupMenu.getMenu().add("Sửa đề tài");
        popupMenu.getMenu().add("Xóa đề tài");

        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getTitle().equals("Sửa đề tài")) {
                Intent intent = new Intent(getActivity(), SuaDeTaiActivity.class);
                intent.putExtra("deTaiId", deTai.getDeTaiId());
                startActivity(intent);
                return true;
            }
            if (item.getTitle().equals("Xóa đề tài")) {
                confirmDelete(deTai);
                return true;
            }
            return false;
        });

        popupMenu.show();
    }

    private void confirmDelete(DeTai deTai) {
        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc muốn xóa \"" + deTai.getTenDeTai() + "\" không?")
                .setPositiveButton("Xóa", (dialog, which) -> deleteDeTai(deTai))
                .setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void deleteDeTai(DeTai deTai) {
        db.collection("detai").document(deTai.getDeTaiId()).delete()
                .addOnSuccessListener(a -> {
                    Log.d("DELETE", "Deleted: " + deTai.getTenDeTai());
                    loadDeTai();
                })
                .addOnFailureListener(e -> Log.e("DELETE_ERROR", e.getMessage()));
    }

    private void updateUIState() {
        if (adapter != null && txtEmpty != null && recyclerView != null) {
            boolean isEmpty = adapter.getItemCount() == 0;
            txtEmpty.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
            recyclerView.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
            if (isEmpty && (swipeRefreshLayout == null || !swipeRefreshLayout.isRefreshing())) {
                txtEmpty.setText("Danh sách hiện đang trống");
            }
        }
    }

    // --- Hàm lọc nội bộ được gọi bởi TextWatcher ---
    private void filterListInternal(String text) {
        if (adapter != null && adapter instanceof Filterable) {
            Log.d("FragmentFilter", "Filtering internally with: '" + text + "'");
            ((Filterable) adapter).getFilter().filter(text); // Gọi filter của adapter
            if (recyclerView != null) {
                // Cập nhật trạng thái trống sau khi lọc
                recyclerView.postDelayed(this::updateUIState, 300);
            }
        } else {
            Log.e("FragmentFilter", "Adapter is null or not Filterable!");
        }
    }

    public void filterList(String query) {
    }


}