package smartCars;

import java.util.Stack;

public class EventVehicleEnd extends AbstractEvent{
	
	public EventVehicleEnd(AbstractVehicle vehicle, Road road, double date, Stack<Road> path)
	{
		super(vehicle, road, date);
		nature = 4;
		executeEvent(path);
	}
	
	public void executeEvent(Stack<Road> path) throws IllegalStateException
	{
		if(vehicle.pathCalculated & !path.isEmpty())
		{
			throw new IllegalStateException("L'itinéraire est faux ou n'a pas été mis à jour.");
		}
		else
		{
			road.vehiclesOnRoad.remove(vehicle);
			vehicle.location.actualizeLocation(road, vehicle.location.finalPosition, date);
			if(!vehicle.location.checkFinalLocation())
			{
				throw new IllegalStateException("Le véhicule n'est pas arrivé à bon port.");
			}
		}
	}
	
}
