package smartCars;

/**
 * Données de localisation d'un véhicule.
 * @author cylla
 *
 */
public class Location {
	
	// Le véhicule est-il en attente de traversée d'une intersection ?
	public boolean waitingForIntersection;
	
	// données de position initiale
	public Road initialRoad;
	public double initialPosition;
	public double initialDate;
	
	// données de position de la destination
	public Road finalRoad;
	public double finalPosition;
	public double finalDate;
	
	// données de la position courante significatives
	// seulement si une requête de localisation du véhicule a été formulée
	public Road currentRoad;
	public double currentPosition;
	public double currentDate;
	
	/**
	 * Constructeur unique, ne doit être utilisé que par le parser,
	 * ou lors de l'insertion d'un nouveau véhicule.
	 */
	//TODO
	public Location(Road initialRoad, double initialPosition, double initialDate, Road finalRoad, double finalPosition, double finalDate)
	{
		waitingForIntersection = false;
		
		this.initialRoad = initialRoad;
		this.initialPosition = initialPosition;
		this.initialDate = initialDate;

		this.finalRoad = finalRoad;
		this.finalPosition = finalPosition;
		this.finalDate = finalDate;
		
		this.currentRoad = initialRoad;
		this.currentPosition = initialPosition;
		this.currentDate = initialDate;
	}
	
	/**
	 * Affiche le véhicule sur l'image,
	 * en partant du principe que sa localisation courante est à jour.
	 * Cette mise à jour se fera pas à pas dans la classe Event.
	 */
	//TODO
	public static void displayLocation()
	{
		
	}
	
	/**
	 * Renvoie la première intersection
	 * rencontrée par le véhicule après son départ.
	 * @return première intersection rencontrée
	 */
	public AbstractIntersection nextIntersection()
	{
		return initialRoad.destination;
	}

	/**
	 * Renvoie la dernière intersection
	 * rencontrée par le véhicule avant son arrivée.
	 * @return dernière intersection rencontrée
	 */
	public AbstractIntersection finalIntersection()
	{
		return finalRoad.origin;
	}

}
