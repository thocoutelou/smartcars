package tests;

import org.junit.Test;

import graph.Dijkstra;
import junit.framework.TestCase;
import problem.GraphState;
import problem.SvgParser;
import smartcars.AbstractVehicle;

public class DijkstraTest extends TestCase{
	
	String project_location = SvgParser.getProjectLocation();
	
	@Test
	public void test() {
		
		//Parse du graph3
		String fileName=project_location + "/media/exemple/3.svg";
		GraphState graph3 = SvgParser.parseGraphState(fileName);
		System.out.println(graph3);
		
		AbstractVehicle vehicleTest = graph3.getVehicles().get(0);
		Dijkstra.calculatePath(graph3, vehicleTest);
		vehicleTest.printPath();
		
		//Parse du graph4
		fileName=project_location + "/media/exemple/4.svg";
		GraphState graph4 = SvgParser.parseGraphState(fileName);
		System.out.println(graph4);
		for (int i=0; i<graph4.getVehicles().size(); i++){
			vehicleTest = graph4.getVehicles().get(i);
			Dijkstra.calculatePath(graph4, vehicleTest);
			vehicleTest.printPath();
		}
	}

}
