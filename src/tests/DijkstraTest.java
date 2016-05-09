package tests;


import java.io.File;
import java.io.IOException;
import java.util.Stack;

import org.junit.After;
import org.junit.Test;

import junit.framework.TestCase;
import smartCars.AbstractVehicle;
import smartCars.GraphState;
import smartCars.Road;
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
		
		//Parse d'un GraphState
		String fileName=project_location + "/media/map/3.svg";
		GraphState graph3 = SvgParser.parseGraphState(fileName);
		System.out.println(graph3);
		
		AbstractVehicle vehicleTest = graph3.vehicles.get(0);
		Dijkstra.path(graph3, vehicleTest);
		printPath(vehicleTest.getPath());
		
	}

	private void printPath(Stack<Road> path)
	{
		for(Road r : path)
		{
			System.out.println("Road "+r.identifier);
		}
	}

}
