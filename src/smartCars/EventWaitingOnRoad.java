package smartCars;

import java.util.Stack;

public class EventWaitingOnRoad extends AbstractEvent{
	
	public EventWaitingOnRoad(AbstractVehicle vehicle, Road road, double date)
	{
		super(vehicle, road, date);
		nature = 1;
		leavingDate = date+1.;
	}
	
	public static void executeEvent(AbstractEvent event)
	{
		event.road.waitingVehicles.add(event.vehicle);
		event.vehicle.location.waitingForIntersection = true;
		event.road.length-=event.vehicle.length+AbstractVehicle.minSpaceBetweenVehicles;
		event.road.eventsWaitingOnRoad.add(0, event);
		event.vehicle.location.actualizeLocation(event.road, event.road.length, event.date);
	}
	
	// doit absolument être appelée avant l'exécution de l'évènement (event.road.eventsWaitingOnRoad.get(0))
	public static void setLeavingDate(AbstractEvent event)
	{
		if(event.road.eventsWaitingOnRoad.isEmpty())
		{
			event.leavingDate = event.date+event.road.averageWaitingTime;
		}
		else
		{
			AbstractEvent lastEventWaitingOnRoad = event.road.eventsWaitingOnRoad.get(0);
			event.leavingDate = lastEventWaitingOnRoad.leavingDate+event.road.averageWaitingTime;
		}
	}
	
	public static double absoluteDate(Road road, double initialDate)
	{
		return initialDate+Time.duration(road, road.absoluteLength);
	}
	
	// sera appelée en argument du constructeur,
	// donc l'évènement ne sera pas encore dans road.eventsWaitingOnRoad
	public static double relativeDate(AbstractEvent event)
	{
		Road road = event.road;
		double initialDate = event.date;
		double absoluteDate = absoluteDate(road, initialDate);
		if(road.eventsWaitingOnRoad.isEmpty()) return absoluteDate;
		else
		{
			int size = road.eventsWaitingOnRoad.size();
			int left = 0;
			double arrivingDate, distance;
			if(road.eventsWaitingOnRoad.get(0).leavingDate<absoluteDate)
				return absoluteDate;
			else
			{
				do
				{
					distance = road.absoluteLength-road.lengthWaiting(left);
					arrivingDate = initialDate+Time.duration(road, distance);
					left++;
				}
				while(road.eventsWaitingOnRoad.get(size-1-left).leavingDate<arrivingDate);
				return arrivingDate;
			}
		}
	}
	
	public static void nextEvent(AbstractEvent event, Stack<AbstractEvent> tempEvents)
	{

		Road road = event.road;
		AbstractVehicle vehicle = event.vehicle;
		
		tempEvents.add(new EventLeaveRoad(vehicle, road, event.leavingDate));
	}

}
