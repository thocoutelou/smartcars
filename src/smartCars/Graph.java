package smartCars;

import java.util.ArrayList;

/**
 * Le graphe est représenté par la liste de ses noeuds (intersections AbstractIntersection),
 * chacun d'entre eux contenant la liste de ses arêtes (routes Road) directement connectées.
 * L'hypothèse est faite que le graphe est connexe.
 */
public class Graph {
	
	// (intersections) définit le graphe même
	private ArrayList<AbstractIntersection> intersections = new ArrayList<AbstractIntersection>();
	public int numberOfIntersections = 0;
	public ArrayList<Road> roads = new ArrayList<Road>();
	public Cost[][] costsMatrix;

	public Graph(ArrayList<AbstractIntersection> intersections, ArrayList<Road> roads)
	{
		this.intersections = intersections;
		this.roads = roads;
		this.numberOfIntersections = intersections.size();
		//this.costsMatrix = Cost.floydWarshall(this); // Ne marche pas encore
		listRoads();
	}
	
	
		

	/**
	 * Forme la liste complète des routes du graphe
	 */
	public void listRoads()
	{
		for(AbstractIntersection i : intersections)
		{
			roads.addAll(i.getLeavingRoads());
		}
	}
	
	public String toString() {
		String newline = System.getProperty("line.separator");
		String result = "<---   Graph print   --->" + newline;
		for (int i=0; i<intersections.size(); i++){
			result += intersections.get(i).toStringDetailed()+ newline;
		}
		return result;
	}
	
}