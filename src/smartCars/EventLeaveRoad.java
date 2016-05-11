package smartCars;

import java.util.Stack;

public class EventLeaveRoad extends AbstractEvent {
	
	public EventLeaveRoad(AbstractVehicle vehicle, Road roadLeft, double date)
	{
		super(vehicle, roadLeft, date);
		nature = 2;
		executeEvent();
	}
	
	public void executeEvent()
	{
		road.waitingVehicles.remove(super.vehicle);
		vehicle.location.waitingForIntersection = false;
		road.vehiclesOnRoad.remove(super.vehicle);
		road.destination.vehiclesOnIntersection.add(super.vehicle);
		vehicle.location.actualizeLocation(road, road.length, date);
	}
	
	// date de l'objet
	public static void nextEvent(AbstractEvent event, Stack<AbstractEvent> tempEvents, Stack<Road> path)
	{

		Road road = event.road;
		AbstractVehicle vehicle = event.vehicle;
		double date = event.date;
		
		tempEvents.add(new EventEnterRoad(vehicle, path.pop(), date+road.destination.averageTime));
	}
	
}
