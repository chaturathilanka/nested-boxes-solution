package main;

import main.org.box.nestedboxes.model.Box;
import main.org.nestedboxes.util.CollectionManipulationUtil;
import org.box.nestedboxes.constants.Constants;
import org.box.nestedboxes.services.NestedBoxService;
import org.nestedboxes.serviceImpl.NestedBoxServiceImpl;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

public class Main {
    public static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws IOException {

        Properties props = new Properties();
        props.load(new FileInputStream("src/main/resources/config.properties"));
        NestedBoxService nestedBoxService = new NestedBoxServiceImpl();
        String filepath = props.getProperty("boxes.filepath");

        List<Box> boxList = nestedBoxService.getBoxDetails(filepath);

        List<String> immediateBigList = nestedBoxService.getImmediateBiggerBoxes(filepath, Constants.SHINY_GOLD_VALUE);
        Set<String> listBiggerThanShinyBox = new HashSet<>();
        listBiggerThanShinyBox.addAll(immediateBigList);
        immediateBigList.forEach(s -> {
            s = s.split(" ")[0] + " " + s.split(" ")[1];
            nestedBoxService.getBiggerList(listBiggerThanShinyBox, boxList, s);
        });
        System.out.println(listBiggerThanShinyBox);

        System.out.println(boxList);

        CollectionManipulationUtil manipulationUtil = new CollectionManipulationUtil();
        manipulationUtil.removeBiggerBoxes(boxList, listBiggerThanShinyBox);


        List<String> smallestBoxesList = nestedBoxService.getSmallestBoxes(filepath);

        List<String> shinyGoldChildren = new ArrayList<>();

        boxList.forEach(box -> {
            if (box.getName().contains("shiny gold boxes")) {
                shinyGoldChildren.addAll(box.getChildBoxes());
            }
        });

        AtomicLong count1 = new AtomicLong();
        shinyGoldChildren.forEach(s -> {
            String[] a = s.split(" ");
            s = s.replace(".", "");
            try {
                count1.set(count1.get() + (Long.parseLong(a[1]) * nestedBoxService.countSmallBoxes(boxList, smallestBoxesList, s) + Long.parseLong(a[1])));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        logger.info("Question 1 Answer : " + listBiggerThanShinyBox.size());
        logger.info("Question 2 Answer : " + count1);
    }
}