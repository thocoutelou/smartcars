package smartCars;

public class EventLeaveRoad extends AbstractEvent {
	
	private Road roadLeft;
	
	public EventLeaveRoad(AbstractVehicle vehicle, Road roadLeft)
	{
		super(vehicle);
		this.roadLeft = roadLeft;
	}
	
	public void ExecuteEvent()
	{
		roadLeft.vehiclesOnRoad.remove(super.vehicle);
		roadLeft.destination.vehiclesOnIntersection.add(super.vehicle);
	}

}
