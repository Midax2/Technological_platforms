package org.example;

import java.util.HashMap;
import java.util.Map;

public class ResultQueue {

    Map<Integer, String> resultQueue;

    public ResultQueue() {
        resultQueue = new HashMap<>();
    }

    public synchronized void addResult(int id, double result, double percent) {
        resultQueue.put(id, "Przybliżona wartość pi = " + result + ". Procent wykonania: " + percent + "%.\n");
    }
	
	public synchronized void removeResult(int id) {
        resultQueue.remove(id);
    }

    @Override
    public String toString() {
        return "ResultQueue{" +
                "resultQueue=" + resultQueue +
                '}';
    }
}
