package smartCars;

public class EventVehicleEnd extends AbstractEvent{
	
	public EventVehicleEnd(AbstractVehicle vehicle, Road road, double date, boolean sameRoad)
	{
		super(vehicle, road, date);
	}
	
}
