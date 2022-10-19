package main.org.box.nestedboxes.model;

import java.util.List;

public class Box {

    private String name;
    private List<String> childBoxes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getChildBoxes() {
        return childBoxes;
    }

    public void setChildBoxes(List<String> childBoxes) {
        this.childBoxes = childBoxes;
    }
}
