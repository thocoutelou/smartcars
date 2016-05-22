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
Cette classe permet de générer des graphe connexe pseudo-aléatoires. Elle est utilise pour faire des test sur de nombreux Graph.
 C'est une approche graphique : on cherche à générer des graphe qui ont un sens, et qui ressemble à ceux qu'on pourrait avoir
 naturellement dans une ville
 */

public class GraphStateGenerator{

    /*
    les deux points suivants délimite la fenêtre du graph que l'on va générer.
     */
    private static CartesianCoordinate point1 = new CartesianCoordinate(0,0);
    private static CartesianCoordinate point2 = new CartesianCoordinate(2000,1000);

    public static GraphState GraphStateGenerator(int intersectionNb, int vehicleNb){
        ArrayList<AbstractIntersection> intersections = intersectionsGenerator(intersectionNb);
        ArrayList<Road> roads = roadsGenerator(intersections);
        Stack<AbstractVehicle> vehicles = new Stack<AbstractVehicle>();

        return new GraphState(intersections,roads,vehicles);
    }
    /*
    Permet des intersections qui ne se chevauchent pas. On renseigne le nombre d'intersections voulues avec intersectionNb
     */
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

    /*
    Permet de générer des roads entre différents intersections contenues dans ArrayList<AbstractIntersection> intersection.
    La génération des road est compliquée car il faut rendre le graphe connexe. VOici les étapes:
    - création de Road aléatoire
    - liaison d'intersections isolées à l'intersection la plus proche
    - liaison des intersections restantes à la composante connexe la plus grande
     */
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
