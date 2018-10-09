package com.yh.wx.entity;

import java.util.*;

public class Monitor {
    private static Monitor monitor;
    private Map<String, Task> tasks = new HashMap<String, Task>();

    private Monitor() {

    }

    public static Monitor get() {
        if(monitor == null) {
            monitor = new Monitor();
        }

        return monitor;
    }

    public void addTask(String key, Task task) {
        tasks.put(key, task);
    }

    public void removeTask(String key) {
        tasks.remove(key);
    }

    public Task getTask(String key) {
        return tasks.get(key);
    }

    public Set<String> getMonitor() {
        return tasks.keySet();
    }
}
