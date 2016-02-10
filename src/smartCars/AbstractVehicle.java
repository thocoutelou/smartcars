package smartCars;

public class AbstractVehicle {

	protected double startDate;
	protected Location startLocation;
	public Location currentLocation;
	public double size;
	
	//TODO mais encore beaucoup de travail avant cette méthode
	public void calculateLocation(float time, Graph graph)
	{
		
	}
	
	// L'hypothèse est faite que la voiture ne peut se trouver dans une intersection à l'arrêt,
	// et par conséquent à son départ.
	public AbstractIntersection intersectionAfterStart()
	{
		return startLocation.nextIntersection();
	}
	
	//TODO
	public void overtake(AbstractVehicle vehicle, Road road)
	{
		
	}

}
