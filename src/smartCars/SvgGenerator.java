package smartCars;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;

/* Ce fichier permet de générer une image svg qui représente l'état d'un GraphState à l'instant t
    Il est inspiré de http://research.jacquet.xyz/teaching/java/xml-dom/ dont l'auteur est Christophe Jacquet
 */

public class SvgGenerator {

    static Document doc = null;


    public SvgGenerator(Graph graph){

        doc = createSvgStructure();
        Element element = doc.getDocumentElement();
        addRoadToElement(element);
        // l'écrire sur le disque dans un fichier
        ecrireDocument(doc, "media/output.svg");
    }

    private static Element addRoadToElement(Element element){
        Element path = doc.createElement("path");
        path.setAttribute("d", "M 0,0 100,100");
        path.setAttribute("style", "fill:none;fill-rule:evenodd;stroke:#000000;stroke-width:1px;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1");
        element.appendChild(path);
        return element;
    }

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

    private static void ecrireDocument(Document doc, String nomFichier) {
        // on considère le document "doc" comme étant la source d'une
        // transformation XML
        Source source = new DOMSource(doc);

        // le résultat de cette transformation sera un flux d'écriture dans
        // un fichier
        Result resultat = new StreamResult(new File(nomFichier));

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
