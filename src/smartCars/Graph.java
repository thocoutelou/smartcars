package smartCars;

public class Graph {
	
	public static Location startDefault;
	
	/*The graph is represented by an array of Intersection, and each of those contains a Road
	 * When the intersection numbered "i" have an outgoing Road, the i case of graph contains an array in which the Road is.
	 * The Road itself have the information of the destination
	 */
	private AbstractIntersection[] intersections;

	public Graph(AbstractIntersection[] intersections) {
		this.intersections = intersections;
	}


}
