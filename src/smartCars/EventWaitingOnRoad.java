package smartCars;

import java.util.ListIterator;

public class EventWaitingOnRoad extends AbstractEvent{
	
	public double waitingTime;
	
	public EventWaitingOnRoad(AbstractVehicle vehicle, Road road, double date)
	{
		super(vehicle, road, date);
		executeEvent();
		
	}
	
	public void executeEvent()
	{
		road.waitingVehicles.add(vehicle);
		vehicle.location.waitingForIntersection = true;
		
	}
	
	public static double absoluteDate(Road road, double initialDate)
	{
		return initialDate+road.absoluteLength/road.speed;
	}
	
	// sera appelée en argument du constructeur,
	// donc l'évènement ne sera pas encore dans road.eventsWaitingOnRoad
	public static double relativeDate(Road road, double initialDate)
	{
		double absoluteDate = absoluteDate(road, initialDate);
		if(road.eventsWaitingOnRoad.isEmpty()) return absoluteDate;
		else
		{
			int i = road.eventsWaitingOnRoad.size()-1;
			EventWaitingOnRoad event = road.eventsWaitingOnRoad.get(i);
			while(event.date+event.waitingTime<absoluteDate & i>=0)
			{
				i--;
				event = road.eventsWaitingOnRoad.get(i);
			}
		}
		//TODO
		return 0.;
	}
	
	//TODO
	public void nextEvent()
	{
		
	}
	
	public int compareTo(EventWaitingOnRoad event)
	{
		if(this.date>event.date) return -1;
		else return 1;
	}

}
