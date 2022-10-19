package org.nestedboxes.serviceImpl;

import main.org.box.nestedboxes.model.Box;
import org.box.nestedboxes.constants.Constants;
import org.box.nestedboxes.services.NestedBoxService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class NestedBoxServiceImpl implements NestedBoxService {

    public static final Logger LOGGER = Logger.getLogger(NestedBoxServiceImpl.class.getName());

    /**
     * purpose of the method is to retrieve all the data from a file
     * @param filepath : path to file where all the input details
     * @return get all the data into list of Box classes and return
     */
    @Override
    public List<Box> getBoxDetails(String filepath) {

        List<Box> boxList = new ArrayList<>();

        try (Stream<String> lines = Files.lines(Paths.get(filepath))) {
            lines.filter(s -> !s.contains(Constants.NO_OTHER_BOXES)).forEach(line -> {
                String[] keyValuePair = line.split(Constants.SPLIT_MARK);
                String key = keyValuePair[0];
                String value = keyValuePair[1];
                if (value.contains(",")) {
                    List<String> valList = Arrays.asList(value.split(","));
                    Box box = new Box();
                    box.setName(key);
                    box.setChildBoxes(valList);
                    boxList.add(box);
                } else if (!value.contains(",")) {
                    List<String> valList = List.of(value);
                    Box box = new Box();
                    box.setName(key);
                    box.setChildBoxes(valList);
                    boxList.add(box);
                }
            });
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE,"getBoxDetails() : Error occurred while reading from input file ", e);
        }
        return boxList;
    }

    /**
     * purpose of the method is to get the immediately bigger boxes which contains shiny boxes directly as a child
     * @param filepath : path to file where all the input details
     * @return list of immediate bigger boxes.
     */
    @Override
    public List<String> getImmediateBiggerBoxes(String filepath, String box) {

        List<String> immediateBoxList = new ArrayList<>();

        try (Stream<String> lines = Files.lines(Paths.get(filepath))) {
            lines.filter(s -> !s.contains(Constants.NO_OTHER_BOXES)).forEach(line -> {
                String[] keyValuePair = line.split(Constants.SPLIT_MARK);
                String key = keyValuePair[0];
                String value = keyValuePair[1];
                if (value.contains(",")) {
                    List<String> valList = Arrays.asList(value.split(","));
                    valList.forEach(s -> {
                        if (s.contains(box)) {
                            immediateBoxList.add(key);
                        }
                    });
                } else if (!value.contains(",")) {
                    List<String> valList = List.of(value);
                    valList.forEach(s -> {
                        if (s.contains(box)) {
                            immediateBoxList.add(key);
                        }
                    });
                }
            });
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE,"getImmediateBiggerBoxes() : Error occurred while reading from input file ", e);
        }
        return immediateBoxList;
    }


    /**
     * get the bigger list of the given box
     * @param biggerList
     * @param boxDetails
     * @param boxName
     * @return
     */
    @Override
    public Set<String> getBiggerList(Set<String> biggerList, List<Box> boxDetails, String boxName) {
        AtomicReference<String> tempBigBox = new AtomicReference<>("");
        boxDetails.forEach(s -> {
            s.getChildBoxes().forEach(s1 -> {
                if (s1.contains(boxName)) {
                    biggerList.add(s.getName());
                    tempBigBox.set(s.getName());
                    getBiggerList(biggerList, boxDetails, String.valueOf(tempBigBox));
                }
            });
        });
        return biggerList;
    }

    /**
     * count the smaller box count which can contain by given box
     * @param boxList
     * @param smallestBoxList : boxes which cannot contain other boxes
     * @param startBox : given box
     * @return
     * @throws IOException
     */
    public long countSmallBoxes(List<Box> boxList, List<String> smallestBoxList, String startBox) throws IOException {

        if (startBox.contains("box") && !startBox.contains("boxes")) {
            startBox = startBox.replace("box", "boxes");
        }
        String[] shinyChild = startBox.split(" ");
        AtomicLong count = new AtomicLong();


        String shinyChildKey = shinyChild[2] + " " + shinyChild[3] + " " + shinyChild[4].replace(".", "");

        try {
            String shinyChildValue = shinyChild[2] + " " + shinyChild[3];
            if (!smallestBoxList.contains(shinyChildValue)) {
                boxList.forEach(box -> {
                    if (box.getName().contains(shinyChildKey)) {
                        box.getChildBoxes().forEach(s -> {
                            String[] a = s.split(" ");
                            String g = a[2] + " " + a[3];
                            if (smallestBoxList.contains(g)) {
                                //   System.out.println(g);
                                count.set(count.get() + Long.parseLong(a[1]));

                            } else if (!smallestBoxList.contains(g)) {
                                try {
                                    count.set(count.get() + (countSmallBoxes(boxList, smallestBoxList, s) * Long.parseLong(a[1]) + Long.parseLong(a[1])));
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        });
                    }
                });

            } else if (smallestBoxList.contains(shinyChildValue)) {
                count.set(count.get() + Long.parseLong(shinyChild[1]));
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE,"Error occurred while reading from input file ", e);
            e.printStackTrace();
        }
        return count.get();
    }

    /**
     * The smallest boxes which cannot contain another box
     * @param filepath
     * @return : list of the smallest boxes which cannot contain another box
     * @throws IOException
     */
    @Override
    public List<String> getSmallestBoxes(String filepath) throws IOException {
        List<String> smallestBoxList = new ArrayList<>();
        try (Stream<String> lines = Files.lines(Paths.get(filepath))) {
            lines.forEach(line -> {
                String[] keyValuePair = line.split("contain");
                String key = keyValuePair[0];
                String value = keyValuePair[1];
                if (!value.contains(",")) {
                    List<String> valList = List.of(value);
                    valList.forEach(s -> {
                        if (s.contains("no other boxes")) {
                            smallestBoxList.add(key.replace(" boxes", "").trim());
                        }
                    });
                }

            });
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE,"getSmallestBoxes() : Error occurred while reading from input file ", e);
        }
        return smallestBoxList;
    }
}
