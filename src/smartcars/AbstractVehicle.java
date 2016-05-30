package smartcars;

import java.util.Stack;

import events.AbstractEvent;
import graph.AbstractIntersection;
import graph.Graph;
import graph.Road;
import resources.Cost;
import resources.Location;
import resources.PriorityQueue;
import resources.Time;

/**
 * Définit un véhicule.
 * Le véhicule connaîtra son itinéraire
 * une fois celui-ci calculé.
 * @author cylla
 *
 */
public class AbstractVehicle {

	// identificateur des instances, s'incrémente à chaque instanciation...
	private static int identificator = 0;
	// ... pour définir l'identifiant du véhicule créé
	private int identifier;
	
	// localise le point de départ de la voiture, sa destination,
	// et sera mis à jour si une requête de visualisation de la carte est formulée
	private Location location;
	// longueur de la voiture
	protected double length;
	// espace moyen entre deux véhicules à l'arrêt
	private static double minSpaceBetweenVehicles = 0.7;
	
	// trajectoire du véhicule une fois calculée
	private Stack<Road> path;
	private Stack<Road> tempPath;
	// La trajectoire du véhicule est-elle calculée ?
	private boolean pathCalculated = false;
	// évènements calculés à partir de Dijkstra
	private PriorityQueue events = new PriorityQueue();
	private PriorityQueue tempEvents = new PriorityQueue();
	
	/**
	 * Constructeur à partir des information de localisation
	 * des points de départ et de destination du véhicule.
	 * @param location
	 */
	public AbstractVehicle(Location location)
	{
		this.identifier = identificator;
		identificator++;
		this.location = location;
	}
	
	/**
	 * Setter de la trajectoire du véhicule une fois calculée.
	 * @param path
	 */
	public void savePath(Stack<Road> path)
	{
		this.pathCalculated = true;
		this.setPath(path);
	}
	
	/**
	 * Crée une copie superficielle de l'itinéraire du véhicule.
	 * @return copie de path
	 */
	private Stack<Road> getPathCopy()
	{
		Stack<Road> path = new Stack<Road>();
		for(Road r : this.getPath())
		{
			path.add(r);
		}
		return path;
	}
	
	/**
	 * Initialise tempPath avec une copie superficielle de l'itinéraire path du véhicule.
	 */
	public void setTempPath()
	{
		this.tempPath = getPathCopy();
	}
	
	/**
	 * Crée une copie superficielle de la liste des évènements du véhicule.
	 * @return copie de events
	 */
	public PriorityQueue getEventsCopy()
	{
		return getEvents().getCopy();
	}
	
	/**
	 * Initialise tempEvents avec une copie superficielle
	 * de la liste des évènements du véhicule.
	 */
	public void setTempEvents()
	{
		tempEvents = getEventsCopy();
	}
	
	/**
	 * Affiche en détail l'itinéraire du véhicule.
	 */
	public void printPath()
	{
		Stack<Road> path = getPathCopy();
		while(!path.isEmpty())
		{
			Road road = path.pop();
			
			if(getLocation().initialRoad.getIdentifier()==getLocation().finalRoad.getIdentifier()
					& getLocation().initialPosition>getLocation().finalPosition)
			{
				System.out.print("Road "+road.getIdentifier()+" -> "+"Intersection "+road.getDestination().identifier+"; ");
			}
			if(road.equals(getLocation().finalRoad))
			{
				System.out.print("Road "+road.getIdentifier()+" -> ");
			}
			else
			{
				System.out.print("Road "+road.getIdentifier()+" -> "+"Intersection "+road.getDestination().identifier+"; ");
			}
		}
		System.out.println("end\n");
	}
	
	/**
	 * Setter propre des évènements du véhicule,
	 * à partir d'une pile calculée au sein
	 * de la méthode statique AbstractEvent.vehicleEvents.
	 * @param tempEvents
	 */
	public void setEvents(Stack<AbstractEvent> tempEvents)
	{
		while(!tempEvents.isEmpty())
		{
			events.qadd(tempEvents.pop());
		}
	}
	
	/**
	 * Compare les coûts des plus courts chemins de deux véhicules
	 * s'ils étaient calculés indépendamment,
	 * avec priorité pour le premier véhicule.
	 * @param graph
	 * @param a
	 * @param b
	 * @return véhicule dont la trajectoire optimale est moins coûteuse
	 */
	public static boolean inferiorPath(Graph graph, AbstractVehicle a, AbstractVehicle b)
	{
		Cost aPath = graph.getCost(a.intersectionBeforeEnd().identifier, a.intersectionAfterStart().identifier);
		Cost bPath = graph.getCost(b.intersectionBeforeEnd().identifier, b.intersectionAfterStart().identifier);
		return Cost.inferior(aPath, bPath);
	}
	
	/**
	 * Calcule et met à jour la position courante de la voiture à la date Time.time.
	 */
	public void setCurrentPosition()
	{
		double duration;
		double distance;
		AbstractVehicle[] waitingVehicles;
		int c=0;
		
		// la voiture continue à avancer
		if(getLocation().lastEventNature==0 | getLocation().lastEventNature==3)
		{
			duration = Time.time-getLocation().currentDate;
			distance = Time.distance(getLocation().currentRoad, duration);
			getLocation().currentPosition += distance;
		}
		if(getLocation().lastEventNature==1)
		{
			waitingVehicles = new AbstractVehicle[getLocation().currentRoad.getWaitingVehicles().size()];
			for(AbstractVehicle v : getLocation().currentRoad.getWaitingVehicles())
			{
				waitingVehicles[c] = v;
				c++;
			}
			c=waitingVehicles.length-1;
			distance=0;
			while(!waitingVehicles[c].equals(this))
			{
				if(c<0)
				{
					throw new IllegalStateException("La liste des véhicules vehiclesWaitingOnRoad est corrompue.");
				}
				distance += waitingVehicles[c].getLength()+AbstractVehicle.getMinSpaceBetweenVehicles();
				c--;
			}
			getLocation().currentPosition-=distance;
			
		}
		if(getLocation().lastEventNature==2)
		{
			getLocation().onIntersection = true;
		}
	}
	
	/**
	 * L'hypothèse est faite que la voiture
	 * ne peut se trouver dans une intersection à l'arrêt,
	 * et par conséquent à son départ.
	 * @return prochaine intersection après la position de départ du véhicule
	 */
	public AbstractIntersection intersectionAfterStart()
	{
		return getLocation().nextIntersection();
	}
	
	/**
	 * L'hypothèse est faite que la voiture
	 * ne peut se trouver dans une intersection à l'arrêt,
	 * et par conséquent à son départ.
	 * @return intersection précédent après la destination du véhicule
	 */
	public AbstractIntersection intersectionBeforeEnd()
	{
		return getLocation().finalIntersection();
	}
	
	/**
	 * Renvoie le véhicule dont la trajectoire optimale,
	 * calculée indépendamment des autres véhicules,
	 * présente le coût le moins important.
	 * @param graph
	 * @param vehicles à traiter
	 * @return véhicule dont la destination est la moins coûteuse
	 */
	public static AbstractVehicle lessPriorityVehicle(Graph graph, Stack<AbstractVehicle> vehicles)
	{
		AbstractVehicle lessPriorityVehicle = vehicles.get(0);
		for(AbstractVehicle v : vehicles)
		{
			if(inferiorPath(graph, v, lessPriorityVehicle))
			{
				lessPriorityVehicle = v;
			}
		}
		// suppression du véhicule traité des véhicules à traiter
		vehicles.remove(lessPriorityVehicle);
		return lessPriorityVehicle;
	}
	
	
	public String toString(){
		String result = "Véhicule " + this.getIdentifier() + " : ";
		result += this.getLocation().toString();
		return result;
	}

	
	// *** Getters ***
	
	public int getIdentifier() {
		return identifier;
	}

	public Location getLocation() {
		return location;
	}

	public double getLength() {
		return length;
	}

	public static double getMinSpaceBetweenVehicles() {
		return minSpaceBetweenVehicles;
	}

	public Stack<Road> getPath() {
		return path;
	}

	public void setPath(Stack<Road> path) {
		this.path = path;
	}

	public Stack<Road> getTempPath() {
		return tempPath;
	}

	public boolean getPathCalculated() {
		return pathCalculated;
	}
	
	public void isPathCalculated() {
		pathCalculated = true;
	}

	public PriorityQueue getEvents() {
		return events;
	}

	public PriorityQueue getTempEvents() {
		return tempEvents;
	}

	public static void initializeIdentificator()
	{
		identificator = 0;
	}
	
}
