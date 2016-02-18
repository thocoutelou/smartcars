package smartCars;

public class Event {
	
	private int vehicleIdentifier;
	public boolean critical;
	public int nature;
	public double date;
	
	public Event(AbstractVehicle vehicle, int nature, double date)
	{
		this.vehicleIdentifier = vehicle.identifier;
		this.nature = nature;
		this.date = date;
		setCritical();
	}
	
	//TODO Définir les natures possibles des évènements
	private void setCritical()
	{
		critical = nature==0;
	}
	
	
	
}
