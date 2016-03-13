package smartCars;


public class EventWaitingOnRoad extends AbstractEvent{
	
	public double waitingTime;
	
	public EventWaitingOnRoad(AbstractVehicle vehicle, Road road, double date)
	{
		super(vehicle, road, date);
		waitingTime = road.averageWaitingTime;
	}
	
	public void executeEvent()
	{
		road.waitingVehicles.add(vehicle);
		vehicle.location.waitingForIntersection = true;
		road.length-=vehicle.length+AbstractVehicle.minSpaceBetweenVehicles;
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
			int i=0;
			int numberOfEvents = road.eventsWaitingOnRoad.size();
			EventWaitingOnRoad event = road.eventsWaitingOnRoad.get(i);
			while(event.date+event.waitingTime<absoluteDate & i<numberOfEvents)
			{
				i++;
				event = road.eventsWaitingOnRoad.get(i);
			}
			if(i==numberOfEvents) return absoluteDate;
			else
			{
				//TODO
				return 0.;
			}
		}
	}
	
	//TODO
	public void nextEvent()
	{
		
	}

}
