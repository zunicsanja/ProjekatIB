package ib.project.core;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream.GetField;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.Node;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import ib.project.model.Image;

public class CreateXMLFileJava {
	
	
	public static final String xmlFilePath = "D:\\ProjekatIB\\IB_Project_Desktop\\data\\xmlfile.xml";
	public static void main(String argv[]) throws IOException {
		
		Image image = new Image();
		
        try {
        	File folder = new File("C:\\Users\\Win7\\Desktop\\slike");
    		File[] listOfFiles = folder.listFiles();

    		for (int i = 0; i < listOfFiles.length; i++) {
    		  if (listOfFiles[i].isFile()) {
    		    System.out.println("File " + listOfFiles[i].getName());
    		  } else if (listOfFiles[i].isDirectory()) {
    		    System.out.println("Directory " + listOfFiles[i].getName());
    		  }
    		}
 
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
 
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
 
            Document document = documentBuilder.newDocument();
            document.setXmlStandalone(true);
            
            Element storage = document.createElement("storage");
            document.appendChild(storage);
            
            Element userName = document.createElement("username");
            userName.appendChild(document.createTextNode("John Smith"));
            storage.appendChild(userName);
            
	        Element images = document.createElement("images");
	        storage.appendChild(images);
	        
            
	        for(int i = 0; i<listOfFiles.length; i++) {
	        	Element imageElement = document.createElement("image");
		        images.appendChild(imageElement);
		        
	        	Element imageName = document.createElement("name");
	        	imageName.setTextContent(listOfFiles[i].getName());
	        	imageElement.appendChild(imageName);
	        	
	        	File f = new File("C:\\Users\\Win7\\Desktop\\slike\\cat3.jpg");
	        	
	        	Element width = document.createElement("width");
				BufferedImage imagee = ImageIO.read(f);
				int iwidth = imagee.getWidth();
				width.setTextContent(Integer.toString(iwidth));
				imageElement.appendChild(width);
				
				Element height = document.createElement("height");
				int iheight = imagee.getHeight();
				height.setTextContent(Integer.toString(iheight));
				imageElement.appendChild(height);
	        }
	        
	        
			
			
			/*File f = new File("C:\\Users\\Win7\\Desktop\\slike");
			for(int i = 0; i<listOfFiles.length; i++) {
				Element imageElement = document.createElement("image");
		        images.appendChild(imageElement);
				Element width = document.createElement("width");
				
				BufferedImage imagee = ImageIO.read(f);
				int iwidth = imagee.getWidth();
				width.setTextContent(Integer.toString(iwidth));
				image.appendChild(width);
			}*/
			
			
		        
			/*Element height = document.createElement("height");
			File ff = new File("C:\\Users\\Win7\\Desktop\\slike\\cat3.jpg");
			for(int i = 0; i<listOfFiles.length; i++) {
				BufferedImage imagee = ImageIO.read(ff);
				int iheight = imagee.getHeight();
				height.setTextContent(Integer.toString(iheight));
				images.appendChild(height);
			}*/
            
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
