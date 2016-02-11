package smartCars;

import java.util.LinkedList;
import java.util.Queue;

public class Road {
	
	private static int identificator = 0;
	public int identifier;
	private Cost cost;
	public int lane;
	private double absoluteLength;
	
	// La longueur de la route sera variable car égale à :
	// la longueur absolue de la route diminuée de la longueur de la file d'attente
	public double length;
	private AbstractIntersection origin;
	public AbstractIntersection destination;
	
	// Queue: structure FIFO (first in first out)
	// Méthodes: isEmpty(), remove(), add()
	public Queue<AbstractVehicle> waitingVehicles = new LinkedList<AbstractVehicle>();
	
	// Construit une route de coût infini
	public Road(AbstractIntersection origin, AbstractIntersection destination)
	{
		identifier = identificator;
		identificator++;
		this.origin = origin;
		this.destination = destination;
		cost = new Cost();
		lane = 1;
		// Une route fera 1 km par défaut (moyenne urbaine)
		absoluteLength = 1000.;
	}
	
	public Road(AbstractIntersection origin, AbstractIntersection destination, Cost cost)
	{
		identifier = identificator;
		identificator++;
		this.origin = origin;
		this.destination = destination;
		this.cost = cost;
		lane = 1;
		absoluteLength = 1000.;
	}
	
	public Road(AbstractIntersection origin, AbstractIntersection destination, Cost cost, int lane)
	{
		identifier = identificator;
		identificator++;
		this.origin = origin;
		this.destination = destination;
		this.cost = cost;
		this.lane = lane;
		absoluteLength = 1000.;
	}
	
	// Beaucoup d'arguments pour un constructeur,
	// mais transparent pour l'utilisateur car ce constructeur
	// sera appelé par le constructeur du graphe à partir d'une image.
	public Road(AbstractIntersection origin, AbstractIntersection destination, Cost cost, int lane, double absoluteLength)
	{
		identifier = identificator;
		identificator++;
		this.origin = origin;
		this.destination = destination;
		this.cost = cost;
		this.lane = lane;
		this.absoluteLength = absoluteLength;
	}
	

	private boolean decreaseLength(double distance)
	{
		// Marge d'erreur de 1 dm
		if(0.1<distance-length)
		{
			return false;
		}
		else
		{
			length-=distance;
			return true;
		}
	}
	
	//TODO Modifier la condition logique
	private boolean increaseLength(double distance)
	{
		// Marge d'erreur de 1 dm
		if(0.1<absoluteLength-length-distance)
		{
			return false;
		}
		else
		{
			length+=distance;
			return true;
		}
	}
	
	
	public void newWaitingVehicle(AbstractVehicle vehicle) throws IllegalStateException
	{
		// Test à effets de bord
		if(!decreaseLength(vehicle.size+AbstractVehicle.minSpaceBetweenVehicles))
		{
			// Vive la programmation objet pour ses tirades de logique dure
			vehicle.vehicleLocation.road.destination.obstruction = true;
			throw new IllegalStateException("La saturation de la route "+identifier+" obstrue l'intersection "+vehicle.vehicleLocation.road.origin);
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
	
	//TODO Transférer le véhicule dans l'intersection (destination)
	public AbstractVehicle formerWaitingVehicle()
	{
		AbstractVehicle leavingVehicle = waitingVehicles.remove();
		increaseLength(leavingVehicle.size);
		return leavingVehicle;
	}
}
