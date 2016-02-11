package smartCars;


import java.util.Stack;

/**
 * Le graphe est représenté par la liste de ses noeuds (intersections AbstractIntersection),
 * chacune d'entre elles contenant la liste de ses arêtes (routes Road) directement connectées.
 * L'hypothèse est faite que le graphe est FORTEMENT CONNEXE.
 */
public class Graph {
	
	// (intersections) définit le graphe même
	private AbstractIntersection[] intersections;
	
	// (startDefault) définit un point de départ pour les AbstractVehicle instanciés sans précision
	public static Location startDefault;
	
	public static AbstractVehicle[] vehiclesInGraph = {};
	

	public Graph(AbstractIntersection[] intersections, Location startDefault, AbstractVehicle[] vehicles)
	{
		this.intersections = intersections;
		this.startDefault = startDefault;
		this.vehiclesInGraph = vehicles;
	}
	
	//TODO contructeur à partir d'un image vectorielle
	//TODO définir les normes de l'image (notamment les couleurs)
	public Graph() {}

	
	public void djikstra(AbstractVehicle vehicle)
	{
		// 
		AbstractIntersection origin = vehicle.intersectionAfterStart();

		// Stack: structure LIFO (last in first out), pour un parcours du graphe en profondeur
		// Méthodes: empty(), peek(), pop(), push(e), search(e)
		// Pile des voisins aux derniers noeuds traités...
		Stack<AbstractIntersection> nextIntersections = new Stack<AbstractIntersection>();
		// ... initialisée avec le noeud choisi :
		nextIntersections.add(origin);
		
	}
	
	
}
