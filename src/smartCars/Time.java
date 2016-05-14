package smartCars;

import java.util.PriorityQueue;
import java.util.concurrent.PriorityBlockingQueue;

public class Time {
	
	public static double time;

	// évènements à survenir dans l'ordre chronologique, une fois calculés
	public static PriorityBlockingQueue<AbstractEvent> events = new PriorityBlockingQueue<AbstractEvent>();
	

	public static double duration(Road road, double distance)
	{
		return 3.6*distance/road.speed;
	}
	
	public static double startingTime(GraphState graphState) throws IllegalStateException
	{
		if(graphState.vehicles.isEmpty())
		{
			throw new IllegalStateException("Le graphe est mal initialisé : aucun véhicule");
		}
		double startingTime = graphState.vehicles.get(0).location.initialDate;
		for(AbstractVehicle v : graphState.vehicles)
		{
			startingTime = Math.min(startingTime, v.location.initialDate);
		}
		return startingTime;
	}
	
	public static void realDates(GraphState graph)
	{
		PriorityQueue<AbstractEvent> eventsCopy = new PriorityQueue<AbstractEvent>(new AbstractEvent.EventComparator());
		for(AbstractEvent event : graph.events)
		{
			eventsCopy.add(event);
		}
		for(AbstractVehicle vehicle : graph.vehicles)
		{
			vehicle.setTempEvents();
		}
		AbstractEvent event;
		AbstractEvent vehicleEvent;
		double realDate;
		while(!eventsCopy.isEmpty())
		{
			event = eventsCopy.remove();
			vehicleEvent = event.vehicle.tempEvents.remove();
			System.out.println("Prout");
			if(!event.equals(vehicleEvent))
			{
				throw new IllegalStateException("Les évènements ne sont pas retournés dans l'ordre.");
			}
			if(event.nature==1 & !event.trueDate)
			{
				realDate = EventWaitingOnRoad.relativeDate(event);
				increaseFollowingDates(event.vehicle, event.date, realDate);
				event.date = realDate;
				event.trueDate = true;
				if(event.vehicle.tempEvents.peek().nature!=2)
				{
					throw new IllegalStateException("Un EventWaitingOnRoad doit être suivi par un EventLeaveRoad.");
				}
				else
				{
					EventWaitingOnRoad.setLeavingDate(event);
					event.vehicle.tempEvents.peek().date=event.leavingDate;
				}
				eventsCopy.add(event);
				event.vehicle.tempEvents.add(event);
			}
			else
			{
				if(event.nature==0)
				{
					EventVehicleStart.executeEvent(event);
				}
				else if(event.nature==1 & event.trueDate)
				{
					EventWaitingOnRoad.executeEvent(event);
				}
				else if(event.nature==2)
				{
					EventLeaveRoad.executeEvent(event);
				}
				else if(event.nature==3)
				{
					EventEnterRoad.executeEvent(event);
				}
				else if(event.nature==4)
				{
					EventVehicleEnd.executeEvent(event);
				}
			}
		}
	}
	
	public static void increaseFollowingDates(AbstractVehicle vehicle, double date, double realDate)
	{
		//TODO : déplacer les exécutions des évènements (les retirer des constructeurs)
		PriorityQueue<AbstractEvent> eventsCopy = new PriorityQueue<AbstractEvent>(new AbstractEvent.EventComparator());
		for(AbstractEvent event : vehicle.events)
		{
			eventsCopy.add(event);
		}
		AbstractEvent event = eventsCopy.peek();
		while(event.date<=date & eventsCopy.size()>1)
		{
			eventsCopy.remove();
			event = eventsCopy.peek();
		}
		for(AbstractEvent e : eventsCopy)
		{
			e.date+=realDate-date;
		}
	}
	
	/* Bonus : système de gestion des heures de pointe.

	// 0. correspond à minuit
	private final static double morningRushStart = 25200.; //7h
	private final static double morningRushEnd = 32400.; //9h
	private final static double eveningRushStart = 61200.; //17h
	private final static double eveningRushEnd = 68400.; //19h
	
	
	public static boolean rush()
	{
		double dayTime = Time.time % 86400.;
		boolean morningRush = (dayTime>morningRushStart)&(dayTime<morningRushEnd);
		boolean eveningRush = (dayTime>eveningRushStart)&(dayTime<eveningRushEnd);
		return morningRush|eveningRush;
	}
	
	*/

}
