package tests;

import java.util.PriorityQueue;

import org.junit.Test;

import smartCars.AbstractEvent;
import smartCars.GraphState;
import smartCars.SvgParser;
import smartCars.Time;

public class DatesTest {
	
	String project_location = SvgParser.getProjectLocation();

	@Test
	public void test() {
		
		//Parse du graph4
		String fileName=project_location + "/media/map/3.svg";
		GraphState graph3 = SvgParser.parseGraphState(fileName);
		
		graph3.calculatePaths();
		graph3.gatherEvents();

		AbstractEvent event;
		System.out.println("Vérification de la chronologie de ces évènements (dates factices) :");
		PriorityQueue<AbstractEvent> events = graph3.vehicles.get(0).getEventsCopy();
		while(!events.isEmpty())
		{
			event = events.remove();
			System.out.println(event+" se réaliserait à t="+event.date);
		}
		
		Time.realDates(graph3);
		
		System.out.println("\nVérification de la chronologie de ces évènements (dates réelles) :");
		for(AbstractEvent e : graph3.events)
		{
			events.add(e);
		}
		while(!events.isEmpty())
		{
			event = events.remove();
			System.out.println(event+" se réalise à t="+event.date);
		}
	}

}
