package tests;


import java.io.File;
import java.io.IOException;

import org.junit.Test;

import junit.framework.TestCase;
import smartCars.AbstractVehicle;
import smartCars.GraphState;
import smartCars.SvgParser;
import smartCars.Dijkstra;

public class DijkstraTest extends TestCase{
	
	String project_location;
	
	@Test
	public void test() {
		try {
			project_location = new File(".").getCanonicalPath();
		} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}
		
		//Parse du graph3
		String fileName=project_location + "/media/map/3.svg";
		GraphState graph3 = SvgParser.parseGraphState(fileName);
		System.out.println(graph3);
		
		AbstractVehicle vehicleTest = graph3.vehicles.get(0);
		Dijkstra.path(graph3, vehicleTest);
		vehicleTest.printPath();
		
		//Parse du graph4
		fileName=project_location + "/media/map/4.svg";
		GraphState graph4 = SvgParser.parseGraphState(fileName);
		System.out.println(graph3);
		for (int i=0; i<2; i++){
			vehicleTest = graph4.vehicles.get(i);
			Dijkstra.path(graph4, vehicleTest);
			vehicleTest.printPath();
		}
	}

}
