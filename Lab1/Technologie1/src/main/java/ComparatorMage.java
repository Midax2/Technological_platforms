package main.java;

import java.util.Comparator;

public class ComparatorMage implements Comparator<Mage> {


    @Override
    public int compare(Mage mage1, Mage mage2) {
        if (mage1 == null || mage2 == null){
            throw new NullPointerException("Error");
        }

        if(mage1.getLevel() != mage2.getLevel()){
            return Integer.compare(mage1.getLevel(),mage2.getLevel());
        }
        if(mage1.getName().compareTo(mage2.getName()) != 0) {
            return mage1.getName().compareTo(mage2.getName());
        }
        return Double.compare(mage1.getPower(),mage2.getPower());
    }
}
