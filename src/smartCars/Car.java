package smartCars;

public class Car extends AbstractVehicle {

	public Car()
	{
		startDate = 0.;
		startLocation = Graph.startDefault;
		size = 4.;
	}
	
	public Car(Location start)
	{
		this.startLocation = start;
	}
	

}
