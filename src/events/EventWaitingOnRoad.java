package events;

import java.util.Stack;

import graph.Road;
import resources.Time;
import smartcars.AbstractVehicle;

public class EventWaitingOnRoad extends AbstractEvent{
	
	public EventWaitingOnRoad(AbstractVehicle vehicle, Road road, double date)
	{
		super(vehicle, road, date);
		nature = 1;
		leavingDate = date+1.;
	}
	
	public static synchronized void executeEvent(AbstractEvent event) throws IllegalStateException
	{
		event.road.newWaitingVehicle(event.getVehicle());
		event.road.getEventsWaitingOnRoad().qadd(event);
		event.getVehicle().getLocation().actualizeLocation(event.road, event.road.getLength(), event.getDate(), event.nature);
	}
	
	// doit absolument être appelée avant l'exécution de l'évènement (event.road.eventsWaitingOnRoad.get(0))
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
	
	public static double absoluteDate(Road road, double initialDate)
	{
		return initialDate+Time.duration(road, road.getAbsoluteLength());
	}
	
	// sera appelée en argument du constructeur,
	// donc l'évènement ne sera pas encore dans road.eventsWaitingOnRoad
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
	
	public static void nextEvent(AbstractEvent event, Stack<AbstractEvent> tempEvents)
	{

		Road road = event.road;
		AbstractVehicle vehicle = event.getVehicle();
		
		tempEvents.add(new EventLeaveRoad(vehicle, road, event.leavingDate, event));
	}

}
