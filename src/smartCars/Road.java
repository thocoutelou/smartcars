package smartCars;

import java.lang.IllegalStateException;
import java.util.Stack;

public class Road {
	
	private static int identificator = 0;
	public int identifier;
	private Cost cost;
	private int lane;
	private double absoluteLength;
	
	// La longueur de la route sera variable car égale à :
	// la longueur absolue de la route diminuée de la longueur de la file d'attente
	public double length;
	private AbstractIntersection origin;
	public AbstractIntersection destination;
	public Stack<AbstractVehicle> waitingVehicles = new Stack<>();
	
	// Constructeur utile pour ajouter facilement des routes
	public Road()
	{
		identifier = identificator;
		identificator++;
		cost = new Cost();
		lane = 1;
		// Une route fera 1 km par défaut (moyenne urbaine)
		absoluteLength = 1000.;
	}
	
	public Road(Cost cost)
	{
		identifier = identificator;
		identificator++;
		this.cost = new Cost();
		this.lane = 1;
		this.absoluteLength = 1000.;
	}
	
	public Road(Cost cost, int lane)
	{
		identifier = identificator;
		identificator++;
		this.cost = cost;
		this.lane = lane;
		this.absoluteLength = 1000.;
	}
	
	public Road(Cost cost, int lane, double absoluteLength)
	{
		identifier = identificator;
		identificator++;
		this.cost = cost;
		this.lane = lane;
		this.absoluteLength = absoluteLength;
	}
	
	//TODO
	private boolean decreaseLength(double distance)
	{
		if(length<distance)
		{
			return false;
		}
		
		//TODO avec une structure FIFO et non LIFO (stack est LIFO)
		else
		{
			length-=distance;
			return true;
		}
	}
	
	//TODO Finir dès que la strucure FIFO sera prête
	public void newWaitingVehicle(AbstractVehicle vehicle) throws IllegalStateException
	{
		// Test à effets de bord
		if(!decreaseLength(vehicle.size))
		{
			// Vive la programmation objet pour ses tirades de logique dure
			vehicle.currentLocation.road.destination.obstruction = true;
			throw new IllegalStateException("La saturation de la route "+identifier+" obstrue l'intersection "+vehicle.currentLocation.road.origin);
		}
	}
	
	
}
