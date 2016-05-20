package graph;


import java.util.ArrayList;

import resources.CartesianCoordinate;
import resources.Cost;
import smartcars.AbstractVehicle;

/**
 * Intersection du graphe.
 * Contient les routes sortantes.
 * @author cylla
 *
 */
public class AbstractIntersection {
	
	// identificateur des instances, s'incrémente à chaque instanciation...
	private static int identificator = 0;
	// ... pour définir l'identifiant de l'intersection créée
	public final int identifier;
	// routes sortant de l'intersection
	private ArrayList<Road> leavingRoads;
	// véhicules sur l'intersection
	private ArrayList<AbstractVehicle> vehiclesOnIntersection = new ArrayList<AbstractVehicle>();
	
	// durée moyenne de traversée de l'intersection
	private double averageTime;
	
	//TODO passer en private
	// géométrie de l'intersection
	public final CartesianCoordinate center;
	public final float radius;
	
	
	/**
	 * Constructeur unique, doit être utilisé seulement par le parser.
	 * @param center
	 * @param radius
	 */
	public AbstractIntersection(CartesianCoordinate center, float radius, double averageTime) {
		identifier = identificator;
		identificator++;
		this.center= center;
		this.radius = radius;
		this.averageTime = averageTime;
		// l'initialisation de leavingRoads est nécessaire,
		// mais impossible à réaliser dès l'instanciation de l'intersection
		this.leavingRoads = new ArrayList<Road>();
	}


	/**
	 * Calcule la somme du coût d'une route
	 * et de son intersection d'arrivée.
	 * Permet de prendre en compte les coûts de traversée des intersection
	 * lors du calcul des itinéraires (Dijkstra).
	 * @param road
	 * @return somme du coût de la route et de son intersection d'arrivée
	 */
	public static Cost intersectionCost(Road road)
	{
		return Cost.sum(road.getCost(), new Cost(road.getDestination().getAverageTime()));
	}

	/**
	 * Identification de l'intersection.
	 */
	@Override
	public String toString(){
		return "Intersection " + this.identifier;
	}

	/**
	 * Les intersections se chevauchent-elles ?
	 * Méthode utilisée dans la génération aléatoire de graphes.
	 * @param intersectionB
	 * @return Les intersections se chevauchent-elles ?
	 */
	public boolean overlap(AbstractIntersection intersectionB){
		return (this.center.distanceFrom(intersectionB.center) <= this.radius + intersectionB.radius );
	}


	/**
	 * La liste des intersections contient-elle
	 * une intersection qui chevauche l'instance courrante ?
	 * Méthode utilisée dans la génération aléatoire de graphes.
	 * @param intersections
	 * @return Deux intersections se chevauchent-elles ?
	 */
	public boolean overlapList(ArrayList<AbstractIntersection> intersections){
		for(AbstractIntersection intersection : intersections){
			if(this.overlap(intersection)){
				return true;
			}
		}
		return false;
	}

	/**
	 * Identification détaillée de l'intersection.
	 */
	public String toStringDetailed(){
		String newline = System.getProperty("line.separator");
		String result = "Intersection " + this.identifier + " Center: " + this.center.toString() + " R= " + this.radius + " leavingRoads: " + newline;
		for(int i=0; i<leavingRoads.size(); i++){
			result += leavingRoads.get(i).toStringDetailed() + newline;
		}
		return result;
	}
	
	
	// *** Getters ***

	public ArrayList<Road> getLeavingRoads() {
		return leavingRoads;
	}
	
	/**
	 * Setter des routes sortant de l'intersection.
	 * Doit être utilisée par le parser en complément du constructeur.
	 * @param leavingRoads
	 */
	public void setLeavingRoads(ArrayList<Road> leavingRoads) {
		this.leavingRoads = leavingRoads;
	}
	
	public double getAverageTime() {
		return averageTime;
	}


	public static int getIdentificator() {
		return identificator;
	}

	public ArrayList<AbstractVehicle> getVehiclesOnIntersection() {
		return vehiclesOnIntersection;
	}

}
