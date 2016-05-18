package smartCars;

import java.util.ArrayList;

/**
 * Le graphe est représenté par la liste de ses noeuds (intersections AbstractIntersection),
 * chacun d'entre eux contenant la liste de ses arêtes (routes Road) sortantes..
 * L'hypothèse est faite que le graphe est connexe.
 * @author cylla
 */
public class Graph {
	
	// intersections du graphe (suffit à définir le graphe))
	public ArrayList<AbstractIntersection> intersections = new ArrayList<AbstractIntersection>();
	// routes du graphe
	public ArrayList<Road> roads = new ArrayList<Road>();
	// matrice de Floyd-Warshall en devenir du graphe, initialisée dans le constructeur
	public Cost[][] costsMatrix;

	/**
	 * constructeur temporaire, afin de visualiser les étapes de l'instanciation
	 * @param intersections
	 * @param roads
	 */
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
		for(AbstractIntersection i : intersections)
		{
			roads.addAll(i.getLeavingRoads());
		}
	}
	
	/**
	 * Effectue tous les calculs menant à la résolution du problème,
	 * en partant du principe que l'instance est correctement initialisée.
	 */
	// TODO Actualiser au fur et à mesure des tests.
	public final void resolve(int map)
	{
		String projectLocation = SvgParser.getProjectLocation();
		String mapLocation = new String(projectLocation+"/media/map/"+map+".svg");
		GraphState graph = SvgParser.parseGraphState(mapLocation);
		graph.calculatePaths();
		graph.gatherEvents();
		
	}
	
	/**
	 * Identification du graphe.
	 */
	public String toString() {
		String newline = System.getProperty("line.separator");
		String result = "\n\n<---   Graph print   --->" + newline;
		for (int i=0; i<intersections.size(); i++){
			result += intersections.get(i).toStringDetailed()+ newline;
		}
		return result;
	}
	
}