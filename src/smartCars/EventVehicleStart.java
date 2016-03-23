package smartCars;

public class EventVehicleStart extends AbstractEvent{
	
	public EventVehicleStart(AbstractVehicle vehicle)
	{
		super(vehicle, vehicle.location.initialRoad, vehicle.location.initialDate);
		executeEvent();
		vehicle.events.add(this);
		nextEvent();
	}
	
	public void executeEvent()
	{
		road.vehiclesOnRoad.add(vehicle);
		vehicle.location.actualizeLocation(road, 0., date);
	}
	
	public void nextEvent()
	{
		if(road.equals(vehicle.location.finalRoad) & vehicle.location.initialPosition<vehicle.location.finalPosition)
		{
			vehicle.location.finalDate = date+Time.duration(road, vehicle.location.finalPosition-vehicle.location.initialPosition);
			new EventVehicleEnd(vehicle, road, vehicle.location.finalDate);
		}
		else
		{
			double nextDate = date+Time.duration(road, road.absoluteLength-vehicle.location.initialPosition);
			new EventWaitingOnRoad(vehicle, road, nextDate);
		}
	}

}
