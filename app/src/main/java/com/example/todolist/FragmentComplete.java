package com.example.todolist;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.Model.Task;
import com.example.SQL.SQLClient;
import com.example.todolist.databinding.FragmentCompleteBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;


public class FragmentComplete extends Fragment {
    FragmentCompleteBinding binding;
    AdapterTaskDetail adapterTaskDetail;
    SQLClient sqlClient;
    List<Task> taskList = new ArrayList<>();
    List<Task> taskListComplete = new ArrayList<>();

    public static FragmentComplete newInstance(Bundle bundle) {
        FragmentComplete fragment = new FragmentComplete();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_complete, container, false);
        sqlClient = new SQLClient(binding.getRoot().getContext());
        Bundle bundleData = getArguments();
        if(bundleData.getParcelableArrayList("taskList") != null) taskList = bundleData.getParcelableArrayList("taskList");
        for (Task task: taskList) {
            if(task.getTaskCompleteYN() == 1) taskListComplete.add(task);
        }

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(binding.getRoot().getContext(), LinearLayoutManager.VERTICAL, false);
        binding.fragmentCompleteRecAllTask.setLayoutManager(layoutManager);
        adapterTaskDetail = new AdapterTaskDetail(taskListComplete, new IOnClickCheckBox() {
            @Override
            public void onClickCheckBox(Task task) {
                sqlClient.updateTask(task);
                int index = taskListComplete.indexOf(task);
                taskListComplete.remove(task);
                adapterTaskDetail.notifyItemRemoved(index);
                String taskName = task.getTaskName();
                Snackbar snackbar = Snackbar.make(binding.getRoot(), taskName+" has been removed from CompletedTask", Snackbar.LENGTH_LONG);
                snackbar.setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(task.getTaskCompleteYN() == 0) task.setTaskCompleteYN(1);
                        else task.setTaskCompleteYN(0);
                        taskListComplete.add(index, task);
                        sqlClient.updateTask(task);
                        adapterTaskDetail.notifyItemInserted(index);
                    }
                });
                snackbar.setActionTextColor(getResources().getColor(R.color.blue_light));
                snackbar.show();
            }
        });
        binding.fragmentCompleteRecAllTask.setAdapter(adapterTaskDetail);


        return binding.getRoot();
    }
}