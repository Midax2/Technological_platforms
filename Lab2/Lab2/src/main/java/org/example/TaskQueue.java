package org.example;

import java.util.LinkedList;
import java.util.Queue;

public class TaskQueue {

    private final Queue<String> taskQueue;

    public TaskQueue() {
        this.taskQueue = new LinkedList<>();
    }

    public synchronized void addTask(int id, int task) {
        this.taskQueue.add(id + " " + task);
        notify();
    }

    public synchronized String getTask() throws InterruptedException {
        while (taskQueue.isEmpty()) {
            wait();
        }
        return taskQueue.poll();
    }
}
