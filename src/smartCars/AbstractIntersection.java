package smartCars;


import java.util.ArrayList;

/**
 * Intersection du graphe.
 * Contient les routes sortantes.
 * @author cylla
 *
 */
public class AbstractIntersection {
	
	// identificateur des instances, s'incrémente à chaque instanciation...
	protected static int identificator = 0;
	// ... pour définir l'identifiant de l'intersection créée
	public final int identifier;
	// routes sortant de l'intersection
	private ArrayList<Road> leavingRoads;
	// véhicules sur l'intersection
	public ArrayList<AbstractVehicle> vehiclesOnIntersection = new ArrayList<AbstractVehicle>();
	
	// durée moyenne de traversée de l'intersection
	public double averageTime;
	// L'intersection est-elle encombrée ?
	public boolean obstruction;
	
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
		obstruction = false;
		// l'initialisation de leavingRoads est nécessaire, mais difficile à faire dès l'instanciation de l'intersection
		this.leavingRoads = new ArrayList<Road>();
	}


	/**
	 * Getter des routes sortant de l'intersection.
	 * @return Routes sortantes
	 */
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
	
	/**
	 * Identification de l'intersection.
	 */
	@Override
	public String toString(){
		return "Intersection " + this.identifier;
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

}
