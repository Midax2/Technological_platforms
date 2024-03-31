package org.example;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static EntityManagerFactory entities = Persistence.createEntityManagerFactory("jpa-hibernate-example");

    public static void main(String[] args) {
        EntityManager entityManager = entities.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();

        entityTransaction.begin();

        Tower t1 = new Tower("Tower_1",20);
        Tower t2 = new Tower("Tower_2",10);
        Mage m1 = new Mage("Mage_1", 150, t1);
        t1.getMages().add(m1);
        Mage m2 = new Mage("Mage_2", 250, t2);
        t2.getMages().add(m2);
        Mage m3 = new Mage("Mage_3", 500, t1);
        t1.getMages().add(m3);

        entityManager.persist(t1);
        entityManager.persist(t2);
        entityManager.persist(m1);
        entityManager.persist(m2);
        entityManager.persist(m3);

        entityTransaction.commit();


        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("MENU\nm - dodaj maga\nt - dodaj wieżę\ns - pokaz wszystkich magów\n" +
                    "w - pokazywanie wszystkich wież\nd - usuń maga\n" +
                    "r - usuń wieże\nu - zapytanie\nq - wyjdź");
            String input = scanner.nextLine();
            switch (input) {
                case "m":
                    entityTransaction.begin();
                    System.out.println("Podaj imię maga");
                    String tempName = scanner.nextLine();
                    System.out.println("Podaj poziom maga");
                    int level = Integer.parseInt(scanner.nextLine());
                    System.out.println("Podaj nazwę wieży maga");
                    String tempTower = scanner.nextLine();
                    Tower found = entityManager.find(Tower.class, tempTower);
                    if (found != null) {
                        Mage newMage = new Mage(tempName, level, found);
                        entityManager.persist(newMage);
                        System.out.println("Poprawnie dodano maga: " + tempName);
                        found.getMages().add(newMage);
                    }
                    else {
                        System.out.println("Nie istnieje takiej wieży");
                    }
                    entityTransaction.commit();
                    break;
                case "t":
                    entityTransaction.begin();
                    System.out.println("Podaj nazwę wieży");
                    String tempNameTower = scanner.nextLine();
                    System.out.println("Podaj wysokość wieży");
                    int height = Integer.parseInt(scanner.nextLine());
                    Tower newTower = new Tower(tempNameTower,height);
                    entityManager.persist(newTower);
                    entityTransaction.commit();
                    break;
                case "d":
                    entityTransaction.begin();
                    System.out.println("Podaj imię maga do usunięcia");
                    String nameMageToDel = scanner.nextLine();
                    Mage mageToDel = entityManager.find(Mage.class, nameMageToDel);
                    if (mageToDel != null) {
                        entityManager.remove(mageToDel);
                    }
                    else {
                        System.out.println("Nie istnieje takiego maga");
                    }
                    entityTransaction.commit();
                    break;
                case "r":
                    entityTransaction.begin();
                    System.out.println("Podaj nazwę wieży do usunięcia");
                    String nameTowerToDel = scanner.nextLine();
                    Tower towerToDel = entityManager.find(Tower.class, nameTowerToDel);
                    if (towerToDel != null) {
                        entityManager.remove(towerToDel);
                    }
                    else {
                        System.out.println("Nie istnieje takiej wieży");
                    }
                    entityTransaction.commit();
                    break;
                case "s":
                    List<Mage> mages = entityManager.createQuery("SELECT m FROM Mage m", Mage.class).getResultList();
                    for (Mage mage: mages) {
                        System.out.println("Mage: " + mage.getName() + " Level: " + mage.getLevel() + " Tower: " + mage.getTower().getName());
                    }
                    break;
                case "w":
                    List<Tower> towers = entityManager.createQuery("SELECT t FROM Tower t", Tower.class).getResultList();
                    for (Tower tower: towers) {
                        System.out.println("Tower: " + tower.getName() + " Height: " + tower.getHeight() + " Mages number: " + tower.getMages().size());
                    }
                    break;
                case "u":
                    List<Mage> magesSpec = entityManager.createQuery("SELECT m FROM Mage m WHERE level > 200" +
                            "AND m.tower.Tname = 'Tower_2'", Mage.class).getResultList();
                    for (Mage mage: magesSpec) {
                        System.out.println("Mage: " + mage.getName() + " Level: " + mage.getLevel() + " Tower: " + mage.getTower().getName());
                    }
                    break;
            }
            if (input.equals("q")) {
                break;
            }
        }
        scanner.close();
        entityManager.close();
        entities.close();
    }
}
