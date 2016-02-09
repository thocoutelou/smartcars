package smartCars;

public class AbstractVehicle {

	protected Location start;
	public int size;
	
	
	public void calculateLocation(float time, Graph graph)
	{
		
	}
	
	public AbstractIntersection intersectionAfterStart(float time)
	{
		return start.nextIntersection();
	}
	
	//TODO
	public void overtake(AbstractVehicle vehicle, Road road)
	{
		
	}

}
