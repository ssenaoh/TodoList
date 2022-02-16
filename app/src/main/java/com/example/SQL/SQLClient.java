package com.example.SQL;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.Model.Task;

import java.util.ArrayList;
import java.util.List;

public class SQLClient extends SQLiteOpenHelper {
    private final static String DB_NAME = "Task.db";
    private final static String DB_TABLE_TASK = "Task";
    private final static int DB_VERSION = 1;

    private static final String DB_TASK_COMPLETE_YN = "task_complete_yn";
    private static final String DB_TASK_NAME = "task_name";
    private static final String DB_TASK_ID = "task_id";

    public SQLClient(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String strQuery = String.format("CREATE TABLE %s (%s TEXT, %s INTEGER, %s INTEGER)", DB_TABLE_TASK, DB_TASK_NAME, DB_TASK_COMPLETE_YN, DB_TASK_ID);
        sqLiteDatabase.execSQL(strQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        if(i1 != i){
            sqLiteDatabase.execSQL(String.format("DROP TABLE IF EXISTS %s", DB_NAME));
            onCreate(sqLiteDatabase);
        }
    }

    public void addTask(Task task){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DB_TASK_NAME, task.getTaskName());
        contentValues.put(DB_TASK_COMPLETE_YN, task.getTaskCompleteYN());
        contentValues.put(DB_TASK_ID, task.getTaskID());
        sqLiteDatabase.insert(DB_TABLE_TASK, null, contentValues);
    }

    public void updateTask(Task task){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DB_TASK_NAME, task.getTaskName());
        contentValues.put(DB_TASK_COMPLETE_YN, task.getTaskCompleteYN());
        contentValues.put(DB_TASK_ID, task.getTaskID());
        sqLiteDatabase.update(DB_TABLE_TASK, contentValues, String.format("%s = ?", DB_TASK_ID), new String[]{String.valueOf(task.getTaskID())});
    }

    public List<Task> getAllTask(){
        List<Task> taskList = new ArrayList<>();
        Task task;

        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(false, DB_TABLE_TASK,
                null,null, null, null, null, null, null, null);

        while (cursor.moveToNext()){
            @SuppressLint("Range") String taskName = cursor.getString(cursor.getColumnIndex(DB_TASK_NAME));
            @SuppressLint("Range") int taskCompleteYN = cursor.getInt(cursor.getColumnIndex(DB_TASK_COMPLETE_YN));
            @SuppressLint("Range") int taskID = cursor.getInt(cursor.getColumnIndex(DB_TASK_ID));
            taskList.add(new Task(taskID, taskName, taskCompleteYN));
        }
        return taskList;
    }

    public void deleteAllTask(){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        sqLiteDatabase.delete(DB_NAME, null, null );
    }
}
