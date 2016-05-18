package smartCars;

import java.util.Stack;

public class EventLeaveRoad extends AbstractEvent {
	
	public EventLeaveRoad(AbstractVehicle vehicle, Road roadLeft, double date, AbstractEvent eventWOR)
	{
		super(vehicle, roadLeft, date);
		eventWaitingOnRoad = eventWOR;
		nature = 2;
	}
	
	public static synchronized void executeEvent(AbstractEvent event)
	{
		event.road.formerWaitingVehicle(event.vehicle);
		event.road.destination.vehiclesOnIntersection.add(event.vehicle);
		event.vehicle.location.actualizeLocation(event.road, event.road.length, event.date, event.nature);
	}
	
	public static void nextEvent(AbstractEvent event, Stack<AbstractEvent> tempEvents)
	{

		Road road = event.road;
		AbstractVehicle vehicle = event.vehicle;
		double date = event.date;
		
		tempEvents.add(new EventEnterRoad(vehicle, event.vehicle.tempPath.pop(), date+road.destination.averageTime));
	}
	
}
