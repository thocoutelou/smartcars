package tests;

import org.junit.Test;
import smartCars.Graph;
import smartCars.SvgGenerator;
import smartCars.SvgParser;

import java.io.File;
import java.io.IOException;

public class SvgGeneratorTest {

    String project_location;

    @Test
    public void test() {
        try {
            project_location = new File(".").getCanonicalPath();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //Scenario 1 : création d'un graphe simple à partir d'une image svg
        String fileName=project_location + "/media/map/1.svg";
        Graph graph1 = SvgParser.parseGraphState(fileName);
        System.out.println(graph1);
        SvgGenerator sg = new SvgGenerator(graph1);
    }
}
