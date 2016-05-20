package events;

import java.util.Stack;

import graph.Road;
import resources.Time;
import smartcars.AbstractVehicle;

/**
 * Evènement représentant l'entrée du véhicule
 * dans la file d'attente pour entrer sur la prochaine intersection.
 * @author cylla
 *
 */
public class EventWaitingOnRoad extends AbstractEvent{
	
	public EventWaitingOnRoad(AbstractVehicle vehicle, Road road, double date)
	{
		super(vehicle, road, date);
		nature = 1;
		leavingDate = date+1.;
	}
	
	/**
	 * Exécution d'un évènement EventWaitingOnRoad.
	 */
	public static synchronized void executeEvent(AbstractEvent event) throws IllegalStateException
	{
		event.road.newWaitingVehicle(event.getVehicle());
		event.road.getEventsWaitingOnRoad().qadd(event);
		event.getVehicle().getLocation().actualizeLocation(event.road, event.road.getLength(), event.getDate(), event.nature);
	}
	
	/**
	 * Calcule la date réelle de l'évènement EventLeaveRoad qui suit.
	 * Doit absolument être appelée avant sans
	 * l'évènement courant dans event.road.eventsWaitingOnRoad
	 * (à cause de event.road.eventsWaitingOnRoad.get(0)).
	 * @param event
	 */
	public static synchronized void setLeavingDate(AbstractEvent event)
	{
		if(event.road.getEventsWaitingOnRoad().qisEmpty())
		{
			event.leavingDate = event.getDate()+event.road.getAverageWaitingTime();
		}
		else
		{
			System.out.println("Consultation de la liste road.eventsWaitingOnRoad");
			event.leavingDate = event.road.getEventsWaitingOnRoad().aqelement().leavingDate+event.road.getAverageWaitingTime();
		}
	}
	
	/**
	 * Calcule la date de l'évènement si le véhicule était seul sur la route.
	 * @param road
	 * @param initialDate
	 * @return date + durée de traversée de la route
	 */
	public static double absoluteDate(Road road, double initialDate)
	{
		return initialDate+Time.duration(road, road.getAbsoluteLength());
	}
	
	/**
	 * Calcul de la vraie date de l'évènement EventWaitingOnRoad.
	 * Doit être appelée seulement lors du déroulement
	 * de la liste intégrale des évènements du graphe.
	 * @param event
	 * @return date réelle
	 */
	public static synchronized double relativeDate(AbstractEvent event)
	{
		Road road = event.road;
		double initialDate = event.getDate();
		double absoluteDate = absoluteDate(road, initialDate);
		if(road.getEventsWaitingOnRoad().qisEmpty()) return absoluteDate;
		else
		{
			AbstractEvent[] eWORArray = road.eventsWaitingOnRoadToArray();
			int left = 0;
			double arrivingDate, distance;
			if(road.getEventsWaitingOnRoad().aqelement().leavingDate<absoluteDate)
				return absoluteDate;
			else
			{
				do
				{
					distance = road.getAbsoluteLength()-road.lengthWaiting(left);
					arrivingDate = initialDate+Time.duration(road, distance);
					left++;
				}
				while(eWORArray[left].leavingDate<arrivingDate);
				return arrivingDate;
			}
		}
	}
	
	/**
	 * Crée dans tempEvents l'évènement EventLeaveRoad qui suit.
	 * Passage en statique à cause de l'impossibilité de cast
	 * un AbstractEvent en l'un de ses héritiers.
	 * @param event
	 * @param tempEvents
	 */
	public static void nextEvent(AbstractEvent event, Stack<AbstractEvent> tempEvents)
	{

		Road road = event.road;
		AbstractVehicle vehicle = event.getVehicle();
		
		tempEvents.add(new EventLeaveRoad(vehicle, road, event.leavingDate, event));
	}

}
