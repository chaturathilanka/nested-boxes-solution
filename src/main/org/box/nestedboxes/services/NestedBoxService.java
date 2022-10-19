package org.box.nestedboxes.services;

import main.org.box.nestedboxes.model.Box;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface NestedBoxService {

    List<Box> getBoxDetails(String filepath);

    Set<String> getBiggerList(Set<String> biggerList, List<Box> boxDetailsMap, String boxName);

    List<String> getImmediateBiggerBoxes(String filepath, String box);

    long countSmallBoxes(List<Box> boxList, List<String> smallestBosList, String startBox) throws IOException;

    List<String> getSmallestBoxes(String filepath) throws IOException;

}
