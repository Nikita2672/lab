package com.iwaa.server.parser;

import com.iwaa.common.data.Coordinates;
import com.iwaa.common.data.Location;
import com.iwaa.common.data.Route;
import com.iwaa.common.io.CollectionFileReader;
import com.iwaa.common.io.CollectionFileWriter;
import com.iwaa.server.collection.CollectionAdminImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class Parser implements CollectionFileReader<CollectionAdminImpl>, CollectionFileWriter<CollectionAdminImpl> {

    @Override
    public CollectionAdminImpl read(File file) {
        HashSet<Route> routes = new HashSet<>();
        Document doc;
        try {
            doc = buildDocument(file);
        } catch (ParserConfigurationException | IOException | SAXException e) {
            System.out.println("Wrong configuration, your file will be overwrite if it possible");
            HashSet<Route> routes1 = new HashSet<>();
            return new CollectionAdminImpl(routes1);
        }
        Node myCollectionNode = doc.getFirstChild();
        NodeList myCollectionChild = myCollectionNode.getChildNodes();
        Node routeNode = null;
        for (int i = 0; i < myCollectionChild.getLength(); i++) {
            if (myCollectionChild.item(i).getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            if (myCollectionChild.item(i).getNodeName().equals("Route")) {
                routeNode = myCollectionChild.item(i);
            }
        }
        if (routeNode == null) {
            return new CollectionAdminImpl(routes);
        }
        NodeList routeChildren = routeNode.getChildNodes();
        for (int i = 0; i < routeChildren.getLength(); i++) {
            if (routeChildren.item(i).getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            if (!routeChildren.item(i).getNodeName().equals("element")) {
                continue;
            }
            NodeList elementChild = routeChildren.item(i).getChildNodes();
            Route route = parsRoute(elementChild);
            routes.add(route);
        }
        return new CollectionAdminImpl(routes);
    }

    private Document buildDocument(File file) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db;
        db = dbf.newDocumentBuilder();
        db.setErrorHandler(null);
        return db.parse(file);
    }

    public static Date parseDate(String s) {
        SimpleDateFormat parser = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        try {
            return parser.parse(s);
        } catch (ParseException e) {
            return null;
        }
    }

    private static Route parsRoute(NodeList elementChild) {
        Long id = 0L;
        String name = null;
        Date creationDate = null;
        Long distance = null;
        Coordinates coordinates = null;
        Location from = null;
        Location to = null;
        for (int j = 0; j < elementChild.getLength(); j++) {
            if (elementChild.item(j).getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            if (elementChild.item(j).getNodeName().equals("id")) {
                id = Long.parseLong(elementChild.item(j).getTextContent());
            }
            if (elementChild.item(j).getNodeName().equals("name")) {
                name = elementChild.item(j).getTextContent();
            }
            if (elementChild.item(j).getNodeName().equals("date")) {
                String s = elementChild.item(j).getTextContent();
                creationDate = parseDate(s);
            }
            if (elementChild.item(j).getNodeName().equals("distance")) {
                distance = Long.valueOf(elementChild.item(j).getTextContent());
            }
            if (elementChild.item(j).getNodeName().equals("coordinates")) {
                NodeList coordinatesChild = elementChild.item(j).getChildNodes();
                coordinates = parsCoordinates(coordinatesChild);
            }
            if (elementChild.item(j).getNodeName().equals("from")) {
                NodeList fromChild = elementChild.item(j).getChildNodes();
                from = parsLocation(fromChild);
            }
            if (elementChild.item(j).getNodeName().equals("to")) {
                NodeList fromChild = elementChild.item(j).getChildNodes();
                to = parsLocation(fromChild);
            }
        }
        return new Route(id, name, coordinates, creationDate, from, to, distance);
    }

    private static Coordinates parsCoordinates(NodeList coordinatesChild) {
        float x1 = 0;
        Float y1 = null;
        for (int z = 0; z < coordinatesChild.getLength(); z++) {
            if (coordinatesChild.item(z).getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            if (coordinatesChild.item(z).getNodeName().equals("x")) {
                x1 = Float.parseFloat(coordinatesChild.item(z).getTextContent());
            }
            if (coordinatesChild.item(z).getNodeName().equals("y")) {
                y1 = Float.parseFloat(coordinatesChild.item(z).getTextContent());
            }
        }
        return new Coordinates(y1, x1);
    }

    private static Location parsLocation(NodeList fromChild) {
        Long x2 = null;
        int y2 = 0;
        Integer z2 = null;
        for (int z = 0; z < fromChild.getLength(); z++) {
            if (fromChild.item(z).getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            if (fromChild.item(z).getNodeName().equals("x")) {
                x2 = Long.parseLong(fromChild.item(z).getTextContent());
            }
            if (fromChild.item(z).getNodeName().equals("y")) {
                y2 = Integer.parseInt(fromChild.item(z).getTextContent());
            }
            if (fromChild.item(z).getNodeName().equals("z")) {
                z2 = Integer.valueOf(fromChild.item(z).getTextContent());
            }
        }
        return new Location(x2, y2, z2);
    }
    @Override
    public boolean write(File file, CollectionAdminImpl collectionAdmin) {
        try {
            Set<Route> routes = collectionAdmin.getCollection();
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.newDocument();
            Element root = document.createElement("myCollection");
            document.appendChild(root);
            if (!routes.isEmpty()) {
                Element root1 = document.createElement("Route");
                root.appendChild(root1);
                for (Route route : routes) {
                    Element e = document.createElement("element");
                    Element id = document.createElement("id");
                    id.setTextContent(Long.toString(route.getId()));
                    e.appendChild(id);
                    Element name = document.createElement("name");
                    name.setTextContent(route.getName());
                    e.appendChild(name);
                    Element distance = document.createElement("distance");
                    distance.setTextContent(route.getDistance().toString());
                    e.appendChild(distance);
                    Element date = document.createElement("date");
                    date.setTextContent(route.getCreationDate().toString());
                    e.appendChild(date);
                    Node routNode = createClassesNodes(e, route, document);
                    root1.appendChild(routNode);
                }
            }
            document.normalizeDocument();
            writeDocument(document, file);
            return true;
        } catch (ParserConfigurationException | FileNotFoundException e) {
            return false;
        }
    }

    private static Element createClassesNodes(Element e, Route route, Document document) {
        Element coordinates = document.createElement("coordinates");
        Element x1 = document.createElement("x");
        x1.setTextContent(Float.toString(route.getCoordinates().getX()));
        Element y1 = document.createElement("y");
        y1.setTextContent(Float.toString(route.getCoordinates().getY()));
        coordinates.appendChild(x1);
        coordinates.appendChild(y1);
        e.appendChild(coordinates);
        Element from = document.createElement("from");
        Element x2 = document.createElement("x");
        x2.setTextContent(Long.toString(route.getFrom().getX()));
        Element y2 = document.createElement("y");
        y2.setTextContent(Integer.toString(route.getFrom().getY()));
        Element z2 = document.createElement("z");
        z2.setTextContent(Integer.toString(route.getFrom().getZ()));
        from.appendChild(x2);
        from.appendChild(y2);
        from.appendChild(z2);
        e.appendChild(from);
        Element to = document.createElement("to");
        Element x3 = document.createElement("x");
        x3.setTextContent(Long.toString(route.getTo().getX()));
        Element y3 = document.createElement("y");
        y3.setTextContent(Integer.toString(route.getTo().getY()));
        Element z3 = document.createElement("z");
        z3.setTextContent(Integer.toString(route.getTo().getZ()));
        to.appendChild(x3);
        to.appendChild(y3);
        to.appendChild(z3);
        e.appendChild(to);
        return e;
    }

    public static void writeDocument(Document document, File file) throws FileNotFoundException {
        StreamResult streamResult = null;
        String res = null;
        try (PrintWriter printWriter = new PrintWriter(file)) {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            streamResult = new StreamResult(printWriter);
            transformer.transform(source, streamResult);
            res = streamResult.getWriter().toString();
        } catch (TransformerException e) {
            System.out.println("Wrong configuration");
        }
        try {
            if (res != null) {
                streamResult.getWriter().write(res);
            }
        } catch (IOException e) {
            System.out.println("There is no such file or you haven't permissions");
        }
    }
}
