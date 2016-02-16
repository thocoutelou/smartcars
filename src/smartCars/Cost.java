/* Peut-être sera-t-il utile de créer une méthode
 * pour ranger une ArrayList<Road>
 * dans l'ordre croissant de coût de traversée.
 * Cette méthode serait soit ici présente, soit dans Road.
 * Le mieux serait que les listes de routes de chaque intersection
 * soit rangées dans l'ordre croissant de leur coût de traversée
 * dès la construction du graphe (à partir de l'image) !
 */

package smartCars;

import java.util.ArrayList;

public class Cost {
	
	private double absoluteCost;
	private boolean finite;
	private double realCost;
	
	public Cost()
	{
		finite = false;
	}
	
	public Cost(double cost)
	{
		this.absoluteCost = cost;
		this.finite = true;
		this.realCost = this.absoluteCost;
	}
	
	public static boolean inferior(Cost a, Cost b)
	{
		if(!b.finite) return true;
		else if(!a.finite) return false;
		else if(a.realCost<=b.realCost) return true;
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
		if(!b.finite) return a;
		else if(!a.finite) return b;
		else if(a.realCost<=b.realCost) return a;
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
		else return new Cost(a.realCost+b.realCost);
	}
	
}
