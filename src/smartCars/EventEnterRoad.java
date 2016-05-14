package smartCars;

import java.util.Stack;

public class EventEnterRoad extends AbstractEvent {
	
	public EventEnterRoad(AbstractVehicle vehicle, Road roadEntered, double date)
	{
		super(vehicle, roadEntered, date);
		nature = 3;
	}
	
	public static void executeEvent(AbstractEvent event)
	{
		event.road.origin.vehiclesOnIntersection.remove(event.vehicle);
		event.road.vehiclesOnRoad.add(event.vehicle);
		event.vehicle.location.actualizeLocation(event.road, 0., event.date);
	}
	
	// date de l'objet
	public static void nextEvent(AbstractEvent event, Stack<AbstractEvent> tempEvents)
	{
		Road road = event.road;
		AbstractVehicle vehicle = event.vehicle;
		double date = event.date;
		
		if(road.equals(vehicle.location.finalRoad))
		{
			vehicle.location.finalDate = date+Time.duration(road, vehicle.location.finalPosition);
			tempEvents.add(new EventVehicleEnd(vehicle, road, vehicle.location.finalDate, event.vehicle.tempPath));
		}
		else
		{
			tempEvents.add(new EventWaitingOnRoad(vehicle, road, date+1.));
		}
	}

}
