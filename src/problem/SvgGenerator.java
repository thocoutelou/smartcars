package problem;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.util.Stack;

import graph.AbstractIntersection;
import graph.Graph;
import graph.Road;
import resources.CartesianCoordinate;
import smartcars.AbstractVehicle;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;
import java.io.File;
import java.io.IOException;
import java.util.IllegalFormatException;

/* Ce fichier permet de générer une image svg qui représente l'état d'un GraphState à l'instant t
   Il est inspiré de http://research.jacquet.xyz/teaching/java/xml-dom/ dont l'auteur est Christophe Jacquet
   @author thomas
 */

public class SvgGenerator {

    private static Document doc = null;
    private CartesianCoordinate viewBox1 = new CartesianCoordinate(0,0);
    private CartesianCoordinate viewBox2 = new CartesianCoordinate(0,0);

    /*
    COnstructeur de la classe. Il permet de générer un fichier SVG d'un Graph ou d'un GraphState à un instant donné.
    Cette méthode appelle les autres méthodes de la classe, pour ajouter successivement au document les intersections, les routes et les véhicules.
    Enfin, elle écrit le document avec la méthode writeDocument
     */
    public SvgGenerator(Graph graph, File outputFile){

        doc = createSvgStructure();
        Element element = doc.getDocumentElement();

        //Ajout des Intersections
        for (AbstractIntersection intersection: graph.getIntersections()){
            addIntersectionToElement(element, intersection);
        }

        // Ajout des Road au document
        for (Road road : graph.getRoads()){
            addRoadToElement(element,road);
        }

        System.out.println(graph.getClass());
        if (graph.getClass() == problem.GraphState.class){
            addVehicleToElement(element, ((GraphState) graph).getVehicles());
        }

        setViewBox(graph,element);
        // Ecriture du document sur le disque dans un fichier
        writeDocuement(doc, outputFile);
    }

    /*
    Cette méthode permet d'ajouter une intersection à un document(org.w3c.dom.Document), ici doc. L'élément Element element permet
    de connaître l'endroit où il faut insérer l'intersection.
    Pour des questions pratique, un numéro d'intersection est également ajouté au document.
     */

    private static Element addIntersectionToElement(Element element, AbstractIntersection intersection){
        // Ajout du cercle
        Element circle = doc.createElement("circle");
        circle.setAttribute("cx", String.valueOf(intersection.center.x));
        circle.setAttribute("cy", String.valueOf(intersection.center.y));
        circle.setAttribute("r", String.valueOf(intersection.radius));
        circle.setAttribute("style", "fill:#ff0000;fill-rule:evenodd;stroke:#000000;stroke-width:1px;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1");
        element.appendChild(circle);

        //Ajout d'un légende (texte)
        Element text = doc.createElement("text");
        text.setAttribute("x", String.valueOf(intersection.center.x));
        text.setAttribute("y", String.valueOf(intersection.center.y));
        text.setAttribute("text-anchor", "middle");
        text.setAttribute("fill", "black");
        String style = "font-size:" + String.valueOf(intersection.radius);
        text.setAttribute("style", style);
        text.setTextContent(String.valueOf(intersection.identifier));
        element.appendChild(text);
        //<text x="541.8071899414062" y="552.707275390625" text-anchor="middle" fill="black" stroke-width="2px" dy=".3em">Look, I’m centered!Look, I’m centered!</text>

        return element;
    }

    /*
    Cette méthode permet comme la précédente d'ajouter un Road à doc, dans l'élément element.
     */
    private static Element addRoadToElement(Element element, Road road){
        Element path = doc.createElement("path");
        String d = "M " + road.getPoint1().x + "," + road.getPoint1().y + " " + road.getPoint2().x + "," + road.getPoint2().y;
        path.setAttribute("d", d);
        path.setAttribute("style", "fill:none;fill-rule:evenodd;stroke:#000000;stroke-width:1px;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1");
        element.appendChild(path);
        return element;
    }
    /*
    Cette méthode permet de créer la structure de base d'un document destiné à devenir un fichier svg.
    Elle utilise le modèle media/svg_template.svg
     */
    private static Document createSvgStructure(){

        String template_location = "media/svg_template.svg";

        try {
            final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            final DocumentBuilder builder = factory.newDocumentBuilder();
            doc= builder.parse(new File(template_location));

        } catch (final ParserConfigurationException e) {
            e.printStackTrace();
        } catch (final SAXException e) {
            e.printStackTrace();
        } catch (final IOException e) {
            e.printStackTrace();
        }

        return doc;
    }
    /*
    Cette méthode permet d'adapter la fenêtre graphique d'un document svg à l'ensemble des objets qu'il contient.
    Cela permet de tous les voir, dans le cas de graph de grande dimension
     */
    private void setViewBox(Graph graph, Element element){
        for (AbstractIntersection intersection : graph.getIntersections()) {
            viewBox1.x=Math.min(viewBox1.x, intersection.center.x - intersection.radius);
            viewBox1.y=Math.min(viewBox1.y, intersection.center.y - intersection.radius);
            viewBox2.x=Math.max(viewBox2.x, intersection.center.x + intersection.radius);
            viewBox2.y=Math.max(viewBox2.y, intersection.center.y + intersection.radius);
            String viewBox = viewBox1.x +" " +viewBox1.y+" "+viewBox2.x+" "+viewBox2.y;
            System.out.println(viewBox);
        }
        String viewBox = viewBox1.x +" " +viewBox1.y+" "+viewBox2.x+" "+viewBox2.y;
        element.setAttribute("viewBox", viewBox);
    }
    /*
    Cette méthode permet d'ajouter un AbstractVehicule dans doc, fils de element.
     */
    private void addVehicleToElement(Element element, Stack<AbstractVehicle> vehicles){
        for(AbstractVehicle vehicle : vehicles){
            Element rect = doc.createElement("rect");
            rect.setAttribute("style", "fill:#0000ff;fill-rule:evenodd;stroke:#000000;stroke-width:1px;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1");
            rect.setAttribute("width", "10");
            rect.setAttribute("height", "15");
            CartesianCoordinate finalPosition = vehicle.getLocation().finalRoad.getPositionCartesianCoordinate(vehicle.getLocation().finalPosition);
            rect.setAttribute("x_destination", String.valueOf(finalPosition.x));
            rect.setAttribute("y_destination", String.valueOf(finalPosition.y));
            CartesianCoordinate currentPosition = vehicle.getLocation().currentRoad.getPositionCartesianCoordinate(vehicle.getLocation().currentPosition);
            rect.setAttribute("x", String.valueOf(currentPosition.x));
            rect.setAttribute("y", String.valueOf(currentPosition.y));
            element.appendChild(rect);
        }
        /*    <rect
       style="fill:#0000ff;fill-rule:evenodd;stroke:#000000;stroke-width:1px;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1"
       id="rect3372"
       width="9.4592094"
       height="16.55261"
       x="165.85022"
       y="242.63161"
       x_destination="605"
       y_destination="718"
       /> */
    }
    /*
    Cette méthode permet de convertir un document org.w3c.dom.Document en un fichier svg : outputFile
     */
    private static void writeDocuement(Document doc, File outputFile) {
        // on considère le document "doc" comme étant la source d'une
        // transformation XML
        Source source = new DOMSource(doc);

        // le résultat de cette transformation sera un flux d'écriture dans
        // un fichier
        Result resultat = new StreamResult(outputFile);

        // création du transformateur XML
        Transformer transfo = null;
        try {
            transfo = TransformerFactory.newInstance().newTransformer();
        } catch (TransformerConfigurationException e) {
            System.err.println("Impossible de créer un transformateur XML.");
            System.exit(1);
        }

        // configuration du transformateur

        // sortie en XML
        transfo.setOutputProperty(OutputKeys.METHOD, "xml");

        // inclut une déclaration XML (recommandé)
        transfo.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");

        // codage des caractères : UTF-8. Ce pourrait être également ISO-8859-1
        transfo.setOutputProperty(OutputKeys.ENCODING, "utf-8");

        // idente le fichier XML
        transfo.setOutputProperty(OutputKeys.INDENT, "yes");

        try {
            transfo.transform(source, resultat);
        } catch (TransformerException e) {
            System.err.println("La transformation a échoué : " + e);
            System.exit(1);
        }
    }
}
