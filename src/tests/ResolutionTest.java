package tests;

import org.junit.Test;

import problem.GraphState;

public class ResolutionTest {
	
	@Test
	public void test() {
		
		GraphState graph4 = GraphState.parse(4);
		graph4.resolve();
		graph4.setCurrentLocations(80.);
		
	}

}
