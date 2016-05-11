package smartCars;

import java.util.Stack;

public class EventEnterRoad extends AbstractEvent {
	
	public EventEnterRoad(AbstractVehicle vehicle, Road roadEntered, double date)
	{
		super(vehicle, roadEntered, date);
		nature = 3;
		executeEvent();
	}
	
	public void executeEvent()
	{
		road.origin.vehiclesOnIntersection.remove(super.vehicle);
		road.vehiclesOnRoad.add(super.vehicle);
		vehicle.location.actualizeLocation(road, 0., date);
	}
	
	// date de l'objet
	public static void nextEvent(AbstractEvent event, Stack<AbstractEvent> tempEvents, Stack<Road> path)
	{
		Road road = event.road;
		AbstractVehicle vehicle = event.vehicle;
		double date = event.date;
		
		if(road.equals(vehicle.location.finalRoad))
		{
			vehicle.location.finalDate = date+Time.duration(road, vehicle.location.finalPosition);
			tempEvents.add(new EventVehicleEnd(vehicle, road, vehicle.location.finalDate, path));
		}
		else
		{
			tempEvents.add(new EventWaitingOnRoad(vehicle, road, date+1.));
		}
	}

}
