package com.darekeapp.fragments;

import android.app.Activity;
import android.arch.persistence.room.Room;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.darekeapp.R;
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
                    Toast.makeText(fragment.getContext(), "Share", Toast.LENGTH_LONG).show();
                    mode.finish();
                    return true;
                case R.id.action_multi_delete:
                    //
                    for (int i = 0; i < selectedShiftLogsList.size(); i++) {
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
    };
}
