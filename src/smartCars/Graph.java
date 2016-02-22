package smartCars;


import java.io.File;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Le graphe est représenté par la liste de ses noeuds (intersections AbstractIntersection),
 * chacune d'entre elles contenant la liste de ses arêtes (routes Road) directement connectées.
 * L'hypothèse est faite que le graphe est FORTEMENT CONNEXE.
 */
public class Graph {
	
	// (intersections) définit le graphe même
	private static ArrayList<AbstractIntersection> intersections;
	public static int numberOfIntersections;
	public static ArrayList<Road> roads;
	public static Cost[][] costsMatrix;
	
	// (startDefault) définit un point de départ pour les AbstractVehicle instanciés sans précision
	public static Location startDefault;
	public static Stack<AbstractVehicle> vehicles;
	public static ArrayList<Event> events;
	

	public Graph(ArrayList<AbstractIntersection> intersections, Location startDefault, ArrayList<AbstractVehicle> vehiclesInGraph)
	{
		Graph.intersections = intersections;
		Graph.startDefault = startDefault;
		Graph.numberOfIntersections = intersections.size();
		Graph.costsMatrix = Cost.floydWarshall();
		listRoads();
		stackVehicles(vehiclesInGraph);
	}
	
	
	//TODO contructeur à partir d'un image vectorielle
	//TODO définir les normes de l'image (notamment les couleurs)
	// Ne vaudrait-il pas mieux créer une classe Svg
	// dont les méthodes seraient appelées dans le constructeur Graph ?
	/*
	 * Étapes du parsing de l'image:
	 * 0 : Ouvrir le fichier .svg
	 * 1 : sélectionner les informations dans le calque 1 (balise g)
	 * 2 : créer toute les intersections représentées par des cercles <circle/>
	 * 3 : créer les road dans les intersections, représentés par des <path/>
	 * 	   un path a une intersection de départ si son premier sommet se trouve dans la zone d'un cercle (intersection)
	 */
	public Graph(String fileName) {
        
		/* La documentation du parser qu'on va utiliser est en ligne:
		 *  https://openclassrooms.com/courses/structurez-vos-donnees-avec-xml/dom-exemple-d-utilisation-en-java
		 */
		
		// La classe DocumentBuilderFacorty est utilisée pour parser le xml de l'image svg.
		final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		final DocumentBuilder builder;
		

		try {
			// On crée le parser
		    builder = factory.newDocumentBuilder();       
		    final Document document= builder.parse(new File(fileName));
		    //On sélectionne la racine
		    final Element racine = document.getDocumentElement();
		    final NodeList racineNoeuds = racine.getChildNodes();
		    final int nbRacineNoeuds = racineNoeuds.getLength();

		    for (int i = 0; i<nbRacineNoeuds; i++) {
		    	//On se place dans le premier calque
		        if (racineNoeuds.item(i).getNodeName() == "g"){
		        	final NodeList gNoeuds = racineNoeuds.item(i).getChildNodes();
		        	final int nbgNoeuds = gNoeuds.getLength();
		        	for(int j = 0; j<nbgNoeuds; j++ ){
		        		//if(gNoeuds.item(j).getNodeType() == Node.ELEMENT_NODE)
		        		System.out.println(gNoeuds.item(j).getNodeName());
		        	}
		        }

		    }
		}
		catch (final ParserConfigurationException e) {
		    e.printStackTrace();
		}
		catch (final SAXException e) {
		    e.printStackTrace();
		}
		catch (final IOException e) {
		    e.printStackTrace();
		}
		


	}

	/**
	 * Forme la liste complète des routes du graphe
	 */
	public void listRoads()
	{
		for(AbstractIntersection i : intersections)
		{
			roads.addAll(i.leavingRoads);
		}
	}

	/**
	 * Forme la pile des véhicules dans leur ordre de priorité de traitement :
	 * les véhicules d'itinéraires les plus longs sont prioritaires
	 * @param vehiclesInGraph
	 */
	public void stackVehicles(ArrayList<AbstractVehicle> vehiclesInGraph)
	{
		Stack<AbstractVehicle> vehicles = new Stack<AbstractVehicle>();
		while(!vehiclesInGraph.isEmpty())
		{
			vehicles.add(AbstractVehicle.lessPriorityVehicle(vehiclesInGraph));
		}
		Graph.vehicles = vehicles;
	}
	
}