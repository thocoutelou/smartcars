package smartCars;

public class Cost {
	
	// coût absolu de la route
	private double absoluteCost;
	// Le coût est-il fini ?
	// Sinon, absoluteCost ne sera pas significatif.
	private boolean finite;
	
	// constructeur d'un coût infini
	public Cost()
	{
		finite = false;
	}
	
	// constructeur de coût fini
	public Cost(double cost)
	{
		this.absoluteCost = cost;
		this.finite = true;
	}
	
	/**
	 * Compare les coûts a et b,
	 * avec priorité pour le coût a.
	 * @param a
	 * @param b
	 * @return Cost a est-il inférieur ou égal à Cost b ?
	 */
	public static boolean inferior(Cost a, Cost b)
	{
		if(!b.finite) return true;
		else if(!a.finite) return false;
		else if(a.absoluteCost<=b.absoluteCost) return true;
		else return false;
	}
	
	/**
	 * Renvoie le coût le plus faible,
	 * avec priorité pour le Cost a.
	 * @param a
	 * @param b
	 * @return coût minimal
	 */
	public static Cost minimum(Cost a, Cost b)
	{
		if(inferior(a, b)) return a;
		else return b;
	}
	
	/**
	 * Renvoie le coût le plus important,
	 * avec priorité pour le coût b.
	 * @param a
	 * @param b
	 * @return coût maximal
	 */
	public static Cost maximum(Cost a, Cost b)
	{
		if((minimum(a, b)).equals(a)) return b;
		else return a;
	}

	/**
	 * Calcule la somme de deux coûts.
	 * @param a
	 * @param b
	 * @return a+b
	 */
	public static Cost sum(Cost a, Cost b)
	{
		if(!a.finite | !b.finite) return new Cost();
		else return new Cost(a.absoluteCost+b.absoluteCost);
	}
	
	/**
	 * Calcule la somme du coût d'une route
	 * et de son intersection d'arrivée.
	 * Permet de prendre en compte les coûts de traversée des intersection
	 * lors du calcul des itinéraires (Dijkstra).
	 * @param road
	 * @return somme du coût de la route et de son intersection d'arrivée
	 */
	public static Cost intersection(Road road)
	{
		return sum(road.cost, new Cost(road.destination.averageTime));
	}
	
	/**
	 * Calcule la matrice de Floyd-Warshall d'un graphe.
	 * L'idée ici est ici de savoir quelle voiture traiter en priorité,
	 * à partir seulement de l'origine et de l'arrivée de leurs parcours :
	 * il est plus juste de rallonger l'itinéraire d'une voiture de plus court trajet.
	 * @param graph
	 * @return matrice de Floyd-Warshall du graphe
	 */
	public static Cost[][] floydWarshall(Graph graph)
	{
		System.out.println("Nombre d'intersections: "+graph.numberOfIntersections);
		Cost costsMatrix[][] = new Cost[graph.numberOfIntersections][graph.numberOfIntersections];
		for(int i=0; i<graph.numberOfIntersections; i++)
		{
			for(int j=0; j<graph.numberOfIntersections; j++)
			{
				costsMatrix[j][i] = new Cost();
			}
		}
		for(int i = 0; i < graph.roads.size(); i ++)
		{
			costsMatrix[graph.roads.get(i).destination.identifier][graph.roads.get(i).origin.identifier] = graph.roads.get(i).cost;
		}
		for(int k=0; k<graph.numberOfIntersections; k++)
		{
			for(int i=0; i<graph.numberOfIntersections; i++)
			{
				for(int j=0; j<graph.numberOfIntersections; j++)
				{
					costsMatrix[j][i] = minimum(costsMatrix[j][i], sum(costsMatrix[k][i], costsMatrix[j][k]));
				}
			}
		}
		return costsMatrix;
	}
	
}
