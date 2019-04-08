package com.darekeapp.fragments;

import android.app.Activity;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.darekeapp.R;
import com.darekeapp.activities.ShiftLogDataActivity;
import com.darekeapp.database.ShiftLog;
import com.darekeapp.database.ShiftLogDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ShiftLogAdapter extends RecyclerView.Adapter<ShiftLogAdapter.ViewHolder>
        implements Filterable {
    private SimpleDateFormat formatter = new SimpleDateFormat("EEEE dd MMMM yyyy HH:mm");

    private List<ShiftLog> shiftLogs;
    private List<ShiftLog> shiftLogsSearch;
    private OnItemClickListener listener;
    private ShiftLogsFragment fragment;
    private boolean isInActionMode;
    public CheckBox selectCheckBox;
    private List<ShiftLog> selectedShiftLogsList;

    public ShiftLogAdapter(List<ShiftLog> shiftLogs, ShiftLogsFragment fragment) {
        this.shiftLogs = shiftLogs;
        this.shiftLogsSearch = new ArrayList<>(shiftLogs);
        this.fragment = fragment;
        this.selectedShiftLogsList = new ArrayList<>();
    }

    public void removeShiftLog(final int position) {
        shiftLogs.remove(position);
        notifyItemRemoved(position);
    }

    public void deleteShiftLog(final ShiftLog shiftLog) {
        final ShiftLogDatabase db = Room.databaseBuilder(fragment.getContext(),
                ShiftLogDatabase.class,
                "ShiftLogDatabase").build();
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                db.shiftLogDao().deleteShiftLog(shiftLog);
            }
        });
    }

    public List<ShiftLog> getData() {
        return shiftLogs;
    }

    @NonNull
    @Override
    public ShiftLogAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_row,
                viewGroup, false);

        return new ViewHolder(view, ShiftLogAdapter.this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        viewHolder.companyName.setText(shiftLogs.get(position).getCompanyName());
        viewHolder.shiftStart.setText("Shift start: " + formatter.format(shiftLogs.get(position)
                .getShiftStart()));
        viewHolder.shiftEnd.setText("Shift end: " + formatter.format(shiftLogs.get(position)
                .getShiftEnd()));

        selectCheckBox = viewHolder.itemView.findViewById(R.id.shift_log_checkbox);

        if (!isInActionMode) {
            selectCheckBox.setVisibility(View.GONE);
        } else {
            selectCheckBox.setVisibility(View.VISIBLE);
            selectCheckBox.setChecked(false);
        }

        selectCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    selectedShiftLogsList.add(shiftLogs.get(position));
                } else {
                    selectedShiftLogsList.remove(shiftLogs.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return shiftLogs.size();
    }

    @Override
    public Filter getFilter() {
        return shiftLogFilter;
    }

    private Filter shiftLogFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<ShiftLog> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(shiftLogsSearch);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (ShiftLog shiftLog : shiftLogsSearch) {
                    if (shiftLog.getCompanyName().toLowerCase().contains(filterPattern) ||
                            shiftLog.getShiftStart().toString().toLowerCase().contains(filterPattern) ||
                            shiftLog.getShiftEnd().toString().toLowerCase().contains(filterPattern)) {
                        filteredList.add(shiftLog);
                    }
                }
            }

            FilterResults filteredResults = new FilterResults();
            filteredResults.values = filteredList;

            return filteredResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            shiftLogs.clear();
            shiftLogs.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView companyName;
        public TextView shiftStart;
        public TextView shiftEnd;
        public ActionMode actionMode;
        public ShiftLogAdapter shiftLogAdapter;

        public ViewHolder(@NonNull View itemView, ShiftLogAdapter rView) {
            super(itemView);
            companyName = itemView.findViewById(R.id.company_name_list_text);
            shiftStart = itemView.findViewById(R.id.shift_start_list_text);
            shiftEnd = itemView.findViewById(R.id.shift_end_list_text);
            shiftLogAdapter = rView;
            isInActionMode = false;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION){
                        listener.onItemClick(shiftLogs.get(position));
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Activity activity = fragment.getActivity();
                    actionMode = ((AppCompatActivity) activity).startSupportActionMode(
                            new ContextualCallBack(ViewHolder.this));
                    setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(ShiftLog shiftLog) {
                            // Do nothing.
                        }
                    });
                    return true;
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(ShiftLog shiftLog);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public class ContextualCallBack implements ActionMode.Callback {
        private ViewHolder viewHolder;

        public ContextualCallBack(ViewHolder viewHolder) {
            this.viewHolder = viewHolder;
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.multi_select_menu, menu);
            isInActionMode = true;
            viewHolder.shiftLogAdapter.notifyDataSetChanged();
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_multi_share:
                    String message = "";
                    for (ShiftLog shiftLog : selectedShiftLogsList) {
                        message += formatShiftLogMessage(shiftLog);
                    }
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Shift Log");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, message);
                    fragment.getActivity().startActivity(Intent.createChooser(sharingIntent, "Share via"));
                    mode.finish();
                    return true;
                case R.id.action_multi_delete:
                    for (int i = selectedShiftLogsList.size() - 1; i >= 0; i--) {
                        viewHolder.shiftLogAdapter.removeShiftLog(i);
                        viewHolder.shiftLogAdapter.deleteShiftLog(selectedShiftLogsList.get(i));
                    }
                    viewHolder.shiftLogAdapter.notifyDataSetChanged();
                    mode.finish();
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            isInActionMode = false;
            viewHolder.shiftLogAdapter.notifyDataSetChanged();
        }
    }

    private String formatShiftLogMessage(ShiftLog shiftLog) {
        String message;
        ShiftLogDataActivity shiftLogDataActivity = new ShiftLogDataActivity();

        message = "Company name: " + shiftLog.getCompanyName() + "\r\n" +
                "Worked for agent? " + shiftLog.isWorkedForAgent() + "\r\n";

        if (shiftLog.isWorkedForAgent()) {
            message += "Agent name: " + shiftLog.getAgentName() + "\r\n";
        }

        message += "Shift start: " +
                shiftLogDataActivity.dateFormatter.format(shiftLog.getShiftStart()) + "\r\n" +
                "Shift end: " +
                shiftLogDataActivity.dateFormatter.format(shiftLog.getShiftEnd()) + "\r\n" +
                "Break taken? " + shiftLog.isBreakTaken() + "\r\n";

        if (shiftLog.isBreakTaken()) {
            message += "Break start: " +
                    shiftLogDataActivity.dateFormatter.format(shiftLog.getBreakStart()) + "\r\n" +
                    "Break end: " +
                    shiftLogDataActivity.dateFormatter.format(shiftLog.getBreakEnd()) + "\r\n";
        }

        message += "Governed by driver hours? " + shiftLog.isGovernedByDriverHours() + "\r\n";

        if (shiftLog.isGovernedByDriverHours()) {
            message += "Vehicle registration: " + shiftLog.getVehicleRegistration() + "\r\n" +
                    "POA time: " +
                    shiftLogDataActivity.formatTime(shiftLog.getPoaTime()) + "\r\n" +
                    "Drive time: " +
                    shiftLogDataActivity.formatTime(shiftLog.getDriveTime()) + "\r\n";
        } else {
            message += "\r\n";
        }

        // Return the formatted message with the given shift details.
        return message;
    }

    ;
}
