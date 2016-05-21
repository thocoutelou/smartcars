package graph;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import events.AbstractEvent;
import resources.CartesianCoordinate;
import resources.Cost;
import resources.PriorityQueue;
import smartcars.AbstractVehicle;

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
	private final int identifier;
	// coût de traversé de la route
	// indépendant du coût de traversée des intersections
	private Cost cost;
	// nombre de voies, utile pour une éventuelle méthode 'doubler'
	@SuppressWarnings("unused")
	private int lane;
	// longueur absolue de la route en mètres
	private double absoluteLength;
	
	// la longueur disponible de la route sera variable,
	// car doit prendre en compte les véhicules en attente
	// de traversée de la prochaine intersection
	private double length;
	// vitesse de circulation autorisée (km/h)
	// et vitesse supposée des véhicules sur la route
	private double speed;
	// intersection d'origine de la route
	private final AbstractIntersection origin;
	// intersection d'arrivée de la route
	private final AbstractIntersection destination;
	// durée moyenne d'attente devant l'intersection d'arrivée
	private double averageWaitingTime;
	
	// géométrie de la route:
	// point de départ
	private final CartesianCoordinate point1;
	// point d'arrivée
	private final CartesianCoordinate point2;
	
	// véhicules sur la route
	private Queue<AbstractVehicle> vehiclesOnRoad = new LinkedList<AbstractVehicle>();
	// dont véhicules en attente de sortie de la route
	private Queue<AbstractVehicle> waitingVehicles = new LinkedList<AbstractVehicle>();
	// évènements implémentant les attentes de traversée de la prochaine intersection
	private PriorityQueue eventsWaitingOnRoad = new PriorityQueue();
	
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
		this.absoluteLength = this.getPoint1().distanceFrom(point2);
		this.length = this.getAbsoluteLength();
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
		Cost minCost = Cost.minimum(a.getCost(), b.getCost());
		if(minCost.equals(a.getCost())) return a;
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
			length += eWORArray[i].getVehicle().getLength()+AbstractVehicle.getMinSpaceBetweenVehicles();
		}
		return length;
	}

	/**
	 * Calcule la longueur courante de la file des voitures
	 * en attente de traversée de l'intersection suivante.
	 * @return longueur totale de la file d'attente
	 */
	public double lengthWaiting()
	{
		return lengthWaiting(0);
	}
	
	/**
	 * Calcule la file des 'first' premières voitures
	 * dans la file des voitures en attente de traversée de l'intersection suivante.
	 * @param first
	 * @return longueur de la file des first premières voitures
	 */
	public double lengthFirstWaiting(int first)
	{
		AbstractEvent[] eWORArray = eventsWaitingOnRoadToArray();
		double length = 0.;
		for(int i=0; i<first; i++)
		{
			length += eWORArray[i].getVehicle().getLength()+AbstractVehicle.getMinSpaceBetweenVehicles();
		}
		return length;
	}

	/**
	 * Crée une copie superficielle de la liste eventsWaitingOnRoad ;
	 * les évènements à l'intérieur de cette liste ne sont pas copiés
	 * mais sont les instances originales des évènements présents dans la liste de départ.
	 * @return copie des eventsWaitingOnRoad
	 */
	public PriorityQueue eventsWaitingOnRoadCopy()
	{
		PriorityQueue eWORCopy = new PriorityQueue();
		eWORCopy.qaddAll(eventsWaitingOnRoad);
		return eWORCopy;
	}
	
	/**
	 * Transforme la liste des eventsWaitingOnRoad en un tableau.
	 * @return tableau des eventsWaitingOnRoad
	 */
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
	 * Diminue la longueur disponible de la route d'une certaine distance.
	 * @param distance
	 * @return La longueur de la route a-t-elle pu être diminuée ?
	 */
	public boolean decreaseLength(double distance)
	{
		// Marge d'erreur de 1 dm
		if(getLength()+0.1<distance)
		{
			return false;
		}
		else
		{
			if(getLength()<distance) length=0.;
			else length -= distance;
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
		if(getLength()+distance>getAbsoluteLength()+0.1)
		{
			return false;
		}
		else
		{
			if(getLength()+distance>getAbsoluteLength()) length=getAbsoluteLength();
			else length+=distance;
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
		if(!decreaseLength(vehicle.getLength()+AbstractVehicle.getMinSpaceBetweenVehicles()))
		{
			throw new IllegalStateException("La saturation de la route "+getIdentifier()+" obstrue l'intersection "+vehicle.getLocation().currentRoad.getOrigin());
		}
		else
		{
			getWaitingVehicles().add(vehicle);
			vehicle.getLocation().waitingForIntersection = true;
		}
	}
	
	
	/**
	 * Un véhicule précédemment en attente
	 * peut à présent traverser l'intersection suivante.
	 * @throws IllegalStateException
	 */
	public void formerWaitingVehicle(AbstractVehicle vehicle) throws IllegalStateException
	{
		AbstractVehicle leavingVehicle = getWaitingVehicles().remove();
		vehicle.getLocation().waitingForIntersection = false;
		if(!vehicle.equals(leavingVehicle))
		{
			throw new IllegalStateException("La liste des eventsWaitingOnRoad est corrompue.");
		}
		increaseLength(leavingVehicle.getLength()+AbstractVehicle.getMinSpaceBetweenVehicles());
		if(!vehiclesOnRoad.remove(leavingVehicle))
		{
			throw new IllegalStateException("Les files de voitures présentes et en attente ne correspondent pas");
		}
	}
	
	/**
	 * Associe à un point M le point le plus proche qui appartient au segment Road.
	 * @param pointM
	 * @return point le plus proche appartenant à une route
	 */
	public CartesianCoordinate coordinateProjection(CartesianCoordinate pointM){
		double pente = (getPoint2().y - getPoint1().y) / (getPoint2().x - getPoint1().x);
		double k = (pente*(getPoint1().x-pointM.x) + pointM.y - getPoint1().y) / (1 + pente*pente);
		CartesianCoordinate pointH = new CartesianCoordinate(pointM.x + k*pente, pointM.y-k);
		if(pointH.x>getPoint2().x){
			return getPoint2();
		} else if (pointH.x<getPoint1().x){
			return getPoint1();
		} else {
			return pointM;
		}
	}

	public CartesianCoordinate getPositionCartesianCoordinate(double position){
		double x_unit = (getPoint2().x-getPoint1().x)/getLength();
		double y_unit = (getPoint2().y-getPoint1().y)/getLength();
		double x = getPoint1().x + position*x_unit;
		double y = getPoint1().y + position*y_unit;
		return new CartesianCoordinate(x,y);
	}

	/**
	 * Identification de la route.
	 */
	public String toString(){
		return "Road " + this.getIdentifier();
	}
	
	/**
	 * Identification détaillée de la route.
	 */
	public String toStringDetailed(){
		return  "Road " + this.getIdentifier() + " origin=" + this.getOrigin() + " destination=" + this.getDestination();
	}

	
	// *** Getters ***
	
	public int getIdentifier() {
		return identifier;
	}

	public Cost getCost() {
		return cost;
	}

	public double getAbsoluteLength() {
		return absoluteLength;
	}

	public double getLength() {
		return length;
	}
	public double getSpeed() {
		return speed;
	}

	public AbstractIntersection getOrigin() {
		return origin;
	}

	public AbstractIntersection getDestination() {
		return destination;
	}

	public double getAverageWaitingTime() {
		return averageWaitingTime;
	}

	public CartesianCoordinate getPoint1() {
		return point1;
	}

	public CartesianCoordinate getPoint2() {
		return point2;
	}
	
	public Queue<AbstractVehicle> getVehiclesOnRoad() {
		return vehiclesOnRoad;
	}
	
	public void removeVehiclesOnRoad(AbstractVehicle vehicle) {
		// test à effet de bord
		if(!vehiclesOnRoad.remove(vehicle))
		{
			throw new IllegalStateException("La liste vehiclesOnRoad est corrompue.");
		}
	}

	public Queue<AbstractVehicle> getWaitingVehicles() {
		return waitingVehicles;
	}
	
	public PriorityQueue getEventsWaitingOnRoad() {
		return eventsWaitingOnRoad;
	}

	public static void initializeIdentificator()
	{
		identificator = 0;
	}
	
}
