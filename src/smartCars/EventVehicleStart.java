package smartCars;

public class EventVehicleStart extends AbstractEvent{
	
	public EventVehicleStart(AbstractVehicle vehicle)
	{
		super(vehicle, vehicle.location.initialRoad, vehicle.location.initialDate);
		executeEvent();
		nextEvent();
	}
	
	public void executeEvent()
	{
		road.vehiclesOnRoad.add(vehicle);
		actualizeLocation();
	}
	
	public void nextEvent()
	{
		if(road.equals(vehicle.location.finalRoad) & vehicle.location.initialPosition<vehicle.location.finalPosition)
		{
			double nextDate = date+(vehicle.location.finalPosition-vehicle.location.initialPosition)/road.speed;
			new EventVehicleEnd(vehicle, road, nextDate, true);
		}
		else
		{
			double nextDate = date+(road.absoluteLength-vehicle.location.initialPosition)/road.speed;
			new EventWaitingOnRoad(vehicle, road, nextDate);
		}
	}

	public void actualizeLocation()
	{
		vehicle.location.currentDate = date;
		vehicle.location.currentPosition = 0.;
		vehicle.location.currentRoad = road;
	}
}
