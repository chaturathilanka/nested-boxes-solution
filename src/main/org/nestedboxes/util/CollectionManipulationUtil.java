package main.org.nestedboxes.util;

import main.org.box.nestedboxes.model.Box;

import java.util.List;
import java.util.Set;

public class CollectionManipulationUtil {

    public void removeBiggerBoxes(List<Box> bigger, Set<String> listToRemove) {

            for (int i = 0; i < bigger.size(); i++) {
                if(listToRemove.contains(bigger.get(i).getName())){
                    bigger.remove(bigger.get(i));
                    removeBiggerBoxes(bigger, listToRemove);
                }
            }
        }
}
