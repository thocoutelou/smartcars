package smartCars;


public class AbstractEvent implements Comparable<AbstractEvent> {

	protected static int identificator = 0;
	protected int identifier;
	protected int nature;
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
	}

	public int compareTo(AbstractEvent event)
	{
		if(this.date>event.date) return 1;
		else return -1;
	}
	
	public boolean nextEvent(AbstractVehicle vehicle)
	{
		AbstractEvent lastEvent = vehicle.events.peek();
		int nature = lastEvent.nature;
		if(nature==4) return true;
		else if(nature==0)
		{
			EventVehicleStart.nextEvent(road, vehicle, date);
			return false;
		}
		else if(nature==1)
		{
			
			return false;
		}
		else if(nature==2)
		{

			return false;
		}
		else if(nature==3)
		{

			return false;
		}
	}
	
}
