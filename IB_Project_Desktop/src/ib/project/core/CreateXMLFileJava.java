package ib.project.core;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream.GetField;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.text.DateFormatter;
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
	public static void main(String argv[]) throws IOException, NoSuchAlgorithmException {
		
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
				
				Element hash = document.createElement("hash");
				String path = "C:\\Users\\Win7\\Desktop\\slike";

		        MessageDigest md = MessageDigest.getInstance("MD5");
		        byte[] hashInBytes = md.digest(path.getBytes(StandardCharsets.UTF_8));

		        StringBuilder sb = new StringBuilder();
		        for (byte b : hashInBytes) {
		            sb.append(String.format("%02x", b));
		        }
		        hash.setTextContent(sb.toString());
				imageElement.appendChild(hash);
	        }
            
            Element date = document.createElement("date");
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date datee = new Date();
            date.appendChild(document.createTextNode(dateFormat.format(datee)));
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
