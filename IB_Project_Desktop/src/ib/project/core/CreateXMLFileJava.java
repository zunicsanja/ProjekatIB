package ib.project.core;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream.GetField;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

import javax.imageio.ImageIO;
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
	public static void main(String argv[]) throws IOException, NoSuchAlgorithmException {
		
		ArrayList<String> fileNames = new ArrayList<>();
		
        try {
        	File folder = new File("C:\\Users\\Emilija\\Desktop\\slikeIB");
    		File[] listOfFiles = folder.listFiles();

    		for (int i = 0; i < listOfFiles.length; i++) {
    		  if (listOfFiles[i].isFile()) {
    		    System.out.println("File " + listOfFiles[i].getName());
    		    fileNames.add(listOfFiles[i].getName().toString());
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
	        	
	        	
	        	Element width = document.createElement("width");
				BufferedImage imagee = ImageIO.read(listOfFiles[i]);
				int iwidth = imagee.getWidth();
				width.setTextContent(Integer.toString(iwidth));
				imageElement.appendChild(width);
				
				Element height = document.createElement("height");
				int iheight = imagee.getHeight();
				height.setTextContent(Integer.toString(iheight));
				imageElement.appendChild(height);
				
				Element hash = document.createElement("hash");
				
				try {
			          MessageDigest md5Digest = MessageDigest.getInstance("MD5");
			          String checksum = getFileChecksum(md5Digest, listOfFiles[i]);
			          hash.setTextContent(checksum);
			          imageElement.appendChild(hash);
				} catch (Exception e) {
			          System.out.println("Greska prilikom generisanja md5 hasha fajla/slike!");
			          e.printStackTrace();
			        }
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
	
	private static String getFileChecksum(MessageDigest digest, File file) throws IOException
	  {
	      //Get file input stream for reading the file content
	      FileInputStream fis = new FileInputStream(file);
	      
	      //Create byte array to read data in chunks
	      byte[] byteArray = new byte[1024];
	      int bytesCount = 0;
	        
	      //Read file data and update in message digest
	      while ((bytesCount = fis.read(byteArray)) != -1) {
	          digest.update(byteArray, 0, bytesCount);
	      };
	      
	      //close the stream; We don't need it now.
	      fis.close();
	      
	      //Get the hash's bytes
	      byte[] bytes = digest.digest();
	      
	      //This bytes[] has bytes in decimal format;
	      //Convert it to hexadecimal format
	      StringBuilder sb = new StringBuilder();
	      for(int i=0; i< bytes.length ;i++)
	      {
	          sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
	      }
	      
	      //return complete hash
	    return sb.toString();
	  }
	
}
