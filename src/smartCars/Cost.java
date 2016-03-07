package smartCars;

public class Cost {
	
	private double absoluteCost;
	private boolean finite;
	
	public Cost()
	{
		finite = false;
	}
	
	public Cost(double cost)
	{
		this.absoluteCost = cost;
		this.finite = true;
	}
	
	public static boolean inferior(Cost a, Cost b)
	{
		if(!b.finite) return true;
		else if(!a.finite) return false;
		else if(a.absoluteCost<=b.absoluteCost) return true;
		else return false;
	}
	
	/**
	 * Renvoie le Cost le plus faible.
	 * @param Cost a: premier Cost à comparer à b, qui sera prioritaire
	 * @param Cost b: deuxième Cost à comparer à a
	 * @return Cost minimal
	 */
	public static Cost minimum(Cost a, Cost b)
	{
		if(inferior(a, b)) return a;
		else return b;
	}
	
	public static Cost maximum(Cost a, Cost b)
	{
		if((minimum(a, b)).equals(a)) return b;
		else return a;
	}

	
	public static Cost sum(Cost a, Cost b)
	{
		if(!a.finite | !b.finite) return new Cost();
		else return new Cost(a.absoluteCost+b.absoluteCost);
	}
	

	public static Cost intersection(Road r)
	{
		return sum(r.cost, new Cost(r.destination.averageTime));
	}
	
	
	// L'idée ici est de construire la matrice de Floyd-Warshall du graphe
	// afin de savoir quelle voiture traiter en priorité,
	// à partir seulement de l'origine et de l'arrivée de son parcours :
	// il est plus juste de rallonger l'itinéraire d'une voiture de plus court trajet.
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
