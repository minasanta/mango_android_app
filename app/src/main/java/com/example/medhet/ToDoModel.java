package com.example.medhet;

public class ToDoModel {
    private int id;
    private int status;
    private String task;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public boolean getStatusBoolean()
    {
        if(status != 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }
}
