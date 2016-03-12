package smartCars;

public class EventWaitingOnRoad extends AbstractEvent{
	
	public EventWaitingOnRoad(AbstractVehicle vehicle, Road road)
	{
		super(vehicle, road);
	}
	
	public void executeEvent()
	{
		road.waitingVehicles.add(vehicle);
	}

}
