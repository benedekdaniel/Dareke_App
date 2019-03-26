package com.darekeapp.fragments;

import android.arch.persistence.room.Room;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.darekeapp.R;
import com.darekeapp.database.ShiftLog;
import com.darekeapp.database.ShiftLogDatabase;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;


public class ShiftLogsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
//    ArrayList<ShiftLog> users;

    public ShiftLogsFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ShiftLogsFragment newInstance(String param1, String param2) {
        ShiftLogsFragment fragment = new ShiftLogsFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView =  inflater.inflate(R.layout.fragment_shift_logs, container, false);

        recyclerView = myView.findViewById(R.id.shift_list_recycle);

        ShiftLogDatabase db = Room.databaseBuilder(getContext(), ShiftLogDatabase.class, "ShiftLogDatabase")
                .allowMainThreadQueries()
                .build();

        List<ShiftLog> users = db.shiftLogDao().getAllShiftLogs(
                FirebaseAuth.getInstance().getCurrentUser().getUid());

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext()));

        adapter = new UserAdapter(users);

        recyclerView.setAdapter(adapter);

        return myView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
