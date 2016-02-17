package smartCars;


import java.util.ArrayList;

/**
 * Le graphe est représenté par la liste de ses noeuds (intersections AbstractIntersection),
 * chacune d'entre elles contenant la liste de ses arêtes (routes Road) directement connectées.
 * L'hypothèse est faite que le graphe est FORTEMENT CONNEXE.
 */
public class Graph {
	
	// (intersections) définit le graphe même
	private ArrayList<AbstractIntersection> intersections;
	
	// (startDefault) définit un point de départ pour les AbstractVehicle instanciés sans précision
	public static Location startDefault;
	
	public static ArrayList<AbstractVehicle> vehiclesInGraph;
	

	public Graph(ArrayList<AbstractIntersection> intersections, Location startDefault, ArrayList<AbstractVehicle> vehicles)
	{
		this.intersections = intersections;
		Graph.startDefault = startDefault;
		Graph.vehiclesInGraph = vehicles;
	}
	
	
	//TODO contructeur à partir d'un image vectorielle
	//TODO définir les normes de l'image (notamment les couleurs)
	public Graph() {}

	public ArrayList<AbstractIntersection> getIntersections()
	{
		return intersections;
	}
	
}