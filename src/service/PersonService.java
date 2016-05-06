/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.io.File;
import java.io.IOException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import model.Person;
import model.PersonCollection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author duonghung1269
 */
public class PersonService {

    public static PersonCollection loadPersonList(File file) {
        try {

//		File file = new File("src/data/clubMember.xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(PersonCollection.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            PersonCollection personCollection = (PersonCollection) jaxbUnmarshaller.unmarshal(file);
            System.out.println(personCollection);
            return personCollection;
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        return new PersonCollection();
    }

    public static void marshalPersonCollection(File file, PersonCollection personCollection) {
        try {

//		File file = new File("src/data/clubMember.xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(PersonCollection.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            // output pretty printed
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            jaxbMarshaller.marshal(personCollection, file);
            jaxbMarshaller.marshal(personCollection, System.out);

        } catch (JAXBException e) {
            e.printStackTrace();
        }

    }

    public static Document getDefaultDocument() throws ParserConfigurationException {
        DocumentBuilderFactory dbFactory
                = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder
                = dbFactory.newDocumentBuilder();
        return dBuilder.newDocument();
    }

    public static Document getDocumentFrom(File file) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory dbFactory
                = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(file);
        doc.getDocumentElement().normalize();

        return doc;
    }

    public static PersonCollection readPersonListFrom(Document doc) throws ParserConfigurationException, SAXException, IOException {
        PersonCollection personCollection = new PersonCollection();

        NodeList nList = doc.getElementsByTagName("person");
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            System.out.println("\nCurrent Element :"
                    + nNode.getNodeName());
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                String name = eElement
                        .getElementsByTagName("name")
                        .item(0)
                        .getTextContent();
                String age = eElement
                        .getElementsByTagName("age")
                        .item(0)
                        .getTextContent();
                String gender = eElement
                        .getElementsByTagName("gender")
                        .item(0)
                        .getTextContent();
                personCollection.getPersonList().add(new Person(name, Integer.parseInt(age), gender));
            }
        }

        return personCollection;
    }

    public static void savePersonCollectionToFile(Document doc, File file) throws ParserConfigurationException, SAXException, IOException, TransformerConfigurationException, TransformerException {
        TransformerFactory transformerFactory
                = TransformerFactory.newInstance();
        Transformer transformer
                = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "10");
        DOMSource source = new DOMSource(doc);
        StreamResult result
                = new StreamResult(file);

        transformer.transform(source, result);

        StreamResult consoleResult
                = new StreamResult(System.out);
        transformer.transform(source, consoleResult);
    }

    public static void addPersonToDocument(Document doc, Person person) {
        Node people = doc.getElementsByTagName("people").item(0);
        Element newPerson = doc.createElement("person");

        Element name = doc.createElement("name");
        name.setTextContent(person.getName());
        Element age = doc.createElement("age");
        age.setTextContent("" + person.getAge());
        Element gender = doc.createElement("gender");
        gender.setTextContent(person.getGender());

        newPerson.appendChild(name);
        newPerson.appendChild(age);
        newPerson.appendChild(gender);

        people.appendChild(newPerson);

        doc.getDocumentElement().normalize();
    }

    public static void updatePersonInDocument(Document doc, Person person, int index) {
        NodeList nList = doc.getElementsByTagName("person");
        Node updateNode = nList.item(index);
        if (updateNode.getNodeType() == Node.ELEMENT_NODE) {
            Element eElement = (Element) updateNode;
            eElement
                    .getElementsByTagName("name")
                    .item(0)
                    .setTextContent(person.getName());
            eElement
                    .getElementsByTagName("age")
                    .item(0)
                    .setTextContent(person.getAge() + "");
            eElement
                    .getElementsByTagName("gender")
                    .item(0)
                    .setTextContent(person.getGender());
        }

    }

    public static void deletePersonInDocument(Document doc, int index) {

        NodeList nList = doc.getElementsByTagName("person");
        Node deleteNode = nList.item(index);
        deleteNode.getParentNode().removeChild(deleteNode);

    }
}
