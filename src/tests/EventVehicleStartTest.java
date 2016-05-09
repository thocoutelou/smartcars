package tests;


import java.io.File;
import java.io.IOException;

import org.junit.Test;

import smartCars.AbstractVehicle;
import smartCars.EventVehicleStart;
import smartCars.GraphState;
import smartCars.SvgParser;

public class EventVehicleStartTest {

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
		new EventVehicleStart(vehicleTest);
		System.out.println(vehicleTest.events);
	}

}
