package smartCars;

public class EventLeaveRoad extends AbstractEvent {
	
	public EventLeaveRoad(AbstractVehicle vehicle, Road roadLeft, double date)
	{
		super(vehicle, roadLeft, date);
		nature = 2;
		executeEvent();
	}
	
	public void executeEvent()
	{
		road.waitingVehicles.remove(super.vehicle);
		vehicle.location.waitingForIntersection = false;
		road.vehiclesOnRoad.remove(super.vehicle);
		road.destination.vehiclesOnIntersection.add(super.vehicle);
		vehicle.location.actualizeLocation(road, road.length, date);
	}
	
	// date de l'objet
	public static void nextEvent(AbstractVehicle vehicle, double date, Road road)
	{
		vehicle.events.add(new EventEnterRoad(vehicle, vehicle.path.pop(), date+road.destination.averageTime));
	}
	
}
