package problem;

import java.awt.*;
import java.awt.image.AreaAveragingScaleFilter;
import java.util.ArrayList;
import java.util.Stack;

import graph.AbstractIntersection;
import graph.Road;
import resources.CartesianCoordinate;
import resources.Cost;
import smartcars.AbstractVehicle;
import graph.Graph;

/**
 * Created by tc on 18/05/16.
 */

public class GraphStateGenerator{

    private static CartesianCoordinate point1 = new CartesianCoordinate(0,0);
    private static CartesianCoordinate point2 = new CartesianCoordinate(2000,1000);

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
        //int roadNb = (int) Math.pow(2,intersectionNb);
        int roadNb = (int) intersectionNb;
        for(int i=0; i<intersectionNb; i++){
            boolean added = false;
            do{
                AbstractIntersection origin = intersections.get( (int) (Math.random()*(intersectionNb - 1)));
                AbstractIntersection destination = intersections.get( (int) (Math.random()*(intersectionNb - 1)));
                if(origin != destination){
                    Cost cost = new Cost(origin.center.distanceFrom(destination.center));
                    road = new Road(origin.center, destination.center, origin, destination, (double) 50, cost, 1, 10 );
                    added = true;
                    System.out.println(roads.size());
                }
            } while(! added);
        }

        // Test de la connexité du Graph
        Graph graph = new Graph(intersections, roads);
        for(AbstractIntersection intersection1: intersections) {
            for (AbstractIntersection intersection2 : intersections) {
                if (intersection2 != intersection1) {
                    if (Cost.isInfinite(graph.getCostsMatrix()[intersection2.identifier][intersection1.identifier])) {
                        ArrayList<AbstractIntersection> neighbours = (ArrayList<AbstractIntersection>) intersections.clone();
                        neighbours.remove(intersection1);
                        System.out.println(neighbours.size());
                        //AbstractIntersection link = intersection1.center.closestRoad(neighbours).getDestination();
                        AbstractIntersection link = AbstractIntersection.getClosestIntersection(intersection1.center, neighbours);
                        Cost cost = new Cost(intersection1.center.distanceFrom(link.center));
                        road = new Road(intersection1.center, link.center, intersection1, link, (double) 50, cost, 1, 10);
                        roads.add(road);
                        System.out.println("Road added");
                        graph = new Graph(intersections, roads);
                    }
                }
            }
        }

        for(AbstractIntersection origin : intersections){
            for(AbstractIntersection destination : intersections){
                if(origin != destination){
                    if(Cost.isInfinite(graph.getCostsMatrix()[destination.identifier][origin.identifier])){
                        Cost cost = new Cost(origin.center.distanceFrom(destination.center));
                        road = new Road(origin.center, destination.center, origin, destination, (double) 50, cost, 1, 10 );
                        roads.add(road);
                        road = new Road(destination.center, origin.center, destination, origin, (double) 50, cost, 1, 10 );
                        roads.add(road);
                        System.out.println("Road added");
                        graph = new Graph(intersections, roads);
                    }
                }
            }
        }

        return roads;
    }

}
