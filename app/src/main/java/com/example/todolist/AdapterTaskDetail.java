package com.example.todolist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Model.Task;

import java.util.ArrayList;
import java.util.List;

public class AdapterTaskDetail extends RecyclerView.Adapter<AdapterTaskDetail.Viewhoder> {
    List<Task> taskList = new ArrayList<>();
    IOnClickCheckBox iOnClickCheckBox;

    public AdapterTaskDetail(List<Task> taskList, IOnClickCheckBox iOnClickCheckBox) {
        this.taskList = taskList;
        this.iOnClickCheckBox = iOnClickCheckBox;
    }

    @NonNull
    @Override
    public Viewhoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return(new Viewhoder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_task_detail, parent, false)));
    }

    @Override
    public void onBindViewHolder(@NonNull Viewhoder holder, int position) {
        Task task = taskList.get(position);

        holder.tevName.setText(task.getTaskName());
        if(task.getTaskCompleteYN() == 1) holder.checkBox.setChecked(true);
        else holder.checkBox.setChecked(false);

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(task.getTaskCompleteYN() == 1) {
                    task.setTaskCompleteYN(0);
                    holder.checkBox.setChecked(false);
                } else {
                    task.setTaskCompleteYN(1);
                    holder.checkBox.setChecked(true);
                }
                iOnClickCheckBox.onClickCheckBox(task);
            }
        });

    }

    @Override
    public int getItemCount() {
        if(taskList != null) return taskList.size();
        else return 0;
    }

    public class Viewhoder extends RecyclerView.ViewHolder{
        TextView tevName;
        CheckBox checkBox;
        public Viewhoder(@NonNull View itemView) {
            super(itemView);

            tevName = itemView.findViewById(R.id.fragment_task_detail_tev);
            checkBox = itemView.findViewById(R.id.fragment_task_detail_che);
        }
    }
}
