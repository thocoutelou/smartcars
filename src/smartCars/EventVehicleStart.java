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
		
	}
	
	//TODO
	public void nextEvent()
	{
		if(road.equals(vehicle.location.finalRoad) & vehicle.location.initialPosition<vehicle.location.finalPosition)
		{
			
			new EventVehicleEnd(vehicle, road, date, true);
		}
		else new EventWaitingOnRoad(vehicle, road, date);
	}

}
