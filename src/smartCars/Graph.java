package smartCars;


import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Le graphe est représenté par la liste de ses noeuds (intersections AbstractIntersection),
 * chacun d'entre eux contenant la liste de ses arêtes (routes Road) directement connectées.
 * L'hypothèse est faite que le graphe est connexe.
 */
public class Graph {
	
	// (intersections) définit le graphe même
	private ArrayList<AbstractIntersection> intersections = new ArrayList<AbstractIntersection>();
	public static int numberOfIntersections = 0;
	public static ArrayList<Road> roads = new ArrayList<Road>();
	public static Cost[][] costsMatrix;
	
	// (startDefault) définit un point de départ pour les AbstractVehicle instanciés sans précision
	public static Location startDefault;
	public static Stack<AbstractVehicle> vehicles;
	public static ArrayList<Event> events;
	

	public Graph(ArrayList<AbstractIntersection> intersections, Location startDefault, ArrayList<AbstractVehicle> vehiclesInGraph)
	{
		this.intersections = intersections;
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
	 * 2 : créer toute les intersections représentées par des cercles <circle/> et les Road représentées par des <path/>
	 * 3 : Créer le graphe
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
		        		// On filtre les éléments qui sont des noeuds
		        		if(gNoeuds.item(j).getNodeType() == Node.ELEMENT_NODE){
		        			final Element geometricFigure = (Element) gNoeuds.item(j);
		        			
		        			// Cas d'une intersection
		        			if(geometricFigure.getNodeName() == "circle" ){
		        				AbstractIntersection parseIntersection = new AbstractIntersection();
		        				parseIntersection.center = new CartesianCoordinate(Float.parseFloat(geometricFigure.getAttribute("cx")),
		        						Float.parseFloat(geometricFigure.getAttribute("cy")));
		        				parseIntersection.radius = Float.parseFloat(geometricFigure.getAttribute("r"));
			        			System.out.println("Intersection " + parseIntersection.identifier + " parsed: " + parseIntersection);
		        				this.intersections.add(parseIntersection);
		        			}

		        			// Cas d'un Road
		        			if(geometricFigure.getNodeName() == "path" ){
		        				Road parseRoad = new Road();
		        				// d contient les coordonnées du path
		        				// d est sous la forme d="m 395.9798,236.15895 6.06091,165.66502"
		        				String d=geometricFigure.getAttribute("d");
		        				String [] dParse = d.split("( |,)"); // Utilisation de regex
		        				if(dParse[0].equals("M") || dParse[0].equals("m")){
		        					if(dParse[0].equals("M")){
		        						parseRoad.point1 = new CartesianCoordinate(Float.parseFloat(dParse[1]), Float.parseFloat(dParse[2]));
		        						parseRoad.point2 = new CartesianCoordinate(Float.parseFloat(dParse[3]), Float.parseFloat(dParse[4]));
		        					} else { //dParse[0].equals("m")
		        						parseRoad.point1 = new CartesianCoordinate(Float.parseFloat(dParse[1]), Float.parseFloat(dParse[2]));
		        						parseRoad.point2 = new CartesianCoordinate(parseRoad.point1.x + Float.parseFloat(dParse[3]), parseRoad.point1.y + Float.parseFloat(dParse[4]));
		        					}
				        			System.out.println("Road " + parseRoad.identifier + " parsed: " +
				        					parseRoad.point1 + "; " + parseRoad.point2);
			        				this.roads.add(parseRoad);
		        				}else{
		        					System.out.println("Cannot parse " + geometricFigure.getAttribute("id"));
		        				}

		        			}


		        		}
		        		
		        	}
		        	
		        	/* Création du graphe
		        	 * On parcourt les arêtes et on rajoute les arêtes dont le point1 est situé dans une intersection
		        	 * et point2 dans une intersection (On ne considère que les arêtes arêtes significatives)
		        	 */
		        	for(int j = 0; j<roads.size(); j++ ){
		        		Boolean isIntegrated = false;
		        		// Vérification de point1
		        		for(int k= 0; k<intersections.size(); k ++){
		        			if(roads.get(j).point1.distanceFrom(intersections.get(k).center) <= intersections.get(k).radius){
		        				// On cherche alors pour le point2
		        				for(int l=0; l<intersections.size(); l++){
		        					if(roads.get(j).point2.distanceFrom(intersections.get(l).center) <= intersections.get(l).radius){
		        						// Le Road j a deux extrémités dans des intersections
		        						//On renseigne les informations de l'arête
		        						roads.get(j).origin = intersections.get(k);
		        						roads.get(j).destination = intersections.get(l);
		        						// On renseigne origin de l'arête sortante
		        						intersections.get(k).leavingRoads.add(roads.get(j));
		        						isIntegrated = true;
		        					}
		        				}
		        			}
		        		}
	        			if(! isIntegrated){
	        				System.out.println(roads.get(j) + " has not been integrated to the graph" );
	        			}
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
	
	public String toString() {
		String newline = System.getProperty("line.separator");
		String result = "<---   Graph print   --->" + newline;
		for (int i=0; i<intersections.size(); i++){
			result += intersections.get(i).toStringDetailed()+ newline;
		}
		return result;
	}
	
}