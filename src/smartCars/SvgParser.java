package smartCars;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class SvgParser {
	
	/**
	 * Cette classe permet de créer un graphe à partir d'une image svg. 
	 * Elle extrait également les voitures du svg 
	 */
	// ***** Commentaires de lecture et questions *****
		// -> pourquoi utiliser float plutôt que double ?
		// -> ajout d'une initialisation de fortune du coût d'une route
		// -> il faudra utiliser les constructeurs complets des objets,
		// quitte à modifier ces constructeurs, pour faciliter la lecture et cibler le debug :
		// commencer par définir les caractéristiques de l'objet à créer avant d'instancier la classe
		// -> je pense que l'exception NullPointerException est due à l'initialisation des routes de ce constructeur
		
		//TODO définir les normes de l'image (notamment les couleurs)
		// Ne vaudrait-il pas mieux créer une classe Svg
		// dont les méthodes seraient appelées dans le constructeur Graph ?
		/*
		 * Étapes du parsing de l'image:
		 * 0 : Ouvrir le fichier .svg
		 * 1 : sélectionner les informations dans le calque 1 (balise g)
		 * 2 : créer toute les intersections représentées par des cercles <circle/> puis les Road représentées par des <path/> aves les méthodes
		 * parseIntersection() et parseRoad()
		 * 	   un path a une intersection de départ si son premier sommet se trouve dans la zone d'un cercle (intersection)
		 */
		public static Graph parseGraph(String fileName) {

			/* La documentation du parser qu'on va utiliser est en ligne:
			 *  https://openclassrooms.com/courses/structurez-vos-donnees-avec-xml/dom-exemple-d-utilisation-en-java
			 */

			// La classe DocumentBuilderFacorty est utilisée pour parser le xml de l'image svg.
			final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			final DocumentBuilder builder;
			
			//Attributs du futur Graph
			ArrayList<AbstractIntersection> intersections = new ArrayList<AbstractIntersection>();
			ArrayList<Road> roads = new ArrayList<Road>();

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

						// Parcourt et création des intersections
						for(int j = 0; j<gNoeuds.getLength(); j++ ){
							// On filtre les éléments qui sont des noeuds
							if(gNoeuds.item(j).getNodeType() == Node.ELEMENT_NODE){
								final Element geometricFigure = (Element) gNoeuds.item(j);
								// Cas d'une intersection
								if(geometricFigure.getNodeName() == "circle" ){
									intersections.add(parseIntersection(geometricFigure));
								}
							}
						}


						// Parcourt des Road
						// Toutes les intersections doivent être instanciées avant la création des Road
						for(int j = 0; j<gNoeuds.getLength(); j++ ){
							// On filtre les éléments qui sont des noeuds
							if(gNoeuds.item(j).getNodeType() == Node.ELEMENT_NODE){
								final Element geometricFigure = (Element) gNoeuds.item(j);
								if(geometricFigure.getNodeName() == "path" ){
									roads.add(parseRoad(geometricFigure, intersections));
								}
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

			return new Graph(intersections, roads);
			//this.costsMatrix = Cost.floydWarshall(this);
		}
		
		private static AbstractIntersection parseIntersection(Element geometricFigure) throws IllegalArgumentException{
			if(geometricFigure.getNodeName() == "circle" ){
				CartesianCoordinate center = new CartesianCoordinate(Float.parseFloat(geometricFigure.getAttribute("cx")),
						Float.parseFloat(geometricFigure.getAttribute("cy")));
				Float radius = Float.parseFloat(geometricFigure.getAttribute("r"));
				AbstractIntersection parseIntersection = new AbstractIntersection(center, radius);
				System.out.println("Intersection " + parseIntersection.identifier + " parsed: " + parseIntersection);
				return parseIntersection;
			} else {
				throw new IllegalArgumentException("Not a circle");
			}

		}
		
		private static Road parseRoad(Element geometricFigure, ArrayList<AbstractIntersection> intersections) throws IllegalArgumentException{
			if(geometricFigure.getNodeName() != "path" ){
				throw new IllegalArgumentException(geometricFigure.getAttribute("id") + " is not a path");
			}
			
			CartesianCoordinate point1;
			CartesianCoordinate point2;
			AbstractIntersection origin = null;
			AbstractIntersection destination = null;
			Cost cost;
			int lane = 1; // Attribut pas encore implémenté			
			
			
			//Détermination des attributs géométriques
			String d=geometricFigure.getAttribute("d");			// d contient les coordonnées du path
			// d est sous la forme d="m 395.9798,236.15895 6.06091,165.66502"
			String [] dParse = d.split("( |,)"); // Utilisation de regex
			if(dParse[0].equals("M") || dParse[0].equals("m")){
				if(dParse[0].equals("M")){
					point1 = new CartesianCoordinate(Float.parseFloat(dParse[1]), Float.parseFloat(dParse[2]));
					point2 = new CartesianCoordinate(Float.parseFloat(dParse[3]), Float.parseFloat(dParse[4]));
				} else { //dParse[0].equals("m")
					point1 = new CartesianCoordinate(Float.parseFloat(dParse[1]), Float.parseFloat(dParse[2]));
					point2 = new CartesianCoordinate(point1.x + Float.parseFloat(dParse[3]), point1.y + Float.parseFloat(dParse[4]));
				}
			}else{
				throw new IllegalArgumentException("Cannot parse " + geometricFigure.getAttribute("id"));
			}


			// Détermination de origin et destination
			Boolean isIntegrated = false;
			// Vérification de point1
			for(int i= 0; i<intersections.size(); i ++){
				if(point1.distanceFrom(intersections.get(i).center) <= intersections.get(i).radius){
					// On cherche alors pour le point2    
					for(int j=0; j<intersections.size(); j++){
						if(point2.distanceFrom(intersections.get(j).center) <= intersections.get(j).radius){
							// Le Road j a deux extrémités dans des intersections
							//On renseigne les informations de l'arête
							origin = intersections.get(i);
							destination = intersections.get(j);
							isIntegrated = true;
						}
					}
				}

			}
			if(! isIntegrated){
				throw new IllegalArgumentException(geometricFigure.getAttribute("id") + " has not been integrated to the graph");
			}


			// Initialisation de cost
			// Le coût est pour l'instant la longueur de la route sur la vitesse urbaine standard de 50 km/h
			cost = new Cost(CartesianCoordinate.distance(point1, point2)/50000.);

			// Instanciation de Road
			Road parseRoad = new Road(point1, point2, origin, destination, cost, lane);
			
			// Renseignement de origin d'un nouveau leavingRoad
			origin.getLeavingRoads().add(parseRoad);

			System.out.println("Road " + parseRoad.identifier + " parsed: " +
					parseRoad.point1 + "; " + parseRoad.point2);
			//this.roads.add(parseRoad);
			return parseRoad;
		}

}