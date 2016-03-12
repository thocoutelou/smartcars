package smartCars;

public class EventEnterRoad extends AbstractEvent {
	
	public EventEnterRoad(AbstractVehicle vehicle, Road roadEntered)
	{
		super(vehicle, roadEntered);
	}
	
	public void executeEvent()
	{
		road.origin.vehiclesOnIntersection.remove(super.vehicle);
		road.vehiclesOnRoad.add(super.vehicle);
	}

}
