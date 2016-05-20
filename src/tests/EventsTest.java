package tests;

import org.junit.Test;

import events.AbstractEvent;
import graph.Dijkstra;
import problem.GraphState;
import problem.SvgParser;
import smartcars.AbstractVehicle;

public class EventsTest {
	
	String project_location = SvgParser.getProjectLocation();

	@Test
	public void test() {
		
		//Parse d'un GraphState
		String fileName=project_location + "/media/exemple/3.svg";
		GraphState graph3 = SvgParser.parseGraphState(fileName);
		System.out.println(graph3);
		
		AbstractVehicle vehicleTest = graph3.getVehicles().get(0);
		Dijkstra.calculatePath(graph3, vehicleTest);
		AbstractEvent.vehicleEvents(vehicleTest);
		
	}
	
}
