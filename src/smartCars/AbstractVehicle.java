package smartCars;

public class AbstractVehicle {

	protected static int identificator = 0;
	public int identifier;
	protected double startDate;
	protected Location startLocation;
	public Location currentLocation;
	public double size;
	public static double minSpaceBetweenVehicles = 0.7;
	
	public AbstractVehicle()
	{
		identifier = identificator;
		identificator++;
		startDate = 0.;
		startLocation = Graph.startDefault;
	}
	
	//TODO mais encore beaucoup de travail avant cette méthode
	public void calculateLocation(float date, Graph graph)
	{
		
	}
	
	// L'hypothèse est faite que la voiture ne peut se trouver dans une intersection à l'arrêt,
	// et par conséquent à son départ.
	public AbstractIntersection intersectionAfterStart()
	{
		return startLocation.nextIntersection();
	}
	
	//TODO
	public void overtake(AbstractVehicle vehicle, Road road) throws IllegalArgumentException
	{
		if(road.lane<=1)
		{
			throw new IllegalArgumentException("");
		}
	}

}
