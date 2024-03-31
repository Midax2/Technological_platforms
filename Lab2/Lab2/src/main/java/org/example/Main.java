package org.example;

import java.util.Scanner;

public class Main {


    public static void main(String[] args) {
        int countCalc = Integer.parseInt(args[0]);

        Calculations[] calculations = new Calculations[countCalc];
        Thread[] threads = new Thread[countCalc];

        TaskQueue taskQueue = new TaskQueue();
        ResultQueue resultQueue = new ResultQueue();

        for (int i = 0; i < countCalc; i++) {
            calculations[i] = new Calculations(taskQueue, resultQueue);
            threads[i] = new Thread(calculations[i]);
            threads[i].start();
        }
		int idZadania = 1;
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("MENU\nPodaj liczbę do obliczeń\ns - pokaż wyniki\nq - pokaż wyniki i wyjdź");
            String input = scanner.nextLine();
            switch (input) {
                case "q":
                    System.out.println(resultQueue);
                    break;
                case "s":
                    System.out.println(resultQueue);
                    break;
                default:
                    try {
                        int number = Integer.parseInt(input);
                        taskQueue.addTask(idZadania, number);
						idZadania += 1;
                    } catch (NumberFormatException e) {
                        System.out.println("Błędny format numeru.");
                    }
            }
            if (input.equals("q")) {
                break;
            }
        }

        for (Thread thread : threads) {
            thread.interrupt();
        }
        scanner.close();
    }

}