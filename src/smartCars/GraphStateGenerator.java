package smartCars;

import java.util.ArrayList;

/**
 * Created by tc on 18/05/16.
 */
public class GraphStateGenerator extends GraphState{
	
    CartesianCoordinate point1 = new CartesianCoordinate(0,0);
    CartesianCoordinate point2 = new CartesianCoordinate(2000,1000);

    public GraphStateGenerator(int intersectionNb, int roadNb, int vehicleNb){
        ArrayList<AbstractIntersection> intersections = intersectionsGenerator(intersectionNb);


    }

    public ArrayList<AbstractIntersection> intersectionsGenerator(int intersectionNb){
        ArrayList<AbstractIntersection> intersections = new ArrayList<AbstractIntersection>();
        AbstractIntersection intersection;
        for(int i=0; i<intersectionNb; i++){
            do {
                CartesianCoordinate center = CartesianCoordinate.CartesianCoordinateRandom(point1, point2);
                intersection = new AbstractIntersection(center, 50, 10);
            }while(intersection.overlapList(intersections));
        }
        return intersections;
    }

}
