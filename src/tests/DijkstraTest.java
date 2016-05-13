package tests;

import org.junit.Test;

import junit.framework.TestCase;
import smartCars.AbstractVehicle;
import smartCars.GraphState;
import smartCars.SvgParser;
import smartCars.Dijkstra;

public class DijkstraTest extends TestCase{
	
	String project_location = SvgParser.getProjectLocation();
	
	@Test
	public void test() {
		
		//Parse du graph3
		String fileName=project_location + "/media/map/3.svg";
		GraphState graph3 = SvgParser.parseGraphState(fileName);
		System.out.println(graph3);
		
		AbstractVehicle vehicleTest = graph3.vehicles.get(0);
		Dijkstra.calculatePath(graph3, vehicleTest);
		vehicleTest.printPath();
		
		//Parse du graph4
		fileName=project_location + "/media/map/4.svg";
		GraphState graph4 = SvgParser.parseGraphState(fileName);
		System.out.println(graph4);
		for (int i=0; i<2; i++){
			vehicleTest = graph4.vehicles.get(i);
			Dijkstra.calculatePath(graph4, vehicleTest);
			vehicleTest.printPath();
		}
	}

}
