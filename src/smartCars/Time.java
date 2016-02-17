package smartCars;

public class Time {
	
	//TODO Penser une façon de gérer le temps
	// - avec une classe "Events" basée sur une file chronologique ?
	// - 'a posteriori' de Dijkstra pour chaque voiture ?
	public static double time = 0.;
	
	// 0. correspond à minuit
	private final static double morningRushStart = 25200.; //7h
	private final static double morningRushEnd = 32400.; //9h
	private final static double eveningRushStart = 61200.; //17h
	private final static double eveningRushEnd = 68400.; //19h
	
	public static boolean Rush()
	{
		double dayTime = Time.time % 86400.;
		boolean morningRush = (dayTime>morningRushStart)&(dayTime<morningRushEnd);
		boolean eveningRush = (dayTime>eveningRushStart)&(dayTime<eveningRushEnd);
		return morningRush|eveningRush;
	}
	
	public static void setStartingTime() throws IllegalStateException
	{
		if(Graph.vehiclesInGraph.isEmpty())
		{
			throw new IllegalStateException("Le graphe est mal initialisé : aucun véhicule");
		}
		double startingTime = Graph.vehiclesInGraph.get(0).location.initialDate;
		for(AbstractVehicle v : Graph.vehiclesInGraph)
		{
			startingTime = Math.min(startingTime, v.location.initialDate);
		}
		time = startingTime;
	}

}
