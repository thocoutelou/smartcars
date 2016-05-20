package resources;

import graph.AbstractIntersection;
import graph.Graph;
import graph.Road;

/**
 * Représente un coût de traversée,
 * pour une route aussi bien que pour une intersection.
 * Permet de définir des coûts infinis.
 * @author cylla
 *
 */
public class Cost {
	
	// coût absolu de la route
	private double absoluteCost;
	// Le coût est-il fini ?
	// Sinon, absoluteCost ne sera pas significatif.
	private boolean finite;
	
	/**
	 * Constructeur d'un coût infini.
	 */
	public Cost()
	{
		finite = false;
	}
	
	/**
	 * Constructeur d'un coût fini.
	 * @param cost
	 */
	public Cost(double cost)
	{
		this.absoluteCost = cost;
		this.finite = true;
	}
	
	/**
	 * Getter du coût lorsque celui-ci est fini.
	 * @return
	 */
	public double getFiniteCost()
	{
		if(!finite)
		{
			throw new IllegalStateException("Le coût de cet itinéraire est infini.");
		}
		else return absoluteCost;
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

	public static boolean isInfinite(Cost a){
		return (! a.finite);
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
	 * Calcule la matrice de Floyd-Warshall d'un graphe.
	 * L'idée ici est ici de savoir quelle voiture traiter en priorité,
	 * à partir seulement de l'origine et de l'arrivée de leurs parcours :
	 * il est plus juste de rallonger l'itinéraire d'une voiture de plus court trajet.
	 * @param graph
	 * @return matrice de Floyd-Warshall du graphe
	 */
	public static Cost[][] floydWarshall(Graph graph)
	{
		int start = AbstractIntersection.getIdentificator()-graph.getIntersections().size();
		Road road;
		Cost costsMatrix[][] = new Cost[AbstractIntersection.getIdentificator()][AbstractIntersection.getIdentificator()];
		for(int i=start; i<AbstractIntersection.getIdentificator(); i++)
		{
			for(int j=start; j<AbstractIntersection.getIdentificator(); j++)
			{
				costsMatrix[j][i] = new Cost();
			}
		}
		for(int i=0; i<graph.getRoads().size(); i++)
		{
			road=graph.getRoads().get(i);
			costsMatrix[road.getDestination().identifier][road.getOrigin().identifier] = AbstractIntersection.intersectionCost(road);
		}
		for(int k=start; k<AbstractIntersection.getIdentificator(); k++)
		{
			for(int i=start; i<AbstractIntersection.getIdentificator(); i++)
			{
				for(int j=start; j<AbstractIntersection.getIdentificator(); j++)
				{
					costsMatrix[j][i] = minimum(costsMatrix[j][i], sum(costsMatrix[k][i], costsMatrix[j][k]));
				}
			}
		}
		return costsMatrix;
	}
	
}
