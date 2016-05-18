package smartCars;

import java.util.Stack;

public class EventVehicleStart extends AbstractEvent{
	
	public EventVehicleStart(AbstractVehicle vehicle)
	{
		super(vehicle, vehicle.location.initialRoad, vehicle.location.initialDate);
		nature = 0;
	}
	
	public static synchronized void executeEvent(AbstractEvent event)
	{
		if(!event.vehicle.pathCalculated)
		{
			throw new IllegalStateException("L'itinéraire n'a pas été mis à jour.");
		}
		event.road.vehiclesOnRoad.add(event.vehicle);
		event.vehicle.location.actualizeLocation(event.road, event.vehicle.location.initialPosition, event.date, event.nature);
	}
	
	// passage en statique à cause de l'impossibilité de cast
	// un AbstractEvent en l'un de ses héritiers
	public static void nextEvent(AbstractEvent event, Stack<AbstractEvent> tempEvents)
	{
		Road road = event.road;
		AbstractVehicle vehicle = event.vehicle;
		double date = event.date;
		
		if(road.equals(vehicle.location.finalRoad) & vehicle.location.initialPosition<=vehicle.location.finalPosition)
		{
			vehicle.location.finalDate = date+Time.duration(road, vehicle.location.finalPosition-vehicle.location.initialPosition);
			tempEvents.add(new EventVehicleEnd(vehicle, road, vehicle.location.finalDate, event.vehicle.tempPath));
		}
		else
		{
			tempEvents.add(new EventWaitingOnRoad(vehicle, road, date+1.));
		}
	}

}
