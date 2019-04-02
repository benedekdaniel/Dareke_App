package com.darekeapp.fragments;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.darekeapp.R;
import com.darekeapp.activities.ShiftLogDataActivity;
import com.darekeapp.database.ShiftLog;
import com.darekeapp.database.ShiftLogDatabase;
import com.darekeapp.utils.SwipeToDeleteCallback;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class ShiftLogsFragment extends Fragment {
    public static final String EXTRA_COMPANY_NAME = "COMPANY_NAME";
    public static final String EXTRA_WORKED_FOR_AGENT = "WORKED_FOR_AGENT";
    public static final String EXTRA_AGENT_NAME = "AGENT_NAME";
    public static final String EXTRA_SHIFT_START = "SHIFT_START";
    public static final String EXTRA_SHIFT_END = "SHIFT_END";
    public static final String EXTRA_BREAK_TAKEN = "BREAK_TAKEN";
    public static final String EXTRA_BREAK_START = "BREAK_START";
    public static final String EXTRA_BREAK_END = "BREAK_END";
    public static final String EXTRA_GOVERNED_BY_DRIVER_HOURS = "GOVERNED_BY_DRIVER_HOURS";
    public static final String EXTRA_VEHICLE_REGISTRATION = "VEHICLE_REGISTRATION";
    public static final String EXTRA_POA_TIME = "POA_TIME";
    public static final String EXTRA_DRIVE_TIME = "DRIVE_TIME";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener listener;

    private RecyclerView recyclerView;
    private ShiftLogAdapter adapter;

    public ShiftLogsFragment() {
        // Required empty public constructor.
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
        // Inflate the layout for this fragment.
        View view =  inflater.inflate(R.layout.fragment_shift_logs, container, false);

        recyclerView = view.findViewById(R.id.shift_list_recycler_view);

        enableSwipeToDeleteAndUndo();

        ShiftLogDatabase db = Room.databaseBuilder(getContext(), ShiftLogDatabase.class,
                "ShiftLogDatabase")
                .allowMainThreadQueries()
                .build();

        List<ShiftLog> shiftLogs = db.shiftLogDao().getAllShiftLogs(
                FirebaseAuth.getInstance().getCurrentUser().getUid());

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext()));
        adapter = new ShiftLogAdapter(shiftLogs);

        // Add line divider after each shift log.
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity()
                .getBaseContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(getResources()
                .getDrawable(R.drawable.recyclerview_divider));
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(
                new ShiftLogAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ShiftLog shiftLog) {
                Intent intent = new Intent(getActivity(), ShiftLogDataActivity.class);
                intent.putExtra(EXTRA_COMPANY_NAME, shiftLog.getCompanyName());
                intent.putExtra(EXTRA_WORKED_FOR_AGENT, shiftLog.isWorkedForAgent());
                intent.putExtra(EXTRA_AGENT_NAME, shiftLog.getAgentName());
                intent.putExtra(EXTRA_SHIFT_START, shiftLog.getShiftStart());
                intent.putExtra(EXTRA_SHIFT_END, shiftLog.getShiftEnd());
                intent.putExtra(EXTRA_BREAK_TAKEN, shiftLog.isBreakTaken());
                intent.putExtra(EXTRA_BREAK_START, shiftLog.getBreakStart());
                intent.putExtra(EXTRA_BREAK_END, shiftLog.getBreakEnd());
                intent.putExtra(EXTRA_GOVERNED_BY_DRIVER_HOURS,
                        shiftLog.isGovernedByDriverHours());
                intent.putExtra(EXTRA_VEHICLE_REGISTRATION, shiftLog.getVehicleRegistration());
                intent.putExtra(EXTRA_POA_TIME, shiftLog.getPoaTime());
                intent.putExtra(EXTRA_DRIVE_TIME, shiftLog.getDriveTime());
                startActivity(intent);
            }
        });

        return view;
    }

    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(
                getActivity().getApplicationContext()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                final int position = viewHolder.getAdapterPosition();
                final ShiftLog item = adapter.getData().get(position);

                adapter.removeShiftLog(position);
                final ShiftLogDatabase db = Room.databaseBuilder(getContext(),
                        ShiftLogDatabase.class,
                        "ShiftLogDatabase").build();
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        db.shiftLogDao().deleteShiftLog(item);
                    }
                });
                db.close();

                Snackbar snackbar = Snackbar
                        .make(getActivity().findViewById(android.R.id.content),
                                "Shift log deleted.", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (listener != null) {
            listener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
