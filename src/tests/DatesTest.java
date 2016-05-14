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
		String fileName=project_location + "/media/map/4.svg";
		GraphState graph4 = SvgParser.parseGraphState(fileName);
		
		graph4.calculatePaths();
		graph4.gatherEvents();
		
		Time.realDates(graph4);
		
		System.out.println("Vérification de la chronologie de ces évènements (dates réelles) :");
		PriorityQueue<AbstractEvent> events = new PriorityQueue<AbstractEvent>(new AbstractEvent.EventComparator());
		for(AbstractEvent event : graph4.events)
		{
			events.add(event);
		}
		AbstractEvent event;
		while(!events.isEmpty())
		{
			event = events.remove();
			System.out.println(event+" se réalise à t="+event.date);
		}
	}

}
