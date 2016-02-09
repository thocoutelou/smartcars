/* Peut-être sera-t-il utile de créer une méthode
 * pour ranger une ArrayList<Road>
 * dans l'ordre croissant de coût de traversée.
 * Cette méthode serait soit ici présente, soit dans Road.
 * Le mieux serait que les listes de routes de chaque intersection
 * soit rangées dans l'ordre croissant de leur coût de traversée
 * dès la construction du graphe (à partir de l'image) !
 */

package smartCars;

public class Cost {
	
	private boolean finite;
	private int cost = -1;
	
	public Cost()
	{
		finite = false;
	}
	
	public Cost(int cost)
	{
		this.cost = cost;
		this.finite = true;
	}
	
	/**
	 * Renvoie le Cost le plus faible.
	 * @param Cost a: premier Cost à comparer à b, qui sera prioritaire
	 * @param Cost b: deuxième Cost à comparer à a
	 * @return Cost minimal
	 */
	public Cost minimum(Cost a, Cost b)
	{
		if(!b.finite) return a;
		else if(!a.finite) return b;
		else if(a.cost<=b.cost) return a;
		else return b;
	}
	
	public Cost maximum(Cost a, Cost b)
	{
		if((minimum(a, b)).equals(a)) return b;
		else return a;
	}
	
	
	
}
