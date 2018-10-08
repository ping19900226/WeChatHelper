package com.yh.wx.entity;

import java.util.*;

public class Monitor {
    private static Monitor monitor;
    private Map<String, List<Task>> tasks = new HashMap<String, List<Task>>();

    private Monitor() {

    }

    public static Monitor get() {
        if(monitor == null) {
            monitor = new Monitor();
        }

        return monitor;
    }

    public void addTask(String username, Task task) {
        List<Task> list = tasks.get(username);

        if(list == null) {
            throw new RuntimeException("Object " + username + " not monitor");
        }

        list.add(task);
    }

    public void removeTask(String username, String content) {
        List<Task> list = tasks.get(username);

        if(list == null) {
            return;
        }

        if(content == null) {
            tasks.remove(username);
        }

        Task target = null;

        for(Task t : list) {
            if(t.getContent().equals(content)) {
                target = t;
                break;
            }
        }

        list.remove(target);
    }

    public void monitor(String username) {
        if(tasks.get(username) != null) {
            return;
        }

        tasks.put(username, new ArrayList<Task>());
    }

    public List<Task> getTaskList(String username) {
        return tasks.get(username);
    }

    public Task getTask(String username, String content) {
        List<Task> list = tasks.get(username);

        if(list == null) {
            return null;
        }

        for(Task t : list) {
            if(t.getContent().equals(content)) {
                return t;
            }
        }

        return null;
    }

    public Task getTask(String username) {
        List<Task> list = tasks.get(username);

        if(list == null) {
            return null;
        }

        return list.get(0);
    }

    public Set<String> getMonitor() {
        return tasks.keySet();
    }
}
