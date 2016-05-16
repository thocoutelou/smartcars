package tests;

import org.junit.Test;
import smartCars.Graph;
import smartCars.SvgGenerator;
import smartCars.SvgParser;

import java.io.File;
import java.io.IOException;

public class SvgGeneratorTest {

    String project_location = SvgParser.getProjectLocation();
    String exemple_location = project_location+"/media/exemple/";
    String output_location = project_location+"/media/output/";

    @Test
    public void test() {

        /*String fileName;
        String outputName;

        for(int i=1; i<=4;i++){
            fileName=project_location + "/media/map/" + i + ".svg";
            outputName= project_location + "/media/map/" + i + "_output.svg";
            Graph graph = SvgParser.parseGraphState(fileName);
            SvgGenerator sg = new SvgGenerator(graph, outputName);
        }*/
        File folder = new File(exemple_location);
        File[] exempleList = folder.listFiles();
        Graph graph;

        for(File file : exempleList){
            System.out.println(file.getName());
            graph = SvgParser.parseGraphState(file);
            System.out.println(graph);
            File outputFile= new File(output_location+file.getName());
            SvgGenerator sg = new SvgGenerator(graph, outputFile);
        }


    }
}
