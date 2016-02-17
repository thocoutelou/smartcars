package smartCars;


import java.util.Queue;
import java.util.Stack;

public class AbstractVehicle {

	protected static int identificator = 0;
	public int identifier;
	
	// (vehicleLocation) contient toutes les informations de temps et de position
	public Location location;
	public double size;
	public static double minSpaceBetweenVehicles = 0.7;
	
	protected boolean routeExists = false;
	protected Queue<Road> route;
	// (itinary) sauvegarde les routes déjà empruntées en vue de les revoir
	// d'où sa structure de pile FIFO (Stack)
	protected Stack<Road> itinary;
	
	public AbstractVehicle()
	{
		identifier = identificator;
		identificator++;
		location = Graph.startDefault;
	}
	
	
	public void saveRoute(Queue<Road> route)
	{
		this.routeExists = true;
		this.route = route;
	}
	
	
	//TODO mais encore beaucoup de travail avant cette méthode
	public void calculateLocation(float date, Graph graph)
	{
		
	}
	
	// L'hypothèse est faite que la voiture ne peut se trouver dans une intersection à l'arrêt,
	// et par conséquent à son départ.
	public AbstractIntersection intersectionAfterStart()
	{
		return location.nextIntersection();
	}
	
	
	public void overtake(AbstractVehicle vehicleOvertaking, Road road) throws IllegalArgumentException
	{
		if(road.lane==1)
		{
			throw new IllegalArgumentException("Impossible de doubler, la route n'est formée que d'une seule voie");
		}
		//TODO
		else
		{
			
		}
	}

}





