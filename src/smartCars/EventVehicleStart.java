package smartCars;

import java.util.Stack;

public class EventVehicleStart extends AbstractEvent{
	
	public EventVehicleStart(AbstractVehicle vehicle, Stack<Road> path)
	{
		super(vehicle, vehicle.location.initialRoad, vehicle.location.initialDate);
		nature = 0;
		executeEvent(path);
	}
	
	public void executeEvent(Stack<Road> path)
	{
		// test à effet de bord
		if(!path.pop().equals(vehicle.location.initialRoad))
		{
			throw new IllegalStateException("Itinéraire Dijkstra faux.");
		}
		road.vehiclesOnRoad.add(vehicle);
		vehicle.location.actualizeLocation(road, 0., date);
	}
	
	// passage en statique à cause de l'impossibilité de cast
	// un AbstractEvent en l'un de ses héritiers
	public static void nextEvent(AbstractEvent event, Stack<AbstractEvent> tempEvents, Stack<Road> path)
	{
		Road road = event.road;
		AbstractVehicle vehicle = event.vehicle;
		double date = event.date;
		
		if(road.equals(vehicle.location.finalRoad) & vehicle.location.initialPosition<=vehicle.location.finalPosition)
		{
			vehicle.location.finalDate = date+Time.duration(road, vehicle.location.finalPosition-vehicle.location.initialPosition);
			tempEvents.add(new EventVehicleEnd(vehicle, road, vehicle.location.finalDate, path));
		}
		else
		{
			// double nextDate = date+Time.duration(road, road.absoluteLength-vehicle.location.initialPosition);
			tempEvents.add(new EventWaitingOnRoad(vehicle, road, date+1.));
		}
	}

}
