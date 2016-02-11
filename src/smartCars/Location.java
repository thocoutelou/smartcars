package smartCars;

public class Location {
	
	
	public boolean waitingForIntersection;
	public Road road;
	public double initialDate;
	public double initialPosition;
	public double positionOnTheRoad;
	public double date;
	
	public Location()
	{
		initialDate = 0.;
		initialPosition = road.length;
		
		
	}
	
	//TODO
	public static void displayLocation()
	{
		
	}
	
	public AbstractIntersection nextIntersection()
	{
		return road.destination;
	}
	

}
