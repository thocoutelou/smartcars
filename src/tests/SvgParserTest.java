package tests;

import java.io.File;

import org.junit.Test;

import graph.Graph;
import problem.SvgParser;

public class SvgParserTest {

	String project_location = SvgParser.getProjectLocation();
	String exemple_location = project_location+"/media/exemple/";
	
	@Test
	public void test() {

		File folder = new File(exemple_location);
		File[] fileList = folder.listFiles();
		Graph graph;

		for(File file : fileList){
			System.out.println(file.getName());
			graph = SvgParser.parseGraphState(file);
			System.out.println(graph);
		}
		
	}

}
