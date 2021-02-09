package com.chaitanya.introtuceassignment1.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chaitanya.introtuceassignment1.R;
import com.chaitanya.introtuceassignment1.adapters.UsersListItemAdapter;
import com.chaitanya.introtuceassignment1.models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UsersListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UsersListFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ProgressBar progressBar;
    RecyclerView recyclerView;

    FirebaseFirestore db;
    List<Users> usersList = new ArrayList<>();

    UsersListItemAdapter adapter;
    Boolean secondTime = false;
    public TextView tvNoUser;

    public UsersListFragment() {
        // Required empty public constructor
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && secondTime) {
            getUsersList();
        } else {

        }
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UsersListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UsersListFragment newInstance(String param1, String param2) {
        UsersListFragment fragment = new UsersListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_users_list, container, false);

        progressBar = view.findViewById(R.id.pb_user_list);
        recyclerView = view.findViewById(R.id.rec_user_list);
        tvNoUser = view.findViewById(R.id.tv_no_user);
        db = FirebaseFirestore.getInstance();
        getUsersList();

        return view;
    }


    private void getUsersList() {
        secondTime = true;
        if (usersList.size() > 0) {
            usersList.clear();
        }
        progressBar.setVisibility(View.VISIBLE);
        db.collection("UserData")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                adduserInList(document);
                                Log.d("DOCS>>", document.getId() + " => " + document.getData());
                            }
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            adapter = new UsersListItemAdapter(usersList, getContext());
                            recyclerView.setAdapter(adapter);
                            progressBar.setVisibility(View.GONE);
                            if (usersList.size() < 1)
                                tvNoUser.setVisibility(View.VISIBLE);
                            else
                                tvNoUser.setVisibility(View.GONE);


                        } else {
                            progressBar.setVisibility(View.GONE);
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });
    }


    private void adduserInList(QueryDocumentSnapshot document) {
        Users users = new Users();
        users.setName(document.getData().get("first_name").toString());
        users.setLastName(document.getData().get("last_name").toString());
        users.setGender(document.getData().get("gender").toString());
        users.setDob(document.getData().get("dob").toString());
        users.setPhNo(document.getData().get("ph_no").toString());
        users.setProfileImage(document.getData().get("profile").toString());
        users.setTown(document.getData().get("town").toString());
        users.setId(document.getId());
        usersList.add(users);
    }
}