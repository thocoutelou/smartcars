package smartCars;

public class EventVehicleStart extends AbstractEvent{
	
	public EventVehicleStart(AbstractVehicle vehicle)
	{
		super(vehicle, vehicle.location.initialRoad, vehicle.location.initialDate);
		nature = 0;
		executeEvent();
	}
	
	public void executeEvent()
	{
		road.vehiclesOnRoad.add(vehicle);
		vehicle.location.actualizeLocation(road, 0., date);
	}
	
	// date de l'objet
	public static void nextEvent(Road road, AbstractVehicle vehicle, double date)
	{
		if(road.equals(vehicle.location.finalRoad) & vehicle.location.initialPosition<vehicle.location.finalPosition)
		{
			vehicle.location.finalDate = date+Time.duration(road, vehicle.location.finalPosition-vehicle.location.initialPosition);
			vehicle.events.add(new EventVehicleEnd(vehicle, road, vehicle.location.finalDate));
		}
		else
		{
			double nextDate = date+Time.duration(road, road.absoluteLength-vehicle.location.initialPosition);
			vehicle.events.add(new EventWaitingOnRoad(vehicle, road, nextDate));
		}
	}
	
	// ne doit être appelée qu'après le calcul de Dijkstra sur le véhicule
	public static void vehiclesEvents(AbstractVehicle vehicle) throws IllegalStateException
	{
		if(!vehicle.pathCalculated)
		{
			throw new IllegalStateException("Dijkstra n'a pas été appliqué à ce véhicule");
		}
		else
		{
			// *** EventVehicleStart ***
			Road road = vehicle.path.pop();
			if(!road.equals(vehicle.location.initialRoad))
			{
				throw new IllegalStateException("Itinéraire Dijkstra faux");
			}
			EventVehicleStart start = new EventVehicleStart(vehicle);
			vehicle.events.add(start);
			
			// *** EventWaitingOnRoad ***
			// TODO
		}
		
	}

}
