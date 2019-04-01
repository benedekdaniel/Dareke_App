package com.darekeapp.fragments;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.darekeapp.R;
import com.darekeapp.database.ShiftLog;
import com.darekeapp.utils.DiffUtilCallback;

import java.text.SimpleDateFormat;
import java.util.List;

public class ShiftLogAdapter extends RecyclerView.Adapter<ShiftLogAdapter.ViewHolder> {
    private List<ShiftLog> shiftLogs;
    private OnItemClickListener listener;
    private Context context;
    SimpleDateFormat formatter = new SimpleDateFormat("EEEE dd MMMM yyyy HH:mm");

    public ShiftLogAdapter(List<ShiftLog> shiftLogs) {
        this.shiftLogs = shiftLogs;
    }

    public void insertData(List<ShiftLog> insertList) {
        DiffUtilCallback diffUtilCallback = new DiffUtilCallback(shiftLogs, insertList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtilCallback);
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
