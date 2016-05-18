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
	public PriorityQueue allEvents = new PriorityQueue();
	
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
	
	public void setVehiclesTempEvents()
	{
		for(AbstractVehicle vehicle : vehicles)
		{
			vehicle.setTempEvents();
		}
	}
	
	public PriorityQueue getAllEventsCopy()
	{
		PriorityQueue eventsCopy = new PriorityQueue();
		eventsCopy.qaddAll(allEvents);
		return eventsCopy;
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
			this.allEvents.qaddAll(vehicle.events);
		}
		System.out.println("\nActualisation de l'état du graphe :\n"+this.allEvents+"\n\n");
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
		System.out.println(allEvents);
	}
	
	// part du principe que Time.time est modifié à la date souhaitée
	public void setCurrentLocations()
	{
		setVehiclesTempEvents();
		PriorityQueue eventsCopy = getAllEventsCopy();
		AbstractEvent event = eventsCopy.qremove();;
		AbstractEvent vehicleEvent = event.vehicle.tempEvents.qremove();
		do
		{
			if(!event.equals(vehicleEvent))
			{
				throw new IllegalStateException("Les évènements ne sont pas retournés dans l'ordre.");
			}
			if(event.date<=Time.time)
			{
				if(event.nature==0)
				{
					EventVehicleStart.executeEvent(event);
				}
				if(event.nature==1)
				{
					EventWaitingOnRoad.executeEvent(event);
				}
				if(event.nature==2)
				{
					EventLeaveRoad.executeEvent(event);
				}
				if(event.nature==3)
				{
					EventEnterRoad.executeEvent(event);
				}
				if(event.nature==4)
				{
					EventVehicleEnd.executeEvent(event);
				}
			}
			else
			{
				break;
			}
		}
		while(!allEvents.qisEmpty());
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
