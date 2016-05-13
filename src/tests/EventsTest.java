package tests;

import org.junit.Test;

import smartCars.AbstractEvent;
import smartCars.AbstractVehicle;
import smartCars.Dijkstra;
import smartCars.GraphState;
import smartCars.SvgParser;

public class EventsTest {
	
	String project_location = SvgParser.getProjectLocation();

	@Test
	public void test() {
		
		//Parse d'un GraphState
		String fileName=project_location + "/media/map/3.svg";
		GraphState graph3 = SvgParser.parseGraphState(fileName);
		System.out.println(graph3);
		
		AbstractVehicle vehicleTest = graph3.vehicles.get(0);
		Dijkstra.calculatePath(graph3, vehicleTest);
		AbstractEvent.vehicleEvents(vehicleTest);
	}

}
