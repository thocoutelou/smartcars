package smartCars;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Composée d'un graphe, de véhicules et des évènements en devenir,
 * constitue le problème à résoudre
 * @author thomas
 *
 */
public class GraphState extends Graph {
	
	/**
	 * Cette classe est composée d'un Graph, des véhicules et des évènements. Cela constitue
	 * un problème mathématique à résoudre (c'est à dire déterminer les itinéraires des voitures)
	 */
	public Stack<AbstractVehicle> vehicles;
	public ArrayList<Event> events;
	
	public GraphState(ArrayList<AbstractIntersection> intersections, ArrayList<Road> roads,
			Stack<AbstractVehicle> vehicles, ArrayList<Event> events) {
		super(intersections, roads);
		this.vehicles = vehicles;
		this.events = events;
	}
	
	/**
	 * Forme la pile des véhicules dans leur ordre de priorité de traitement :
	 * les véhicules d'itinéraires les plus longs sont prioritaires.
	 * @param vehiclesInGraph
	 */
	public void stackVehicles(ArrayList<AbstractVehicle> vehiclesInGraph)
	{
		Stack<AbstractVehicle> vehicles = new Stack<AbstractVehicle>();
		while(!vehiclesInGraph.isEmpty())
		{
			vehicles.add(AbstractVehicle.lessPriorityVehicle(this, vehiclesInGraph));
		}
		this.vehicles = vehicles;
	}
}
