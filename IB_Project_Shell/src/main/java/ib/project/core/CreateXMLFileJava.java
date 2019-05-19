package ib.project.core;


import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class CreateXMLFileJava {
	public static final String xmlFilePath = "C:\\ProjekatIB\\IB_Project_Shell\\data\\xmlfile.xml";
	
	public static void main(String argv[]) {
		 
        try {
 
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
 
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
 
            Document document = documentBuilder.newDocument();
            
            Element storage = document.createElement("storage");
            document.appendChild(storage);
            
            Element userName = document.createElement("username");
            userName.appendChild(document.createTextNode("John Smith"));
            storage.appendChild(userName);
            
            Element image = document.createElement("image");
            storage.appendChild(image);
            
            Element name = document.createElement("name");
            name.appendChild(document.createTextNode("cat.jpg"));
            image.appendChild(name);
            
            Element size = document.createElement("size");
            size.appendChild(document.createTextNode("144 KB"));
            image.appendChild(size);
            
            Element date = document.createElement("date");
            date.appendChild(document.createTextNode("2019-05-17"));
            storage.appendChild(date);
            
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File(xmlFilePath));
            
            transformer.transform(domSource, streamResult);
            
            System.out.println("Done creating XML File");
 
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        }
    }
}
