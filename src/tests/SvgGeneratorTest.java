package tests;

import org.junit.Test;

import graph.Graph;
import problem.SvgGenerator;
import problem.SvgParser;
import resources.Time;

import java.io.File;
import java.io.IOException;

public class SvgGeneratorTest {

    String project_location = SvgParser.getProjectLocation();
    String exemple_location = project_location+"/media/exemple/";
    String output_location = project_location+"/media/output/";

    @Test
    public void test() {

        File folder = new File(exemple_location);
        File[] exempleList = folder.listFiles();
        Graph graph;

        for(File file : exempleList){
            System.out.println(file.getName());
            graph = SvgParser.parseGraphState(file);
            System.out.println(graph);
            File outputFile0= new File(output_location+file.getName());
            SvgGenerator sg = new SvgGenerator(graph, outputFile0);

            // Modification de la date pour voir l'évolution des véhicules
            Time.time = 10;
            File outputFile10= new File(output_location+file.getName()+"10.svg");
            sg = new SvgGenerator(graph, outputFile10);
        }


    }
}
