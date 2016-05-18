package smartCars;

import java.awt.image.AreaAveragingScaleFilter;
import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by tc on 18/05/16.
 */
public class GraphStateGenerator{

    static CartesianCoordinate point1 = new CartesianCoordinate(0,0);
    static CartesianCoordinate point2 = new CartesianCoordinate(2000,1000);

    public static GraphState GraphStateGenerator(int intersectionNb, int vehicleNb){
        ArrayList<AbstractIntersection> intersections = intersectionsGenerator(intersectionNb);
        ArrayList<Road> roads = roadsGenerator(intersections);
        Stack<AbstractVehicle> vehicles = new Stack<AbstractVehicle>();
        return new GraphState(intersections,roads,vehicles);
    }

    private static ArrayList<AbstractIntersection> intersectionsGenerator(int intersectionNb){
        ArrayList<AbstractIntersection> intersections = new ArrayList<AbstractIntersection>();
        AbstractIntersection intersection;
        for(int i=0; i<intersectionNb; i++){
            do {
                CartesianCoordinate center = CartesianCoordinate.CartesianCoordinateRandom(point1, point2);
                intersection = new AbstractIntersection(center, 50, 10);
            }while(intersection.overlapList(intersections));
            intersections.add(intersection);
        }
        return intersections;
    }

    private static ArrayList<Road> roadsGenerator(ArrayList<AbstractIntersection> intersections){
        ArrayList<Road> roads = new ArrayList<Road>();
        Road road;
        int intersectionNb = intersections.size();
        System.out.println(intersectionNb);
        int roadNb = (int) Math.pow(2,intersectionNb);
        for(int i=0; i<intersectionNb; i++){
            boolean added = false;
            do{
                AbstractIntersection origin = intersections.get( (int) (Math.random()*(intersectionNb - 1)));
                AbstractIntersection destination = intersections.get( (int) (Math.random()*(intersectionNb - 1)));
                if(origin != destination){
                    Cost cost = new Cost(origin.center.distanceFrom(destination.center));
                    road = new Road(origin.center, destination.center, origin, destination, (double) 50, cost, 1, 10 );
                    roads.add(road);
                    added = true;
                }
            } while(! added);
        }
        return roads;
    }


}
