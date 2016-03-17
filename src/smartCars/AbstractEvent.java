package smartCars;


public class AbstractEvent implements Comparable<AbstractEvent> {

	protected static int identificator = 0;
	protected int identifier;
	protected AbstractVehicle vehicle;
	protected Road road;
	protected double date;
	
	protected AbstractEvent(AbstractVehicle vehicle, Road road, double date)
	{
		this.identifier = identificator;
		identificator++;
		this.vehicle = vehicle;
		this.road = road;
		this.date = date;
		vehicle.events.add(this);
	}

	public int compareTo(AbstractEvent event)
	{
		if(this.date>event.date) return 1;
		else return -1;
	}
	
	public static double duration(Road road, double distance)
	{
		return distance/road.speed;
	}
	
}
