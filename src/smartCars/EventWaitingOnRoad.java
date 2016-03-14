package smartCars;


public class EventWaitingOnRoad extends AbstractEvent{
	
	public double waitingTime;
	public double leavingDate;
	
	public EventWaitingOnRoad(AbstractVehicle vehicle, Road road, double date)
	{
		super(vehicle, road, date);
		waitingTime = road.averageWaitingTime;
		leavingDate = date+waitingTime;
	}
	
	public void executeEvent()
	{
		road.waitingVehicles.add(vehicle);
		vehicle.location.waitingForIntersection = true;
		road.length-=vehicle.length+AbstractVehicle.minSpaceBetweenVehicles;
		if(road.eventsWaitingOnRoad.isEmpty())
		{
			waitingTime = road.averageWaitingTime;
			leavingDate = date+waitingTime;
		}
		else
		{
			EventWaitingOnRoad event = road.eventsWaitingOnRoad.get(0);
			this.leavingDate = event.leavingDate+road.averageWaitingTime;
			this.waitingTime = this.leavingDate-this.date;
		}
		road.eventsWaitingOnRoad.add(0, this);
	}
	
	public static double duration(Road road, double distance)
	{
		return distance/road.speed;
	}
	
	public static double absoluteDate(Road road, double initialDate)
	{
		return initialDate+duration(road, road.absoluteLength);
	}
	
	// sera appelée en argument du constructeur,
	// donc l'évènement ne sera pas encore dans road.eventsWaitingOnRoad
	public static double relativeDate(Road road, double initialDate)
	{
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
					arrivingDate = initialDate+duration(road, distance);
					left++;
				}
				while(road.eventsWaitingOnRoad.get(size-1-left).leavingDate<arrivingDate);
				return arrivingDate;
			}
		}
	}
	
	//TODO
	public void nextEvent()
	{
		
	}

}
