package smartCars;

import java.util.Stack;

public class Graph {
	
	
	/* The graph is represented by an array of Intersection, and each of those contains a Road
	 * When the intersection numbered "i" have an outgoing Road, the i case of graph contains an array in which the Road is.
	 * The Road itself have the information of the destination
	 */
	private AbstractIntersection[] intersections;
	public static Location startDefault;
	

	public Graph(AbstractIntersection[] intersections, Location startDefault)
	{
		this.intersections = intersections;
		this.startDefault = startDefault;
	}
	
	//TODO contructeur à partir d'un image vectorielle
	//TODO définir les normes de l'image (notamment les couleurs)
	public Graph() {}

	
	public void djikstra(AbstractVehicle vehicle)
	{
		AbstractIntersection origin;
		AbstractIntersection[] nextIntersections = {};
		
	}
	
	

}
