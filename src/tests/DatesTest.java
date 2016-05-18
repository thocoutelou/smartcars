package tests;

import org.junit.Test;

import smartCars.AbstractEvent;
import smartCars.GraphState;
import smartCars.PriorityQueue;
import smartCars.SvgParser;
import smartCars.Time;

public class DatesTest {
	
	String project_location = SvgParser.getProjectLocation();

	@Test
	public void test() {
		
		//Parse du graph4
		String fileName=project_location + "/media/exemple/3.svg";
		GraphState graph3 = SvgParser.parseGraphState(fileName);
		
		graph3.calculatePaths();
		graph3.gatherEvents();

		AbstractEvent event;
		System.out.println("Vérification de la chronologie de ces évènements (dates factices) :");
		PriorityQueue events = graph3.vehicles.get(0).getEventsCopy();
		while(!events.qisEmpty())
		{
			event = events.qremove();
			System.out.println(event+" se réaliserait à t="+event.date);
		}
		System.out.println("\n");
		
		Time.realDates(graph3);
		
		System.out.println("\nVérification de la chronologie de ces évènements (dates réelles) :");
		events.qaddAll(graph3.events);
		
		while(!events.qisEmpty())
		{
			event = events.qremove();
			System.out.println(event+" se réalise à t="+event.date);
		}
	}

}
