package smartCars;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Composée d'un graphe, de véhicules et des évènements en devenir,
 * constitue le problème à résoudre.
 * @author thomas
 *
 */
public class GraphState extends Graph {
	
	// véhicules présent sur la carte
	public Stack<AbstractVehicle> vehicles;
	
	// file (FIFO en fonction des dates) des évènements à survenir dans le graphe
	public PriorityQueue events = new PriorityQueue();
	
	/**
	 * Constructeur unique, ne doit être appelé que par le parser.
	 * @param intersections
	 * @param roads
	 * @param vehicles
	 * @param events
	 */
	// this.events sera peut-être initialisé plus tard,
	// sachant que cette pile est le résultat de tous les calculs.
	public GraphState(ArrayList<AbstractIntersection> intersections, ArrayList<Road> roads,
			Stack<AbstractVehicle> vehicles) {
		super(intersections, roads);
		this.vehicles = vehicles;
	}
	
	/**
	 * Forme la pile des véhicules dans leur ordre de priorité de traitement :
	 * les véhicules d'itinéraires les plus longs sont prioritaires.
	 * @param vehiclesInGraph
	 */
	public synchronized void stackVehicles(Stack<AbstractVehicle> vehiclesInGraph)
	{
		Stack<AbstractVehicle> vehicles = new Stack<AbstractVehicle>();
		while(!vehiclesInGraph.isEmpty())
		{
			vehicles.add(AbstractVehicle.lessPriorityVehicle(this, vehiclesInGraph));
		}
		this.vehicles = vehicles;
	}
	
	// Version 1, sans priorité pour les véhicules
	public void calculatePaths()
	{
		stackVehicles(vehicles);
		Stack<AbstractVehicle> vehiclesCopy = new Stack<AbstractVehicle>();
		AbstractVehicle vehicle;
		for(AbstractVehicle v : vehicles)
		{
			vehiclesCopy.add(v);
		}
		while(!vehiclesCopy.isEmpty())
		{
			vehicle = vehiclesCopy.pop();
			Dijkstra.calculatePath(this, vehicle);
			vehicle.printPath();
		}
	}
	
	public void gatherEvents()
	{
		for(AbstractVehicle vehicle : vehicles)
		{
			AbstractEvent.vehicleEvents(vehicle);
			this.events.qaddAll(vehicle.events);
		}
		System.out.println("\nActualisation de l'état du graphe :\n"+this.events+"\n\n");
	}
	
	public void resolve()
	{
		calculatePaths();
		gatherEvents();
		Time.realDates(this);
		
		for(AbstractVehicle v : vehicles)
		{
			System.out.println(v);
			System.out.println(v.events);
		}
		System.out.println("Liste des évènements du graphe :");
		System.out.println(events);
	}
	
	public String toString(){
		String newline = System.getProperty("line.separator");
		String result = super.toString() + newline;
		for(AbstractVehicle v: vehicles){
			result += v.toString() + newline;
		}
		return result;
	}
}
