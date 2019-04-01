package com.darekeapp.fragments;

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

import java.util.List;

public class ShiftLogAdapter extends RecyclerView.Adapter<ShiftLogAdapter.ViewHolder> {

    private List<ShiftLog> shiftLogs;
    private OnItemClickListener listener;

    public ShiftLogAdapter(List<ShiftLog> shiftLogs) {
        this.shiftLogs = shiftLogs;
    }

    public void insertData(List<ShiftLog> insertList) {
        DiffUtilCallback myDiffUtilCallback = new DiffUtilCallback(shiftLogs, insertList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(myDiffUtilCallback);
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
        viewHolder.shiftStart.setText("Shift start: " + shiftLogs.get(position)
                .getShiftStart().toString());
        viewHolder.shiftEnd.setText("Shift end: " + shiftLogs.get(position)
                .getShiftEnd().toString());
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
