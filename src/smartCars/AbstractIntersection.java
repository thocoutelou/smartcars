package smartCars;


import java.util.ArrayList;

public class AbstractIntersection {
	
	protected static int identificator = 0;
	public int identifier;
	// (type) identifie le type d'intersection instancié
	private int type;
	
	// (crossingTime) durée moyenne de traversée de l'intersection
	public double crossingDuration;
	// (obstruction) indique si l'intersection est obstruée
	public boolean obstruction;
	
	// (leavingRoads) routes sortantes directement connectées
	public ArrayList<Road> leavingRoads = new ArrayList<Road>();
	
	
	public AbstractIntersection(ArrayList<Road> leavingRoads)
	{
		identifier = identificator;
		identificator++;
		type = 0;
		crossingDuration = 10.;
		obstruction = false;
		this.leavingRoads = leavingRoads;
	}
	
	//TODO
	public void crossIntersection(AbstractVehicle vehicle, Road nextRoad)
	{
		
	}

}
