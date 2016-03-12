package smartCars;

public class EventLeaveRoad extends AbstractEvent {
	
	public EventLeaveRoad(AbstractVehicle vehicle, Road roadLeft, double date)
	{
		super(vehicle, roadLeft, date);
	}
	
	public void executeEvent()
	{
		road.waitingVehicles.remove(super.vehicle);
		road.vehiclesOnRoad.remove(super.vehicle);
		road.destination.vehiclesOnIntersection.add(super.vehicle);
	}

}
