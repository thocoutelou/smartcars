package smartCars;

public class AbstractIntersection {
	
	protected static int identificator;
	public int identifier;
	
	// (type) identifie le type d'intersection instancié
	private int type;
	
	// (crossingTime) indique la durée moyenne de traversée de l'intersection
	public float crossingLast;
	
	public boolean obstruction;
	
	
	public void crossIntersection(AbstractVehicle vehicle, Road nextRoad)
	{
		
	}

}
