package events;

import java.util.Stack;

import graph.Road;
import resources.Time;
import smartcars.AbstractVehicle;

public class EventEnterRoad extends AbstractEvent {
	
	public EventEnterRoad(AbstractVehicle vehicle, Road roadEntered, double date)
	{
		super(vehicle, roadEntered, date);
		nature = 3;
	}
	
	public static synchronized void executeEvent(AbstractEvent event)
	{
		event.road.getOrigin().getVehiclesOnIntersection().remove(event.getVehicle());
		event.road.getVehiclesOnRoad().add(event.getVehicle());
		event.getVehicle().getLocation().actualizeLocation(event.road, 0., event.getDate(), event.nature);
	}
	
	// date de l'objet
	public static void nextEvent(AbstractEvent event, Stack<AbstractEvent> tempEvents)
	{
		Road road = event.road;
		AbstractVehicle vehicle = event.getVehicle();
		double date = event.getDate();
		
		if(road.equals(vehicle.getLocation().finalRoad))
		{
			vehicle.getLocation().finalDate = date+Time.duration(road, vehicle.getLocation().finalPosition);
			tempEvents.add(new EventVehicleEnd(vehicle, road, vehicle.getLocation().finalDate, event.getVehicle().getTempPath()));
		}
		else
		{
			tempEvents.add(new EventWaitingOnRoad(vehicle, road, date+1.));
		}
	}

}
