package smartCars;

import java.util.concurrent.PriorityBlockingQueue;

public class Time {
	
	public static double time;

	// évènements à survenir dans l'ordre chronologique, une fois calculés
	public static PriorityBlockingQueue<AbstractEvent> events = new PriorityBlockingQueue<AbstractEvent>();
	

	public static double duration(Road road, double distance)
	{
		return distance/road.speed;
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
