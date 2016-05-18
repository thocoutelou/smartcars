package smartCars;

import java.util.Stack;

public class EventVehicleEnd extends AbstractEvent{
	
	public EventVehicleEnd(AbstractVehicle vehicle, Road road, double date, Stack<Road> path)
	{
		super(vehicle, road, date);
		nature = 4;
	}
	
	public static synchronized void executeEvent(AbstractEvent event) throws IllegalStateException
	{
		event.vehicle.location.finalDate = event.date;
		event.road.vehiclesOnRoad.remove(event.vehicle);
		event.vehicle.location.actualizeLocation(event.road, event.vehicle.location.finalPosition, event.date, event.nature);
		if(!event.vehicle.location.checkFinalLocation())
		{
			throw new IllegalStateException("Le véhicule n'est pas arrivé à bon port.");
		}
	}
		
}
