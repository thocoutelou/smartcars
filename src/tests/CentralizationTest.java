package tests;

import org.junit.Test;

import smartCars.AbstractEvent;
import smartCars.GraphState;
import smartCars.PriorityQueue;
import smartCars.SvgParser;

public class CentralizationTest {
	
	String project_location = SvgParser.getProjectLocation();

	@Test
	public void test() {
		
		//Parse du graph3
		String fileName=project_location + "/media/exemple/3.svg";
		GraphState graph3 = SvgParser.parseGraphState(fileName);
		
		graph3.calculatePaths();
		graph3.gatherEvents();
		
		//Parse du graph4
		fileName=project_location + "/media/exemple/4.svg";
		GraphState graph4 = SvgParser.parseGraphState(fileName);
		
		graph4.calculatePaths();
		graph4.gatherEvents();
		
		System.out.println("Vérification de la chronologie de ces évènements (dates factices) :");
		PriorityQueue events = new PriorityQueue();
		events.qaddAll(graph4.events);
		AbstractEvent event;
		while(!events.qisEmpty())
		{
			event = events.qremove();
			System.out.println(event+" se réaliserait à t="+event.date);
		}
	}

}