package resources;

import graph.AbstractIntersection;
import graph.Road;

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
	// seulement si une requête de localisation du véhicule a été formulée,
	// et seront dans ce cas calculées à la date Time.time
	public Road currentRoad;
	public double currentPosition;
	public double currentDate;
	public boolean onIntersection = false;
	
	// nature du dernier évènement exécuté concernant le véhicule
	public int lastEventNature = 4;
	
	/**
	 * Constructeur unique, ne doit être utilisé que par le parser,
	 * ou lors de l'insertion d'un nouveau véhicule.
	 */
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
	 * Renvoie la première intersection
	 * rencontrée par le véhicule après son départ.
	 * @return première intersection rencontrée
	 */
	public AbstractIntersection nextIntersection()
	{
		return initialRoad.getDestination();
	}

	/**
	 * Renvoie la dernière intersection
	 * rencontrée par le véhicule avant son arrivée.
	 * @return dernière intersection rencontrée
	 */
	public AbstractIntersection finalIntersection()
	{
		return finalRoad.getOrigin();
	}
	
	/**
	 * Met à jour la localisation courante du véhicule.
	 * Cette méthode sera appelée lors de l'exécution des évènements.
	 */
	public void actualizeLocation(Road road, double position, double date, int lastEventNature)
	{
		currentRoad = road;
		if(position>road.getAbsoluteLength()+0.1)
		{
			//throw new IllegalArgumentException("La position du véhicule est fausse (en dehors de la route).");
			currentPosition = road.getAbsoluteLength();
		} else {
			currentPosition = position;
		}
		currentDate = date;
		this.lastEventNature = lastEventNature;
	}
	
	/**
	 * Vérifie que la localisation courante supposée à jour
	 * coïncide avec la localisation finale du véhicule.
	 * @return Le véhicule est-il arrivé ?
	 */
	public boolean checkFinalLocation()
	{
		return  ( finalRoad.equals(currentRoad)
				& finalPosition==currentPosition
				& finalDate==currentDate
				);
	}
	
	public String toString(){
		String newline = System.getProperty("line.separator");
		String result = "Position initiale : "+initialRoad.toString()+" / "+initialPosition+"m / "+initialDate+"s"+newline;
		result += "Position finale : "+finalRoad.toString()+" / "+finalPosition+"m / date finale à calculer\n";
		return result;
	}

}
