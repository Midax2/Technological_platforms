package main.java;


import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        String args1 = args[0];
        Map<Mage, Integer> generations;
        Mage ojciec = null;
        ojciec = new Mage("Wizard",100,68,args1);
        if (Objects.equals(args1, "none")) generations = new HashMap<>();
        else generations = new TreeMap<>();
        for (int i = 0; i < 10; i++) {
            Mage temp = new Mage("mage"+i,10-i,20+i*2,args1);
            if (i == 4) {
                Mage temp1 = new Mage("mage"+i+"."+i,10+i,20+i,args1);
                temp.Add(temp1);
                generations.put(temp1, temp1.countGen());
            }
            generations.put(temp, temp.countGen());
            ojciec.Add(temp);
        }
        generations.put(ojciec, ojciec.countGen());
        ojciec.wypisz("1.");
        System.out.println(generations);
    }
}