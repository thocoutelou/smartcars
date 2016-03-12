package smartCars;

import java.util.Collections;

public class AbstractEvent implements Comparable<AbstractEvent> {

	protected static int identificator = 0;
	protected int identifier;
	protected AbstractVehicle vehicle;
	protected Road road;
	protected double date;
	
	protected AbstractEvent(AbstractVehicle vehicle, Road road)
	{
		this.identifier = identificator;
		identificator++;
		this.vehicle = vehicle;
		this.road = road;
	}
	
	public int compareTo(AbstractEvent event)
	{
		if(this.date>event.date) return 1;
		else return -1;
	}
	
	public static void sort(GraphState g)
	{
		Collections.sort(Time.events);
	}
	
}
