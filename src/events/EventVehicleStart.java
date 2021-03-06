package events;

import java.util.Stack;

import graph.Road;
import resources.Time;
import smartcars.AbstractVehicle;

/**
 * Evènement représentant le démarrage du véhicule.
 * @author cylla
 *
 */
public class EventVehicleStart extends AbstractEvent{
	
	public EventVehicleStart(AbstractVehicle vehicle)
	{
		super(vehicle, vehicle.getLocation().initialRoad, vehicle.getLocation().initialDate);
		nature = 0;
	}
	
	/**
	 * Exécution d'un évènement EventVehicleStart.
	 * @param event
	 */
	public static synchronized void executeEvent(AbstractEvent event)
	{
		if(!event.getVehicle().getPathCalculated())
		{
			throw new IllegalStateException("L'itinéraire n'a pas été mis à jour.");
		}
		event.road.getVehiclesOnRoad().add(event.getVehicle());
		event.getVehicle().getLocation().actualizeLocation(event.road, event.getVehicle().getLocation().initialPosition, event.getDate(), event.nature);
	}
	
	/**
	 * Crée dans tempEvents l'évènement EventWaitingOnRoad ou EventVehicleEnd qui suit.
	 * Passage en statique à cause de l'impossibilité de cast
	 * un AbstractEvent en l'un de ses héritiers.
	 * @param event
	 * @param tempEvents
	 */
	public static void nextEvent(AbstractEvent event, Stack<AbstractEvent> tempEvents)
	{
		Road road = event.road;
		AbstractVehicle vehicle = event.getVehicle();
		double date = event.getDate();
		
		if(road.equals(vehicle.getLocation().finalRoad) & vehicle.getLocation().initialPosition<=vehicle.getLocation().finalPosition)
		{
			vehicle.getLocation().finalDate = date+Time.duration(road, vehicle.getLocation().finalPosition-vehicle.getLocation().initialPosition);
			tempEvents.add(new EventVehicleEnd(vehicle, road, vehicle.getLocation().finalDate, event.getVehicle().getTempPath()));
		}
		else
		{
			tempEvents.add(new EventWaitingOnRoad(vehicle, road, date+1.));
		}
	}

}
