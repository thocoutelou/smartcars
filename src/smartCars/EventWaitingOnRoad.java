package smartCars;

import java.util.Stack;

public class EventWaitingOnRoad extends AbstractEvent{
	
	public EventWaitingOnRoad(AbstractVehicle vehicle, Road road, double date)
	{
		super(vehicle, road, date);
		nature = 1;
		leavingDate = date+1.;
	}
	
	public static synchronized void executeEvent(AbstractEvent event) throws IllegalStateException
	{
		event.road.newWaitingVehicle(event.vehicle);
		event.road.eventsWaitingOnRoad.qadd(event);
		event.vehicle.location.actualizeLocation(event.road, event.road.length, event.date, event.nature);
	}
	
	// doit absolument être appelée avant l'exécution de l'évènement (event.road.eventsWaitingOnRoad.get(0))
	public static synchronized void setLeavingDate(AbstractEvent event)
	{
		if(event.road.eventsWaitingOnRoad.qisEmpty())
		{
			event.leavingDate = event.date+event.road.averageWaitingTime;
		}
		else
		{
			System.out.println("Consultation de la liste road.eventsWaitingOnRoad");
			event.leavingDate = event.road.eventsWaitingOnRoad.aqelement().leavingDate+event.road.averageWaitingTime;
		}
	}
	
	public static double absoluteDate(Road road, double initialDate)
	{
		return initialDate+Time.duration(road, road.absoluteLength);
	}
	
	// sera appelée en argument du constructeur,
	// donc l'évènement ne sera pas encore dans road.eventsWaitingOnRoad
	public static synchronized double relativeDate(AbstractEvent event)
	{
		Road road = event.road;
		double initialDate = event.date;
		double absoluteDate = absoluteDate(road, initialDate);
		if(road.eventsWaitingOnRoad.qisEmpty()) return absoluteDate;
		else
		{
			AbstractEvent[] eWORArray = road.eventsWaitingOnRoadToArray();
			int left = 0;
			double arrivingDate, distance;
			if(road.eventsWaitingOnRoad.aqelement().leavingDate<absoluteDate)
				return absoluteDate;
			else
			{
				do
				{
					distance = road.absoluteLength-road.lengthWaiting(left);
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
		AbstractVehicle vehicle = event.vehicle;
		
		tempEvents.add(new EventLeaveRoad(vehicle, road, event.leavingDate, event));
	}

}
