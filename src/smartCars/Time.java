package smartCars;

import java.util.PriorityQueue;
import java.util.concurrent.PriorityBlockingQueue;

public class Time {
	
	public static double time;

	// évènements à survenir dans l'ordre chronologique, une fois calculés
	public static PriorityBlockingQueue<AbstractEvent> events = new PriorityBlockingQueue<AbstractEvent>();
	

	public static double duration(Road road, double distance)
	{
		return 3600.*distance/road.speed;
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
	
	public static synchronized void realDates(GraphState graph)
	{
		PriorityQueue<AbstractEvent> eventsCopy = new PriorityQueue<AbstractEvent>(new AbstractEvent.EventChronos());
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
		AbstractEvent eventLeaveRoad;
		double difference;
		while(!eventsCopy.isEmpty())
		{
			event = eventsCopy.remove();
			vehicleEvent = event.vehicle.tempEvents.remove();
			System.out.print(event+"   ");
			System.out.println(event.date);
			System.out.print(vehicleEvent+"   ");
			System.out.println(vehicleEvent.date);
			System.out.println("Prout");
			/*if(!event.equals(vehicleEvent))
			{
				throw new IllegalStateException("Les évènements ne sont pas retournés dans l'ordre.");
			}*/
			if(event.nature==1 & !event.trueDate)
			{
				difference = event.date;
				event.date = EventWaitingOnRoad.relativeDate(event);
				event.trueDate = true;
				System.out.print("\nChangement de date : "+event+"   ");
				System.out.println(event.date+"\n");
				difference = event.date-difference;
				increaseFollowingDates(event, difference);
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
					eventLeaveRoad = event.vehicle.tempEvents.remove();
					if(eventLeaveRoad.nature!=2)
					{
						System.out.println("Crap: "+eventLeaveRoad);
						throw new IllegalStateException("Un EventWaitingOnRoad doit être suivi par un EventLeaveRoad.");
					}
					else
					{
						EventWaitingOnRoad.setLeavingDate(event);
						difference = event.leavingDate-eventLeaveRoad.date;
						System.out.println(event.date);
						System.out.println(eventLeaveRoad.date);
						System.out.println(event.leavingDate);
						increaseFollowingDates(event, difference);
						eventLeaveRoad.date=event.leavingDate;
					}
					event.vehicle.tempEvents.add(eventLeaveRoad);
					EventWaitingOnRoad.executeEvent(event);
				}
				else if(event.nature==2)
				{
					EventLeaveRoad.executeEvent(event, event.eventWaitingOnRoad);
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
	
	public static void increaseFollowingDates(AbstractEvent event, double difference)
	{
		if(difference<0)
		{
			System.out.println("\n*** Be aware *** Debug time has come!  "+difference);
			throw new IllegalArgumentException("L'incrément de temps doit être positif.");
		}
		for(AbstractEvent e : event.vehicle.tempEvents)
		{
			e.date+=difference;
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
