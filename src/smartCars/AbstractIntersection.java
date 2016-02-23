package smartCars;


import java.util.ArrayList;

public class AbstractIntersection {
	
	protected static int identificator = 0;
	public int identifier;
	// (leavingRoads) routes sortantes directement connectées
	public ArrayList<Road> leavingRoads = new ArrayList<Road>();
	
	// (crossingTime) durée moyenne de traversée de l'intersection
	public double averageTime;
	// (obstruction) indique si l'intersection est obstruée
	public boolean obstruction;
	
	//Géométrie de l'intersection
	public float cx;
	public float cy;
	public float r;
	
	
	public AbstractIntersection(ArrayList<Road> leavingRoads) {
		identifier = identificator;
		identificator++;
		averageTime = 10.;
		obstruction = false;
		this.leavingRoads = leavingRoads;
	}

	public AbstractIntersection()
	{
		this(new ArrayList<Road>());
	}


	//TODO
	public void crossIntersection(AbstractVehicle vehicle, Road nextRoad)
	{
		
	}

}
