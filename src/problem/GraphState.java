package problem;

import java.util.ArrayList;
import java.util.Stack;

import events.AbstractEvent;
import events.EventEnterRoad;
import events.EventLeaveRoad;
import events.EventVehicleEnd;
import events.EventVehicleStart;
import events.EventWaitingOnRoad;
import graph.AbstractIntersection;
import graph.Dijkstra;
import graph.Graph;
import graph.Road;
import resources.PriorityQueue;
import resources.Time;
import smartcars.AbstractVehicle;

/**
 * Composée d'un graphe, de véhicules et des évènements en devenir,
 * constitue le problème à résoudre.
 * @author thomas
 *
 */
public class GraphState extends Graph {
	
	// véhicules présent sur la carte
	private Stack<AbstractVehicle> vehicles;
	
	// file (FIFO en fonction des dates) des évènements à survenir dans le graphe
	private PriorityQueue allEvents = new PriorityQueue();
	
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
		for(AbstractVehicle vehicle : getVehicles())
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
		stackVehicles(getVehicles());
		Stack<AbstractVehicle> vehiclesCopy = new Stack<AbstractVehicle>();
		AbstractVehicle vehicle;
		for(AbstractVehicle v : getVehicles())
		{
			vehiclesCopy.add(v);
		}
		while(!vehiclesCopy.isEmpty())
		{
			vehicle = vehiclesCopy.pop();
			Dijkstra.calculatePath(this, vehicle);
			vehicle.isPathCalculated();
			vehicle.printPath();
		}
	}
	
	public void gatherEvents()
	{
		for(AbstractVehicle vehicle : getVehicles())
		{
			AbstractEvent.vehicleEvents(vehicle);
			this.allEvents.qaddAll(vehicle.getEvents());
		}
		System.out.println("\nActualisation de l'état du graphe :\n"+this.allEvents+"\n\n");
	}
	
	public void resolve()
	{
		calculatePaths();
		gatherEvents();
		Time.realDates(this);
		
		for(AbstractVehicle v : getVehicles())
		{
			System.out.println(v);
			System.out.println(v.getEvents());
		}
		System.out.println("Liste des évènements du graphe :");
		System.out.println(allEvents);
	}
	
	// part du principe que Time.time est modifié à la date souhaitée
	public synchronized void setCurrentLocations()
	{
		setVehiclesTempEvents();
		PriorityQueue eventsCopy = getAllEventsCopy();
		AbstractEvent event;
		AbstractEvent vehicleEvent;
		while(!eventsCopy.qisEmpty())
		{
			event = eventsCopy.qremove();
			vehicleEvent = event.getVehicle().getTempEvents().qremove();
			if(!event.equals(vehicleEvent))
			{
				throw new IllegalStateException("Les évènements ne sont pas retournés dans l'ordre.");
			}
			if(event.getDate()<=Time.time)
			{
				if(event.getNature()==0)
				{
					EventVehicleStart.executeEvent(event);
				}
				if(event.getNature()==1)
				{
					EventWaitingOnRoad.executeEvent(event);
				}
				if(event.getNature()==2)
				{
					EventLeaveRoad.executeEvent(event);
				}
				if(event.getNature()==3)
				{
					EventEnterRoad.executeEvent(event);
				}
				if(event.getNature()==4)
				{
					EventVehicleEnd.executeEvent(event);
				}
			}
			else break;
		}
		
		setCurrentPositions();
		printCurrentPositions();
	}
	
	public void setCurrentPositions()
	{
		for(AbstractVehicle vehicle : getVehicles())
		{
			vehicle.setCurrentPosition();
		}
	}
	
	public void printCurrentPositions()
	{
		System.out.println("\nPositions des véhicules à la date "+Time.time+"s :");
		for(AbstractVehicle v : getVehicles())
		{
			System.out.print("Véhicule "+v.getIdentifier()+" : ");
			if(v.getLocation().onIntersection)
			{
				System.out.println(v.getLocation().currentRoad.getDestination());
			}
			else
			{
				System.out.println(v.getLocation().currentRoad+"; "+"position "+v.getLocation().currentPosition+"m");
			}
		}
	}
	
	public String toString(){
		String newline = System.getProperty("line.separator");
		String result = super.toString() + newline;
		for(AbstractVehicle v: getVehicles()){
			result += v.toString() + newline;
		}
		return result;
	}

	public Stack<AbstractVehicle> getVehicles() {
		return vehicles;
	}

}
