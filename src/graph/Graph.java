package graph;

import java.util.ArrayList;

import resources.Cost;

/**
 * Le graphe est représenté par la liste de ses noeuds (intersections AbstractIntersection),
 * chacun d'entre eux contenant la liste de ses arêtes (routes Road) sortantes..
 * L'hypothèse est faite que le graphe est connexe.
 * @author cylla
 */
public class Graph {
	
	// intersections du graphe (suffit à définir le graphe))
	private ArrayList<AbstractIntersection> intersections = new ArrayList<AbstractIntersection>();
	// routes du graphe
	private ArrayList<Road> roads = new ArrayList<Road>();
	// matrice de Floyd-Warshall en devenir du graphe, initialisée dans le constructeur
	private Cost[][] costsMatrix;

	
	public Graph(ArrayList<AbstractIntersection> intersections, ArrayList<Road> roads)
	{
		this.intersections = intersections;
		this.roads = roads;
		this.costsMatrix = Cost.floydWarshall(this);
		listRoads();
	}

	/**
	 * Forme la liste complète des routes du graphe.
	 */
	public void listRoads()
	{
		for(AbstractIntersection i : getIntersections())
		{
			roads.addAll(i.getLeavingRoads());
		}
	}
	
	/**
	 * Identification du graphe.
	 */
	public String toString() {
		String newline = System.getProperty("line.separator");
		String result = "\n\n<---   Graph print   --->" + newline;
		for (int i=0; i<getIntersections().size(); i++){
			result += getIntersections().get(i).toStringDetailed()+ newline;
		}
		return result;
	}
	
	
	// *** Getters ***
	
	public Cost getCost(int j, int i)
	{
		return costsMatrix[j][i];
	}
	
	public double getFiniteCost(int j, int i)
	{
		return costsMatrix[j][i].getFiniteCost();
	}
	
	public ArrayList<AbstractIntersection> getIntersections() {
		return intersections;
	}
	
	public ArrayList<Road> getRoads()
	{
		return roads;
	}
	
}