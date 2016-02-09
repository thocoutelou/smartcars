package smartCars;

public class Road {
	
	private Cost cost;
	private int lane;
	
	public Road()
	{
		cost = new Cost();
		lane = 1;
	}
	
	public Road(Cost cost)
	{
		this.cost = new Cost();
		this.lane = 1;
	}
	
	public Road(Cost cost, int lane)
	{
		this.cost = cost;
		this.lane = lane;
	}
	
	
	
}
