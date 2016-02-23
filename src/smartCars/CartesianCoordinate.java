package smartCars;

public class CartesianCoordinate {
	
	public float x;
	public float y;
	
	public CartesianCoordinate(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public double distanceFrom(CartesianCoordinate point2){
		return Math.sqrt(Math.pow(point2.x-this.x, 2) + Math.pow(point2.y-this.y, 2)); 
	}
	
	public static double distance(CartesianCoordinate point1, CartesianCoordinate point2)
	{
		return point1.distanceFrom(point2);
	}
	
	public String toString(){
		return "("+this.x + ", " + this.y + ")";
	}
	
}
