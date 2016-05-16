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

        String fileName;
        String outputName;

        for(int i=1; i<=4;i++){
            fileName=project_location + "/media/map/" + i + ".svg";
            outputName= project_location + "/media/map/" + i + "_output.svg";
            Graph graph = SvgParser.parseGraphState(fileName);
            SvgGenerator sg = new SvgGenerator(graph, outputName);
        }


    }
}
