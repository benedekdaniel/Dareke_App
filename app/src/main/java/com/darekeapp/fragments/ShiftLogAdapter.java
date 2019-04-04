package com.darekeapp.fragments;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.darekeapp.R;
import com.darekeapp.database.ShiftLog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ShiftLogAdapter extends RecyclerView.Adapter<ShiftLogAdapter.ViewHolder>
        implements Filterable {
    private SimpleDateFormat formatter = new SimpleDateFormat("EEEE dd MMMM yyyy HH:mm");

    private List<ShiftLog> shiftLogs;
    private List<ShiftLog> shiftLogsSearch;
    private OnItemClickListener listener;

    public ShiftLogAdapter(List<ShiftLog> shiftLogs) {
        this.shiftLogs = shiftLogs;
        this.shiftLogsSearch = new ArrayList<>(shiftLogs);
    }

    public void removeShiftLog(final int position) {
        shiftLogs.remove(position);
        notifyItemRemoved(position);
    }

    public List<ShiftLog> getData() {
        return shiftLogs;
    }

    @NonNull
    @Override
    public ShiftLogAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_row,
                viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShiftLogAdapter.ViewHolder viewHolder, int position) {
        viewHolder.companyName.setText(shiftLogs.get(position).getCompanyName());
        viewHolder.shiftStart.setText("Shift start: " + formatter.format(shiftLogs.get(position)
                .getShiftStart()));
        viewHolder.shiftEnd.setText("Shift end: " + formatter.format(shiftLogs.get(position)
                .getShiftEnd()));
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

            return  filteredResults;
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            companyName = itemView.findViewById(R.id.company_name_list_text);
            shiftStart = itemView.findViewById(R.id.shift_start_list_text);
            shiftEnd = itemView.findViewById(R.id.shift_end_list_text);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION){
                        listener.onItemClick(shiftLogs.get(position));
                    }
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
}
