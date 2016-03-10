package smartCars;

public class AbstractEvent {

	protected static int identificator = 0;
	protected int identifier;
	protected AbstractVehicle vehicle;
	
	public AbstractEvent(AbstractVehicle vehicle)
	{
		this.identifier = identificator;
		identificator++;
		this.vehicle = vehicle;
	}
	
}
