package smartCars;


import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

/* Ce fichier permet de générer une image svg qui représente l'état d'un GraphState à l'instant t
    Il est inspiré de http://research.jacquet.xyz/teaching/java/xml-dom/ dont l'auteur est Christophe Jacquet
 */

public class SvgGenerator {

    private static Document creerDocumentExemple(DocumentBuilder docBuilder) {
        Document doc = docBuilder.newDocument();

        Element racine = doc.createElement("root");
        racine.setAttribute("lang", "fr");
        doc.appendChild(racine);

        Element sujet = doc.createElement("node");
        sujet.setAttribute("role", "sujet");
        sujet.setTextContent("Supélec");
        racine.appendChild(sujet);

        Element verbe = doc.createElement("node");
        verbe.setAttribute("role", "verbe");
        verbe.setTextContent("est");
        racine.appendChild(verbe);

        Element complement = doc.createElement("node");
        complement.setAttribute("role", "complément de lieu");
        complement.setTextContent("en France");
        racine.appendChild(complement);

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

    public static void main(String[] args) {
        // obtention d'un Document Builder qui permet de créer de nouveaux
        // documents ou de parser des documents à partir de fichiers
        DocumentBuilder docBuilder = null;

        try {
            docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch(ParserConfigurationException e) {
            System.err.println("Impossible de créer un DocumentBuilder.");
            System.exit(1);
        }

        // crée un petit document d'exemple
        Document doc = creerDocumentExemple(docBuilder);

        // l'écrire sur le disque dans un fichier
        ecrireDocument(doc, "test.xml");
    }

}
