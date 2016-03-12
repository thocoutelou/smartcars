package smartCars;

public class EventWaitingOnRoad extends AbstractEvent{
	
	public EventWaitingOnRoad(AbstractVehicle vehicle, Road road, double date)
	{
		super(vehicle, road, date);
	}
	
	public void executeEvent()
	{
		road.waitingVehicles.add(vehicle);
	}
	
	public static double absoluteDate(Road road, double initialDate)
	{
		return initialDate+road.absoluteLength/road.speed;
	}

}
