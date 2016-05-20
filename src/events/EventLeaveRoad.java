package events;

import java.util.Stack;

import graph.Road;
import smartcars.AbstractVehicle;

public class EventLeaveRoad extends AbstractEvent {
	
	public EventLeaveRoad(AbstractVehicle vehicle, Road roadLeft, double date, AbstractEvent eventWOR)
	{
		super(vehicle, roadLeft, date);
		eventWaitingOnRoad = eventWOR;
		nature = 2;
	}
	
	public static synchronized void executeEvent(AbstractEvent event)
	{
		event.road.formerWaitingVehicle(event.getVehicle());
		event.road.getDestination().getVehiclesOnIntersection().add(event.getVehicle());
		event.getVehicle().getLocation().actualizeLocation(event.road, event.road.getLength(), event.getDate(), event.nature);
	}
	
	public static void nextEvent(AbstractEvent event, Stack<AbstractEvent> tempEvents)
	{

		Road road = event.road;
		AbstractVehicle vehicle = event.getVehicle();
		double date = event.getDate();
		
		tempEvents.add(new EventEnterRoad(vehicle, event.getVehicle().getTempPath().pop(), date+road.getDestination().getAverageTime()));
	}
	
}
