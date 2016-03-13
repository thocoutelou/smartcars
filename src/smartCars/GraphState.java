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
	public void stackVehicles(ArrayList<AbstractVehicle> vehiclesInGraph)
	{
		Stack<AbstractVehicle> vehicles = new Stack<AbstractVehicle>();
		while(!vehiclesInGraph.isEmpty())
		{
			vehicles.add(AbstractVehicle.lessPriorityVehicle(this, vehiclesInGraph));
		}
		this.vehicles = vehicles;
	}
	
	/**
	 * Effectue tous les calculs menant à la résolution du problème,
	 * en partant du principe que l'instance est correctement initialisée.
	 */
	//TODO
	public void resolve()
	{
		
	}
}