package smartCars;

public class EventEnterRoad extends AbstractEvent {
	
	public EventEnterRoad(AbstractVehicle vehicle, Road roadEntered, double date)
	{
		super(vehicle, roadEntered, date);
		executeEvent();
		vehicle.events.add(this);
		nextEvent();
	}
	
	public void executeEvent()
	{
		road.origin.vehiclesOnIntersection.remove(super.vehicle);
		road.vehiclesOnRoad.add(super.vehicle);
		vehicle.location.actualizeLocation(road, 0., date);
	}
	
	public void nextEvent()
	{
		if(this.road.equals(vehicle.location.finalRoad))
		{
			vehicle.location.finalDate = date+duration(road, vehicle.location.finalPosition);
			new EventVehicleEnd(vehicle, road, date+duration(road, vehicle.location.finalDate));
		}
	}

}
