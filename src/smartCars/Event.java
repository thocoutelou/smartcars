package smartCars;

public class Event {
	
	private int identificator = 0;
	private int identifier;

	public boolean critical;
	public int nature;
	public double date;
	
	public Event(int nature, double date)
	{
		identifier = identificator;
		identificator++;
		
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
