package com.example.todolist;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Model.Task;
import com.example.SQL.SQLClient;
import com.example.todolist.databinding.FragmentAllBinding;

import java.util.ArrayList;
import java.util.List;


public class FragmentAll extends Fragment {
    FragmentAllBinding binding;
    AdapterTaskDetail adapterTaskDetail;
    SQLClient sqlClient;
    List<Task> taskList = new ArrayList<>();
    int maxID = 0;


    public static FragmentAll newInstance(Bundle bundle) {
        FragmentAll fragment = new FragmentAll();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_all, container, false);
        sqlClient = new SQLClient(binding.getRoot().getContext());
        Bundle bundleData = getArguments();
        if(bundleData.getParcelableArrayList("taskList") != null) taskList = bundleData.getParcelableArrayList("taskList");

        if(taskList.size() > 0) {
            for (Task task: taskList) {
                if(task.getTaskID() > maxID) maxID = task.getTaskID();
            }
        }

        maxID = maxID + 1;

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(binding.getRoot().getContext(), LinearLayoutManager.VERTICAL, false);
        binding.fragmentAllRecAllTask.setLayoutManager(layoutManager);
        adapterTaskDetail = new AdapterTaskDetail(taskList, new IOnClickCheckBox() {
            @Override
            public void onClickCheckBox(Task task) {
                sqlClient.updateTask(task);
            }
        });
        binding.fragmentAllRecAllTask.setAdapter(adapterTaskDetail);
        binding.fragmentAllTevCreateTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
        return binding.getRoot();
    }

    private void showDialog(){
        final Dialog dialog = new Dialog(binding.getRoot().getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_create_task);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView tevSubmit = dialog.findViewById(R.id.dialog_create_task_tev_submit);
        EditText taskName = dialog.findViewById(R.id.dialog_create_task_edt);
        CheckBox taskCheck = dialog.findViewById(R.id.dialog_create_task_che);

        tevSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int taskCompleteYN;
                if(taskCheck.isChecked()) taskCompleteYN = 1; else taskCompleteYN = 0;
                Task task = new Task(maxID, taskName.getText().toString(), taskCompleteYN);
                taskList.add(task);
                adapterTaskDetail.notifyItemChanged(taskList.size()-1);
                maxID = maxID + 1;
                sqlClient.addTask(task);
                dialog.dismiss();
                Toast.makeText(getContext(), "Create Task Success!", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }
}