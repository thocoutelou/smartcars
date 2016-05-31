package tests;

import org.junit.Test;

import graph.Graph;
import problem.GraphState;
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
        }

        File file = new File(exemple_location+"3.svg");
        graph = SvgParser.parseGraphState(file);
        GraphState graphState = (GraphState) graph;
        graphState.calculatePaths();
        graphState.gatherEvents();
        File outputFolder=new File(output_location+file.getName()+"_dir/");
        outputFolder.mkdirs();
        graphState.video(1, outputFolder, file.getName());
    }
}
