package smartCars;

public class EventEnterRoad extends AbstractEvent {
	
	private Road roadEntered;
	
	public EventEnterRoad(AbstractVehicle vehicle, Road roadEntered)
	{
		super(vehicle);
		this.roadEntered = roadEntered;
	}
	
	public void ExecuteEvent()
	{
		roadEntered.origin.vehiclesOnIntersection.remove(super.vehicle);
		roadEntered.vehiclesOnRoad.add(super.vehicle);
	}

}
