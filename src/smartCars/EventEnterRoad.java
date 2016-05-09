package smartCars;

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
	public static void nextEvent(Road road, AbstractVehicle vehicle, double date)
	{
		if(road.equals(vehicle.location.finalRoad))
		{
			vehicle.location.finalDate = date+Time.duration(road, vehicle.location.finalPosition);
			vehicle.events.add(new EventVehicleEnd(vehicle, road, date+Time.duration(road, vehicle.location.finalDate)));
		}
		else
		{
			//TODO
		}
	}

}
