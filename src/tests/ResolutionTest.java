package tests;

import org.junit.Test;

import smartCars.GraphState;
import smartCars.SvgParser;

public class ResolutionTest {
	
	String project_location = SvgParser.getProjectLocation();

	@Test
	public void test() {
		
		//Parse du graph4
		String fileName=project_location + "/media/exemple/4.svg";
		GraphState graph4 = SvgParser.parseGraphState(fileName);
		
		graph4.resolve();
		
	}

}
