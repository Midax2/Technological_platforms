package org.example;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
public class Tower {
    @Id
    private String Tname;
    private int height;
    @OneToMany(mappedBy = "tower", fetch = FetchType.EAGER,
            cascade = CascadeType.REMOVE)
    private List<Mage> mages;

    public Tower(String name, int height) {
        this.Tname = name;
        this.height = height;
        this.mages = new ArrayList<>();
    }

    public String getName() {
        return Tname;
    }

    public int getHeight() {
        return height;
    }

    public List<Mage> getMages() {
        return mages;
    }

    public void setName(String name) {
        this.Tname = name;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setMages(List<Mage> mages) {
        this.mages = mages;
    }
}