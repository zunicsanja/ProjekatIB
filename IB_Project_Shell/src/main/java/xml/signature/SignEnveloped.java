package xml.signature;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.signature.XMLSignatureException;
import org.apache.xml.security.transforms.TransformationException;
import org.apache.xml.security.transforms.Transforms;
import org.apache.xml.security.utils.Constants;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class SignEnveloped {

	private static final String IN_FILE = "./data/xmlfile.xml";
	private static final String OUT_FILE = "./data/xmlfile_signed.xml";
	private static final String KEY_STORE_FILE = "./data/spring.keystore";
	
	static {
	  	//staticka inicijalizacija
	      Security.addProvider(new BouncyCastleProvider());
	      org.apache.xml.security.Init.init();
	  }
		
		public void testIt() {
			//ucitava se dokument
			Document doc = loadDocument(IN_FILE);
			
			//ucitava privatni kljuc koji ce biti iskoriscen za potpisivanje dokumenta
			PrivateKey pk = readPrivateKey();
			
			//ucitava sertifikat
			Certificate cert = readCertificate();
			
			//potpisuje
			System.out.println("Signing....");
			doc = signDocument(doc, pk, cert);
			
			//snima se dokument
			saveDocument(doc, OUT_FILE);
			System.out.println("Signing of document done");
		}
		
		/**
		 * Kreira DOM od XML dokumenta
		 */
		private Document loadDocument(String file) {
			try {
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				dbf.setNamespaceAware(true);
				DocumentBuilder db = dbf.newDocumentBuilder();
				Document document = db.parse(new File(file));

				return document;
			} catch (FactoryConfigurationError e) {
				e.printStackTrace();
				return null;
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
				return null;
			} catch (SAXException e) {
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		
		/**
		 * Snima DOM u XML fajl 
		 */
		private void saveDocument(Document doc, String fileName) {
			try {
				File outFile = new File(fileName);
				FileOutputStream f = new FileOutputStream(outFile);

				TransformerFactory factory = TransformerFactory.newInstance();
				Transformer transformer = factory.newTransformer();
				
				DOMSource source = new DOMSource(doc);
				StreamResult result = new StreamResult(f);
				
				transformer.transform(source, result);

				f.close();

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (TransformerConfigurationException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (TransformerFactoryConfigurationError e) {
				e.printStackTrace();
			} catch (TransformerException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		/**
		 * Ucitava sertifikat is KS fajla
		 * alias primer
		 */
		private Certificate readCertificate() {
			try {
				//kreiramo instancu KeyStore
				KeyStore ks = KeyStore.getInstance("JKS", "SUN");
				
				//ucitavamo podatke
				BufferedInputStream in = new BufferedInputStream(new FileInputStream(KEY_STORE_FILE));
				ks.load(in, "spring".toCharArray());
				
				if(ks.isKeyEntry("spring")) {
					Certificate cert = ks.getCertificate("spring");
					return cert;
					
				}
				else
					return null;
				
			} catch (KeyStoreException e) {
				e.printStackTrace();
				return null;
			} catch (NoSuchProviderException e) {
				e.printStackTrace();
				return null;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return null;
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
				return null;
			} catch (CertificateException e) {
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			} 
		}
		
		/**
		 * Ucitava privatni kljuc is KS fajla
		 * alias primer
		 */
		private PrivateKey readPrivateKey() {
			try {
				//kreiramo instancu KeyStore
				KeyStore ks = KeyStore.getInstance("JKS", "SUN");
				
				//ucitavamo podatke
				BufferedInputStream in = new BufferedInputStream(new FileInputStream(KEY_STORE_FILE));
				ks.load(in, "spring".toCharArray());
				
				if(ks.isKeyEntry("spring")) {
					PrivateKey pk = (PrivateKey) ks.getKey("spring", "spring".toCharArray());
					return pk;
				}
				else
					return null;
				
			} catch (KeyStoreException e) {
				e.printStackTrace();
				return null;
			} catch (NoSuchProviderException e) {
				e.printStackTrace();
				return null;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return null;
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
				return null;
			} catch (CertificateException e) {
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			} catch (UnrecoverableKeyException e) {
				e.printStackTrace();
				return null;
			} 
		}
		
		private Document signDocument(Document doc, PrivateKey privateKey, Certificate cert) {
	      
	      try {
				Element rootEl = doc.getDocumentElement();
				
				//kreira se signature objekat
				XMLSignature sig = new XMLSignature(doc, null, XMLSignature.ALGO_ID_SIGNATURE_RSA_SHA1);
				
				//kreiraju se transformacije nad dokumentom
				Transforms transforms = new Transforms(doc);
				    
				//iz potpisa uklanja Signature element
				//Ovo je potrebno za enveloped tip po specifikaciji
				transforms.addTransform(Transforms.TRANSFORM_ENVELOPED_SIGNATURE);
				
				//normalizacija
				transforms.addTransform(Transforms.TRANSFORM_C14N_WITH_COMMENTS);
				    
				//potpisuje se citav dokument (URI "")
				sig.addDocument("", transforms, Constants.ALGO_ID_DIGEST_SHA1);
				    
				//U KeyInfo se postavalja Javni kljuc samostalno i citav sertifikat
				sig.addKeyInfo(cert.getPublicKey());
				sig.addKeyInfo((X509Certificate) cert);
				    
				//poptis je child root elementa
				rootEl.appendChild(sig.getElement());
				
				//potpisivanje
				sig.sign(privateKey);
				
				return doc;
				
			} catch (TransformationException e) {
				e.printStackTrace();
				return null;
			} catch (XMLSignatureException e) {
				e.printStackTrace();
				return null;
			} catch (DOMException e) {
				e.printStackTrace();
				return null;
			} catch (XMLSecurityException e) {
				e.printStackTrace();
				return null;
			}
		}
		
		public static void main(String[] args) {
			SignEnveloped sign = new SignEnveloped();
			sign.testIt();
		}
}
