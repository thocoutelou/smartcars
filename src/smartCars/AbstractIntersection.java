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
	public CartesianCoordinate center;
	public float radius;
	
	
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
	
	public String toStringDetailed(){
		String newline = System.getProperty("line.separator");
		String result = "Intersection " + this.identifier + " Center: " + this.center.toString() + " R= " + this.radius + " leavingRoads: " + newline;
		for(int i=0; i<leavingRoads.size(); i++){
			result += leavingRoads.get(i).toStringDetailed() + newline;
		}
		return result;
	}
	
	public String toString(){
		return "Intersection " + this.identifier;
	}

}
