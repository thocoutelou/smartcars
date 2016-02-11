package smartCars;


public class Car extends AbstractVehicle {

	public Car()
	{
		super();
		size = 4.;
	}
	
	public Car(Location start)
	{
		super();
		size = 4.;
		this.vehicleLocation = start;
	}
	

}
