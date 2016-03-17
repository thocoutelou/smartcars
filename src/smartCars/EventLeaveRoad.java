package smartCars;

public class EventLeaveRoad extends AbstractEvent {
	
	public EventLeaveRoad(AbstractVehicle vehicle, Road roadLeft, double date)
	{
		super(vehicle, roadLeft, date);
		executeEvent();
		vehicle.events.add(this);
		nextEvent();
	}
	
	public void executeEvent()
	{
		road.waitingVehicles.remove(super.vehicle);
		vehicle.location.waitingForIntersection = false;
		road.vehiclesOnRoad.remove(super.vehicle);
		road.destination.vehiclesOnIntersection.add(super.vehicle);
		vehicle.location.actualizeLocation(road, road.length, date);
	}
	
	public void nextEvent()
	{
		new EventEnterRoad(vehicle, vehicle.path.pop(), date+road.destination.averageTime);
	}
	
}
