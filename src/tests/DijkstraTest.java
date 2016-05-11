package tests;


import java.io.File;
import java.io.IOException;
import java.util.Stack;

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
		
		//Parse du graph3
		String fileName=project_location + "/media/map/3.svg";
		GraphState graph3 = SvgParser.parseGraphState(fileName);
		System.out.println(graph3);
		
		AbstractVehicle vehicleTest = graph3.vehicles.get(0);
		Dijkstra.path(graph3, vehicleTest);
		printPath(vehicleTest);
		
		//Parse du graph4
		fileName=project_location + "/media/map/4.svg";
		GraphState graph4 = SvgParser.parseGraphState(fileName);
		System.out.println(graph3);
		for (int i=0; i<2; i++){
			vehicleTest = graph4.vehicles.get(i);
			Dijkstra.path(graph4, vehicleTest);
			printPath(vehicleTest);
		}
	}

	private void printPath(AbstractVehicle vehicle)
	{
		Stack<Road> path = vehicle.getPath();
		while(!path.isEmpty())
		{
			Road road = path.pop();
			if(road.equals(vehicle.location.finalRoad))
			{
				System.out.print("Road "+road.identifier+" -> ");
			}
			else
			{
				System.out.print("Road "+road.identifier+" -> "+"Intersection "+road.destination.identifier+"; ");
			}
		}
		System.out.println("end");
	}

}
