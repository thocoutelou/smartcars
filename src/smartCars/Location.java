package smartCars;

public class Location {
	
	public boolean waitingForIntersection;
	
	public Road initialRoad;
	public double initialPosition;
	public double initialDate;
	
	public Road finalRoad;
	public double finalPosition;
	public double finalDate;
	
	public Road currentRoad;
	public double currentPosition;
	public double currentDate;
	
	public Location()
	{
		initialDate = 0.;
		initialPosition = initialRoad.length;
	}
	
	//TODO
	public static void displayLocation()
	{
		
	}
	
	public AbstractIntersection nextIntersection()
	{
		return initialRoad.destination;
	}
	

}
