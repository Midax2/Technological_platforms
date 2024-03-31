package main.java;

import java.util.*;

public class Mage implements Comparable {
    private String name;
    private int level;
    private double power;
    private Set<Mage> apprentices;


    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public double getPower() {
        return power;
    }

    public Mage(String name, int level, double power, String sort) {
        this.name = name;
        this.level = level;
        this.power = power;
        if (Objects.equals(sort, "none")) this.apprentices = new HashSet<>();
        else if (Objects.equals(sort, "natural")) this.apprentices = new TreeSet<>();
        else this.apprentices = new TreeSet<>(new ComparatorMage());
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mage mage = (Mage) o;
        return level == mage.level && Double.compare(power, mage.power) == 0 && Objects.equals(name, mage.name) && Objects.equals(apprentices, mage.apprentices);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, level, power, apprentices);
    }

    @Override
    public int compareTo(Object o) {

        if (o == null){
            throw new NullPointerException("isNull");
        }
        if (o.getClass() != Mage.class){
            throw new ClassCastException("notSameClass");
        }

        Mage mage = (Mage)o;

        if(this.name.compareTo(mage.name) != 0) {
            return this.name.compareTo(mage.name);
        }
        if(this.level != mage.level){
            return Integer.compare(this.level,mage.level);
        }
        return Double.compare(this.power,mage.power);


    }

    @Override
    public String toString() {
        return "Mage{" + "name='" + name + '\'' + ", level=" + level + ", power=" + power + '}';
    }


    public void Add(Mage mage){
        this.apprentices.add(mage);
    }

    public void wypisz(String lvl){
        System.out.println(lvl + " " + this);
        int i = 1;
        for (Mage son : this.apprentices){
            son.wypisz("\t" + lvl + i + ".");
            i += 1;
        }
    }

    public int countGen(){
        int count = 0;
        for (Mage son : this.apprentices){
            count += 1;
            count += son.countGen();
        }
        return count;
    }
}