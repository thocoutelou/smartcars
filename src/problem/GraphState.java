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

	// file de priorité (en fonction des dates) des évènements à survenir dans le graphe
	private PriorityQueue allEvents = new PriorityQueue();
	
	/**
	 * Constructeur unique, ne doit être appelé que par le parser.
	 * @param intersections
	 * @param roads
	 * @param vehicles
	 * @param events
	 */
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
	
	/**
	 * Place dans l'attribut tempEvents de chaque véhicule
	 * une copie de l'attribut events du même véhicule.
	 */
	public void setVehiclesTempEvents()
	{
		for(AbstractVehicle vehicle : getVehicles())
		{
			vehicle.setTempEvents();
		}
	}
	
	/**
	 * Crée une copie superficielle de la liste des évènements du graphe.
	 * @return copie des events
	 */
	public PriorityQueue getAllEventsCopy()
	{
		PriorityQueue eventsCopy = new PriorityQueue();
		eventsCopy.qaddAll(allEvents);
		return eventsCopy;
	}
	
	/**
	 * Calcule les itinéraires de tous les véhicules,
	 * avec priorité pour les véhicules à trajets longs.
	 * Cette priorité est importante puisque les véhicules
	 * dont l'itinéraire est calculé le plus tardivement
	 * seront ceux qui seront les plus affectés
	 * par les itinéraires des autres voitures
	 * (ceux qui ont déjà été calculés).
	 */
	public void calculatePaths()
	{
		stackVehicles(getVehicles());
		Stack<AbstractVehicle> vehiclesCopy = new Stack<AbstractVehicle>();
		AbstractVehicle vehicle;
		// copie la pile des véhicules déjà rangés dans l'ordre
		// consistant à prioriser les véhicules d'itinéraires les plus longs
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
	
	/**
	 * Réunie les évènements de tous les véhicules
	 * en les insérant dans la liste de priorité
	 * allEvents de l'objet GraphState.
	 */
	public void gatherEvents()
	{
		for(AbstractVehicle vehicle : getVehicles())
		{
			AbstractEvent.vehicleEvents(vehicle);
			this.allEvents.qaddAll(vehicle.getEvents());
		}
		System.out.println("\nActualisation de l'état du graphe :\n"+this.allEvents+"\n\n");
	}
	
	/**
	 * Parse l'image numéro 'map' du dossier 'media/exemple/'.
	 * @param map
	 * @return graphe parsé
	 */
	public static GraphState parse(int map)
	{
		String projectLocation = SvgParser.getProjectLocation();
		String mapLocation = new String(projectLocation+"/media/exemple/"+map+".svg");
		return SvgParser.parseGraphState(mapLocation);
	}
	
	/**
	 * Effectue tous les calculs menant à la résolution du problème,
	 * en partant du principe que le graphe est correctement initialisée.
	 */
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
	
	/**
	 * Actualise la localisation courante de toutes les voitures à la date Time.time.
	 * Cette méthode part donc du principe que Time.time est déjà modifié à la date souhaitée.
	 */
	public synchronized void setCurrentLocations(double time)
	{
		Time.time = time;
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
				AbstractEvent.executeEvent(event);
			}
			else break;
		}
		
		setCurrentPositions();
		printCurrentPositions();
	}
	
	/**
	 * Calcule et met à jour les positions courantes de toutes les voitures à la date Time.time.
	 */
	public void setCurrentPositions()
	{
		for(AbstractVehicle vehicle : getVehicles())
		{
			vehicle.setCurrentPosition();
		}
	}
	
	/**
	 * Affiche les positions courantes des véhicules.
	 */
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
	
	/**
	 * Calcule une vidéo des itinéraires des voitures
	 * en vue de les afficher sur la carte.
	 */
	public void video(double increment)
	{
		double time = 0.;
		double end = Time.endingTime(this);
		PriorityQueue events = getAllEventsCopy();
		while(time<=end)
		{
			while(events.qelement().getDate()<=time)
			{
				AbstractEvent.executeEvent(events.qremove());
			}
			Time.time = time;
			setCurrentPositions();
			time+=increment;
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

	
	// *** Getters ***
	
	public Stack<AbstractVehicle> getVehicles() {
		return vehicles;
	}

}
