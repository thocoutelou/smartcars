package smartCars;

import java.util.ArrayList;

public class CartesianCoordinate {
	
	public double x;
	public double y;
	
	public CartesianCoordinate(double x, double y) {
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
	
	
	//Retourne la Road la plus proche du point parmis un ensemble d'intersections composés de leavingRoads
	public Road closestRoad(ArrayList<AbstractIntersection> intersections){
		Road closestRoad = intersections.get(0).getLeavingRoads().get(0);
		Road testRoad = intersections.get(0).getLeavingRoads().get(0);
		CartesianCoordinate pointH = closestRoad.coordinateProjection(this);
		double minimumDistance = this.distanceFrom(pointH);
		
		for(int i=0; i<intersections.size(); i++){
			for(int j=0; j<intersections.get(i).getLeavingRoads().size(); j++){
				testRoad = intersections.get(i).getLeavingRoads().get(j);
				pointH = testRoad.coordinateProjection(this);
				if (this.distanceFrom(pointH) < minimumDistance){
					closestRoad = testRoad;
					minimumDistance = this.distanceFrom(pointH);
				}
			}
		}
		
		return closestRoad;
	}

	static public CartesianCoordinate CartesianCoordinateRandom(CartesianCoordinate point1, CartesianCoordinate point2){
		double x = Math.random()*(point2.x-point1.x)+point1.x;
		double y = Math.random()*(point2.y-point1.y)+point2.y;
		return new CartesianCoordinate(x,y);
	}
	
	public String toString(){
		return "("+this.x + ", " + this.y + ")";
	}
	
}
