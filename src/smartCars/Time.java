package smartCars;

public class Time {
	
	public static double time;

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
		System.out.println("<---   Calcul des dates réelles   --->");
		// initialisation
		PriorityQueue eventsCopy = new PriorityQueue();
		eventsCopy.qaddAll(graph.events);
		for(AbstractVehicle vehicle : graph.vehicles)
		{
			vehicle.setTempEvents();
		}
		AbstractEvent event;
		AbstractEvent vehicleEvent;
		double difference;
		
		while(!eventsCopy.qisEmpty())
		{
			event = eventsCopy.qremove();
			vehicleEvent = event.vehicle.tempEvents.qremove();
			
			System.out.print(event+"   ");
			System.out.println(event.date);
			if(!event.equals(vehicleEvent))
			{
				event = eventsCopy.qremove();
				vehicleEvent = event.vehicle.tempEvents.qremove();
				throw new IllegalStateException("Les évènements ne sont pas retournés dans l'ordre.");
			}

			if(event.nature==1 & event.trueDate)
			{
				EventWaitingOnRoad.setLeavingDate(event);
				// la ligne suivante doit être exécutée
				// impérativement après le calcul de leavingDate
				event.road.eventsWaitingOnRoad.qadd(event);
			}
			else if(event.nature==1 & !event.trueDate)
			{
				difference = event.date;
				event.date = EventWaitingOnRoad.relativeDate(event);
				event.trueDate = true;
				System.out.print("Changement de date : "+event+"   ");
				System.out.println(event.date);
				difference = event.date-difference;
				event.vehicle.tempEvents.qdates(difference);
				event.vehicle.tempEvents.qadd(event);
				eventsCopy.qadd(event);
			}
			else if(event.nature==2 & event.trueDate)
			{
				if(!event.road.eventsWaitingOnRoad.qremove().equals(event.eventWaitingOnRoad))
				{
					throw new IllegalStateException("La liste des EventWaitingOnRoad est corrompue.");
				}
			}
			else if(event.nature==2 & !event.trueDate)
			{
				difference = event.eventWaitingOnRoad.leavingDate-event.date;
				event.vehicle.tempEvents.qdates(difference);
				event.date=event.eventWaitingOnRoad.leavingDate;
				event.trueDate = true;
				event.vehicle.tempEvents.qadd(event);
				eventsCopy.qadd(event);
			}	
		}
		System.out.println();
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
