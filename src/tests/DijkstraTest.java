package tests;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import smartCars.GraphState;
import smartCars.SvgParser;
import smartCars.Dijkstra;

public class DijkstraTest {
	
	String project_location;
	@Test
	public void test() {
		try {
			project_location = new File(".").getCanonicalPath();
		} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}
		
		//Parse d'un GraphState
		String fileName=project_location + "/media/map/3.svg";
		GraphState graph3 = SvgParser.parseGraphState(fileName);
		System.out.println(graph3);
		
		Dijkstra.path(graph3, graph3.vehicles.get(0));
		
		return;
	}

}
