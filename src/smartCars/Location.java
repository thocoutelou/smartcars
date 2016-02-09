package smartCars;

public class Location {
	
	public boolean waitingForIntersection;
	private Road road;
	public float positionOnTheRoad;
	
	//TODO
	public static void displayLocation()
	{
		
	}
	
	public AbstractIntersection nextIntersection()
	{
		return road.destination;
	}

}
