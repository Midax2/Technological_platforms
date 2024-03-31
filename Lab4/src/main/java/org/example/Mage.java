package org.example;

import javax.persistence.*;


@Entity
public class Mage {
    @Id
    private String Mname;
    private int level;
    @ManyToOne
    @JoinColumn(name = "Tname")
    private Tower tower;

    public Mage(String name, int level, Tower tower) {
        this.Mname = name;
        this.level = level;
        this.tower = tower;
    }

    public String getName() {
        return Mname;
    }

    public int getLevel() {
        return level;
    }

    public Tower getTower() {
        return tower;
    }

    public void setName(String name) {
        this.Mname = name;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setTower(Tower tower) {
        this.tower = tower;
    }
}
