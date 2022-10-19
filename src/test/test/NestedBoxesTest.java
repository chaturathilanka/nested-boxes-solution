package test;


import org.box.nestedboxes.constants.Constants;
import org.box.nestedboxes.services.NestedBoxService;
import org.junit.*;
import org.nestedboxes.serviceImpl.NestedBoxServiceImpl;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class NestedBoxesTest {

    private static NestedBoxService nestedBoxesService;

    @BeforeClass
    public static void initCalculator() {
        nestedBoxesService = new NestedBoxServiceImpl();
    }

    @Before
    public void beforeEachTest() {
        System.out.println("New test case is starting now");

    }

    @After
    public void afterEachTest() {
        System.out.println("Test case is ended");
    }

    @Test
    public void checkTheFilePathIsCorrectInThePropertyFile() throws IOException {

        Properties props = new Properties();
        props.load(new FileInputStream("src/main/resources/config.properties"));
        NestedBoxService nestedBoxService = new NestedBoxServiceImpl();
        String filepath = props.getProperty("boxes.filepath");

        Assert.assertEquals("C:/Users/LENOVO/IdeaProjects/nested-boxes-solution/input-files/input-test.txt", filepath);
    }

    @Test
    public void checkAllTheLinesAreReadFromTheInputFile() throws IOException {

        String filepath = "C:/Users/LENOVO/IdeaProjects/nested-boxes-solution/input-files/input-test.txt";
        List<main.org.box.nestedboxes.model.Box> boxList = nestedBoxesService.getBoxDetails(filepath);
        Assert.assertEquals("number of lines = ", 8, boxList.size());

    }

    @Test
    public void getImmediateBiggerBoxesToAGivenListAndBoxName() {

        String filepath = "C:/Users/LENOVO/IdeaProjects/nested-boxes-solution/input-files/input-test.txt";
        String shinyKey = Constants.SHINY_GOLD_VALUE;
        List<String> list = nestedBoxesService.getImmediateBiggerBoxes(filepath, shinyKey);
        Assert.assertEquals("number of immediate bigger boxes = ", 2, list.size());

    }

    @Test
    public void getSmallestBoxesInGivenFile() throws IOException {

        String filepath = "C:/Users/LENOVO/IdeaProjects/nested-boxes-solution/input-files/input-test.txt";
        List<String> smallestBoxes = nestedBoxesService.getSmallestBoxes(filepath);
        Assert.assertEquals("number of smallest boxes = ", 2, smallestBoxes.size());

    }

}
