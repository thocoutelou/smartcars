package tests;

import org.junit.Test;
import java.io.File;
import java.io.IOException;

import smartCars.AbstractEvent;
import smartCars.AbstractVehicle;
import smartCars.Dijkstra;
import smartCars.GraphState;
import smartCars.SvgParser;

public class EventsTest {
	
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
		
		AbstractVehicle vehicleTest = graph3.vehicles.get(0);
		Dijkstra.path(graph3, vehicleTest);
		AbstractEvent.vehicleEvents(vehicleTest);
		System.out.println(vehicleTest.events);
	}

}
