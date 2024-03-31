package org.example;

public class Calculations implements Runnable{

    private final TaskQueue taskQueue;
    private final ResultQueue resultQueue;
    public Calculations(TaskQueue t, ResultQueue r) {
        taskQueue = t;
        resultQueue = r;
    }

    @Override
    public void run() {
        while (true) {
            try {
                String[] task = taskQueue.getTask().split(" ");

                int id = Integer.parseInt(task[0]);
                double number = Integer.parseInt(task[1]);

                double result = 0;
                for (int n = 1; n <= number; n++) {
                    result += (Math.pow(-1, n - 1)) / (2 * n - 1);
                    resultQueue.addResult(id, result * 4.0, n / number * 100.0);
                    Thread.sleep(5);
                    resultQueue.removeResult(id);
                }

                System.out.print("Przybliżona wartość pi = " + result + ". Procent wykonania: " + 100.0 + "%.\n");
                resultQueue.addResult(id, result * 4.0, 100.0);

            } catch (InterruptedException e) {
                break;
            }
        }
    }


}
