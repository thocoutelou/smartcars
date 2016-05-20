package events;

import java.util.Stack;

import graph.Road;
import smartcars.AbstractVehicle;

public class EventVehicleEnd extends AbstractEvent{
	
	public EventVehicleEnd(AbstractVehicle vehicle, Road road, double date, Stack<Road> path)
	{
		super(vehicle, road, date);
		nature = 4;
	}
	
	public static synchronized void executeEvent(AbstractEvent event) throws IllegalStateException
	{
		event.getVehicle().getLocation().finalDate = event.getDate();
		event.road.removeVehiclesOnRoad(event.getVehicle());
		event.getVehicle().getLocation().actualizeLocation(event.road, event.getVehicle().getLocation().finalPosition, event.getDate(), event.nature);
		if(!event.getVehicle().getLocation().checkFinalLocation())
		{
			throw new IllegalStateException("Le véhicule n'est pas arrivé à bon port.");
		}
	}
		
}
