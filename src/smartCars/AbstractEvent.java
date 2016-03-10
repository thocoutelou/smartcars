package smartCars;

import java.util.Collections;

public class AbstractEvent implements Comparable<AbstractEvent> {

	protected static int identificator = 0;
	protected int identifier;
	protected AbstractVehicle vehicle;
	protected double date;
	
	public AbstractEvent(AbstractVehicle vehicle)
	{
		this.identifier = identificator;
		identificator++;
		this.vehicle = vehicle;
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
