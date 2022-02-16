package com.example.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

public class Task implements Parcelable {
    int taskID;
    String taskName;
    int taskCompleteYN;

    public Task(int taskID, String taskName, int taskCompleteYN) {
        this.taskID = taskID;
        this.taskName = taskName;
        this.taskCompleteYN = taskCompleteYN;
    }

    protected Task(Parcel in) {
        taskID = in.readInt();
        taskName = in.readString();
        taskCompleteYN = in.readInt();
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public int getTaskCompleteYN() {
        return taskCompleteYN;
    }

    public void setTaskCompleteYN(int taskCompleteYN) {
        this.taskCompleteYN = taskCompleteYN;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(taskID);
        parcel.writeString(taskName);
        parcel.writeInt(taskCompleteYN);
    }

    @Override
    public boolean equals(Object o) {
        try {
            Task task = (Task) o;
            return (task.getTaskID() == this.taskID);
        } catch (Exception e) {
            return super.equals(o);
        }
    }

}
