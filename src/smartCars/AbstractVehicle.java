package smartCars;


import java.util.ArrayList;
import java.util.Stack;

public class AbstractVehicle {

	protected static int identificator = 0;
	public int identifier;
	
	// (vehicleLocation) contient toutes les informations de temps et de position
	public Location location;
	public double size;
	public static double minSpaceBetweenVehicles = 0.7;
	
	protected boolean pathCalculated = false;
	protected Stack<Road> path;
	// (itinary) sauvegarde les routes déjà empruntées en vue de les revoir
	// d'où sa structure de pile FIFO (Stack)
	protected Stack<Road> itinary;
	
	public AbstractVehicle()
	{
		identifier = identificator;
		identificator++;
		location = Graph.startDefault;
	}
	
	
	public void savePath(Stack<Road> path)
	{
		this.pathCalculated = true;
		this.path = path;
	}
	
	
	public static boolean inferiorPath(AbstractVehicle a, AbstractVehicle b)
	{
		Cost aPath = Graph.costsMatrix[a.intersectionBeforeEnd().identifier][a.intersectionAfterStart().identifier];
		Cost bPath = Graph.costsMatrix[b.intersectionBeforeEnd().identifier][b.intersectionAfterStart().identifier];
		if(Cost.inferior(aPath, bPath)) return true;
		else return false;
	}
	
	
	//TODO mais encore beaucoup de travail avant cette méthode
	public void calculateLocation(float date, Graph graph)
	{
		
	}
	
	// L'hypothèse est faite que la voiture ne peut se trouver dans une intersection à l'arrêt,
	// et par conséquent à son départ...
	public AbstractIntersection intersectionAfterStart()
	{
		return location.nextIntersection();
	}
	// ... et à son arrivée.
	public AbstractIntersection intersectionBeforeEnd()
	{
		return location.finalIntersection();
	}
	
	
	public static AbstractVehicle lessPriorityVehicle(ArrayList<AbstractVehicle> vehicles)
	{
		AbstractVehicle lessPriorityVehicle = vehicles.get(0);
		for(AbstractVehicle v : vehicles)
		{
			if(inferiorPath(v, lessPriorityVehicle))
			{
				lessPriorityVehicle = v;
			}
		}
		vehicles.remove(lessPriorityVehicle);
		return lessPriorityVehicle;
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





