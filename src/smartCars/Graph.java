package smartCars;

public class Graph {
	
	/*The graph is represented by an array of Intersection, and each of those contains a Road
	 * When the intersection numbered "i" have an outgoing Road, the i case of graph contains an array in which the Road is.
	 * The Road itself have the information of the destination
	 */
	private Intersection_Interface[] graph;

	public Graph(Intersection_Interface[] graph) {
		this.graph = graph;
	}


}
