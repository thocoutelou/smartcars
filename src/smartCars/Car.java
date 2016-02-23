package smartCars;


public class Car extends AbstractVehicle {

	public Car(Graph graph)
	{
		super(graph);
		size = 4.;
	}
	
	public Car(Graph graph, Location start)
	{
		super(graph);
		size = 4.;
		this.location = start;
	}
	

}
