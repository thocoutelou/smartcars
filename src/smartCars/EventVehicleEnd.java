package smartCars;

public class EventVehicleEnd extends AbstractEvent{
	
	public EventVehicleEnd(AbstractVehicle vehicle, Road road, double date)
	{
		super(vehicle, road, date);
		executeEvent();
	}
	
	public void executeEvent() throws IllegalStateException
	{
		if(vehicle.pathCalculated & !vehicle.path.isEmpty())
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
