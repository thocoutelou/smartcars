package smartCars;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Route du graphe.
 * Contient les intersections de départ et de fin.
 * @author cylla
 *
 */
public class Road {
	
	// identificateur des instances, s'incrémente à chaque instanciation...
	private static int identificator = 0;
	// ... pour définir l'identifiant de la route créée
	public final int identifier;
	// coût de traversé de la route
	// indépendant du coût de traversée des intersections
	public Cost cost;
	// nombre de voies
	public int lane;
	// longueur absolue de la route en mètres
	public double absoluteLength;
	
	// la longueur disponible de la route sera variable,
	// car doit prendre en compte les véhicules en attente
	// de traversée de la prochaine intersection
	public double length;
	// vitesse de circulation autorisée (km/h)
	// et vitesse supposée des véhicules sur la route
	public double speed;
	// intersection d'origine de la route
	public final AbstractIntersection origin;
	// intersection d'arrivée de la route
	public final AbstractIntersection destination;
	// durée moyenne d'attente devant l'intersection d'arrivée
	public double averageWaitingTime;
	
	// géométrie de la route:
	// point de départ
	public final CartesianCoordinate point1;
	// point d'arrivée
	public final CartesianCoordinate point2;
	
	// véhicules sur la route
	public Queue<AbstractVehicle> vehiclesOnRoad = new LinkedList<AbstractVehicle>();
	// dont véhicules en attente de sortie de la route
	public Queue<AbstractVehicle> waitingVehicles = new LinkedList<AbstractVehicle>();
	// évènements implémentant les attentes de traversée de la prochaine intersection
	public PriorityQueue eventsWaitingOnRoad = new PriorityQueue();
	
	/**
	 * constructeur unique, doit être utilisé seulement par le parser
	 * @param point1
	 * @param point2
	 * @param origin
	 * @param destination
	 * @param cost
	 * @param lane
	 */
	public Road(CartesianCoordinate point1, CartesianCoordinate point2, AbstractIntersection origin, AbstractIntersection destination, double speed, Cost cost, int lane, double averageWaitingTime)
	{
		identifier = identificator;
		identificator++;
		this.point1 = point1;
		this.point2 = point2;
		this.origin = origin;
		this.destination = destination;
		this.speed = speed;
		this.cost = cost;
		this.lane = lane;
		this.averageWaitingTime = averageWaitingTime;
		this.absoluteLength = this.point1.distanceFrom(point2);
		this.length = this.absoluteLength;
	}
	
	/**
	 * Renvoie la route de coût minimal,
	 * avec priorité pour la première route.
	 * @param a
	 * @param b
	 * @return Road de coût minimal
	 */
	public static Road minimum(Road a, Road b)
	{
		Cost minCost = Cost.minimum(a.cost, b.cost);
		if(minCost.equals(a.cost)) return a;
		else return b;
	}
	
	/**
	 * Renvoie la route de coût minimal,
	 * avec priorité selon l'index de la route dans la liste.
	 * @param roads
	 * @return Road de coût minimal
	 */
	public static Road minimum(ArrayList<Road> roads)
	{
		if(roads.isEmpty()) throw new IllegalArgumentException("Aucune route à explorer");
		else
		{
			Road min = roads.get(0);
			for(Road r : roads)
			{
				min = minimum(min, r);
			}
			return min;
		}
	}
	
	/**
	 * Calcule la longueur de la file des voitures
	 * en attente de traversée de l'intersection suivante,
	 * amputée d'un certain nombre (left) de véhicules
	 * ayant déjà quitté la route.
	 * A noter que la longueur retournée contient un espace supplémentaire
	 * correspondant à la distance entre le dernier véhicule de la file
	 * et un hypothétique nouvel arrivant.
	 * @param left nombre de voitures ayant quitté la file d'attente
	 * @return longueur de la file d'attente résultante
	 */
	public double lengthWaiting(int left)
	{
		AbstractEvent[] eWORArray = eventsWaitingOnRoadToArray();
		double length = 0.;
		for(int i=left; i<eWORArray.length; i++)
		{
			length += eWORArray[i].vehicle.length+AbstractVehicle.minSpaceBetweenVehicles;
		}
		return length;
	}
	
	public PriorityQueue eventsWaitingOnRoadCopy()
	{
		PriorityQueue eWORCopy = new PriorityQueue();
		eWORCopy.qaddAll(eventsWaitingOnRoad);
		return eWORCopy;
	}
	
	public AbstractEvent[] eventsWaitingOnRoadToArray()
	{
		PriorityQueue eWORCopy = eventsWaitingOnRoadCopy();
		AbstractEvent[] eWORArray = new AbstractEvent[eWORCopy.qsize()];
		int c = 0;
		while(!eWORCopy.qisEmpty())
		{
			eWORArray[c]=eWORCopy.aqremove();
			c++;
		}
		return eWORArray;
	}
	
	/**
	 * Calcule la longueur courante de la file des voitures
	 * en attente de traversée de l'intersection suivante.
	 * @return longueur de la file d'attente
	 */
	public double lengthWaiting()
	{
		return lengthWaiting(0);
	}

	/**
	 * Diminue la longueur disponible de la route d'une certaine distance.
	 * @param distance
	 * @return La longueur de la route a-t-elle pu être diminuée ?
	 */
	private boolean decreaseLength(double distance)
	{
		// Marge d'erreur de 1 dm
		if(length+0.1<distance)
		{
			return false;
		}
		else
		{
			length-=distance;
			return true;
		}
	}
	

	/**
	 * Augmente la longueur disponible de la route d'une certaine distance.
	 * @param distance
	 * @return La longueur de la route a-t-elle pu être augmentée ?
	 */
	private boolean increaseLength(double distance)
	{
		// Marge d'erreur de 1 dm
		if(length+distance+0.1<absoluteLength)
		{
			return false;
		}
		else
		{
			length+=distance;
			return true;
		}
	}
	
	
	/**
	 * Un nouveau véhicule attend pour traverser l'intersection suivante.
	 * @param vehicle
	 * @throws IllegalStateException
	 */
	public void newWaitingVehicle(AbstractVehicle vehicle) throws IllegalStateException
	{
		// Test à effets de bord
		if(!decreaseLength(vehicle.length+AbstractVehicle.minSpaceBetweenVehicles))
		{
			vehicle.location.currentRoad.destination.obstruction = true;
			throw new IllegalStateException("La saturation de la route "+identifier+" obstrue l'intersection "+vehicle.location.currentRoad.origin);
		}
		else
		{
			// Le premier véhicule à attendre
			// ne doit pas comptabiliser l'espace entre deux véhicules à l'arrêt
			if(waitingVehicles.isEmpty())
			{
				increaseLength(AbstractVehicle.minSpaceBetweenVehicles);
			}
			waitingVehicles.add(vehicle);
		}
	}
	
	
	/**
	 * Un véhicule précédemment en attente
	 * peut à présent traverser l'intersection suivante.
	 * @throws IllegalStateException
	 */
	public void formerWaitingVehicle() throws IllegalStateException
	{
		AbstractVehicle leavingVehicle = waitingVehicles.remove();
		increaseLength(leavingVehicle.length);
		if(!vehiclesOnRoad.contains(leavingVehicle))
		{
			throw new IllegalStateException("Les files de voitures présentes et en attente ne correspondent pas");
		}
		vehiclesOnRoad.remove(leavingVehicle);
		//destination.crossIntersection(leavingVehicle, leavingVehicle.route.element());
		Road nextRoad = leavingVehicle.path.pop();
		//TODO Modifier en détail la "location" du véhicule
		leavingVehicle.location.currentRoad = nextRoad;
		nextRoad.vehiclesOnRoad.add(leavingVehicle);
	}
	
	// Associe à un point M le point le plus proche qui appartient au segment Road
	public CartesianCoordinate coordinateProjection(CartesianCoordinate pointM){
		double pente = (point2.y - point1.y) / (point2.x - point1.x);
		double k = (pente*(point1.x-pointM.x) + pointM.y - point1.y) / (1 + pente*pente);
		CartesianCoordinate pointH = new CartesianCoordinate(pointM.x + k*pente, pointM.y-k);
		if(pointH.x>point2.x){
			return point2;
		} else if (pointH.x<point1.x){
			return point1;
		} else {
			return pointM;
		}
	}
	
	/**
	 * Identification de la route.
	 */
	public String toString(){
		return "Road " + this.identifier;
	}
	
	/**
	 * Identification détaillée de la route.
	 */
	public String toStringDetailed(){
		return  "Road " + this.identifier + " origin=" + this.origin + " destination=" + this.destination;
	}
	
}
