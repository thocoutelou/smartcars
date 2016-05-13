package tests;

import java.util.PriorityQueue;

import org.junit.Test;

import smartCars.AbstractEvent;
import smartCars.GraphState;
import smartCars.SvgParser;

public class ResolutionTest {
	
	String project_location = SvgParser.getProjectLocation();

	@Test
	public void test() {
		
		//Parse du graph3
		String fileName=project_location + "/media/map/3.svg";
		GraphState graph3 = SvgParser.parseGraphState(fileName);
		
		graph3.calculatePaths();
		graph3.gatherEvents();
		
		System.out.println("\nActualisation de l'état du graphe :\n"+graph3.events+"\n\n");
		
		//Parse du graph4
		fileName=project_location + "/media/map/4.svg";
		GraphState graph4 = SvgParser.parseGraphState(fileName);
		
		graph4.calculatePaths();
		graph4.gatherEvents();
		
		System.out.println("\nActualisation de l'état du graphe :\n"+graph4.events+"\n\n");
		
		System.out.println("Vérification de la chronologie de ces évènements (dates factices) :");
		PriorityQueue<AbstractEvent> events = new PriorityQueue<AbstractEvent>(new AbstractEvent.EventComparator());
		for(AbstractEvent event : graph4.events)
		{
			events.add(event);
		}
		AbstractEvent event;
		while(!events.isEmpty())
		{
			event = events.poll();
			System.out.println(event+" se réaliserait à t="+event.date);
		}
	}

}
