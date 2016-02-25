package smartCars;


import java.util.ArrayList;

public class AbstractIntersection {
	
	protected static int identificator = 0;
	public int identifier;
	// (leavingRoads) routes sortantes directement connectées
	public ArrayList<Road> leavingRoads;
	
	// (crossingTime) durée moyenne de traversée de l'intersection
	public double averageTime;
	// (obstruction) indique si l'intersection est obstruée
	public boolean obstruction;
	
	//Géométrie de l'intersection
	public CartesianCoordinate center;
	public float radius;
	
	
	public AbstractIntersection(CartesianCoordinate center, float radius) {
		identifier = identificator;
		identificator++;
		this.center= center;
		this.radius = radius;
		averageTime = 10.;
		obstruction = false;
		// L'initialisation de leavingRoads est nécessaire, mais difficile à faire dès l'instanciation de l'intersection
		this.leavingRoads = new ArrayList<Road>();
	}

	//TODO
	public void crossIntersection(AbstractVehicle vehicle, Road nextRoad)
	{
		
	}
	
	public ArrayList<Road> getLeavingRoads() {
		return leavingRoads;
	}

	public void setLeavingRoads(ArrayList<Road> leavingRoads) {
		this.leavingRoads = leavingRoads;
	}

	public String toStringDetailed(){
		String newline = System.getProperty("line.separator");
		String result = "Intersection " + this.identifier + " Center: " + this.center.toString() + " R= " + this.radius + " leavingRoads: " + newline;
		for(int i=0; i<leavingRoads.size(); i++){
			result += leavingRoads.get(i).toStringDetailed() + newline;
		}
		return result;
	}
	
	@Override
	public String toString(){
		return "Intersection " + this.identifier;
	}

}
