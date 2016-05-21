package tests;

import java.io.FileNotFoundException;

import org.junit.Test;

import problem.GraphState;
import problem.SvgParser;
import resources.Time;

public class ResolutionTest {
	
	String project_location = SvgParser.getProjectLocation();

	@Test
	public void test() {
		
		GraphState graph4 = GraphState.parse(4);
		graph4.resolve();
		graph4.setCurrentLocations(80.);
		
	}

}
