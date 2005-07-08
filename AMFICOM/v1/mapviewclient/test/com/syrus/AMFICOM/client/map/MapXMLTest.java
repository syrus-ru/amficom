/**
 * $Id: MapXMLTest.java,v 1.1 2005/07/08 06:39:33 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.client.map;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.syrus.amficom.general.xml.UID;
import com.syrus.amficom.map.xml.Characteristic;
import com.syrus.amficom.map.xml.CharacteristicTypeSort;
import com.syrus.amficom.map.xml.Characteristics;
import com.syrus.amficom.map.xml.LibraryDocument;
import com.syrus.amficom.map.xml.MapLibrary;
import com.syrus.amficom.map.xml.PhysicalLinkType;
import com.syrus.amficom.map.xml.PhysicalLinkTypeSort;
import com.syrus.amficom.map.xml.PhysicalLinkTypes;
import com.syrus.amficom.map.xml.SiteNodeType;
import com.syrus.amficom.map.xml.SiteNodeTypeSort;
import com.syrus.amficom.map.xml.SiteNodeTypes;
import com.syrus.amficom.map.xml.VisualCharacteristicType;

public class MapXMLTest {

	public MapXMLTest() {
		super();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MapXMLTest sample = new MapXMLTest();

        // Create an schema type instance from the XML indicated by the path.
        XmlObject doc = sample.parseXml(args[0]);

		sample.printInstance(doc);

        // Validate the XML.
        boolean exampleIsValid = sample.validateXml(doc);
        assert exampleIsValid;

        //Creating a new XML document
		sample.createDocument(args[1]);
	}

	   /**
	    * Creates a File from the XML path provided in main arguments, then
	    * parses the file's contents into a type generated from schema.
	    */
	   public XmlObject parseXml(String file){
	       // Get the XML instance into a file using the path provided.
	       File xmlfile = new File(file);

	       // Create an instance of a type generated from schema to hold the XML.
		   XmlObject doc = null;

	       try {
	           // Parse the instance into the type generated from the schema.
	           doc = LibraryDocument.Factory.parse(xmlfile);
	       }
	       catch(XmlException e){
	           e.printStackTrace();
	       }
	       catch(IOException e){
	           e.printStackTrace();
	       }
	       return doc;
	   }

	   /**
	    * This method prints first occurence of <important-date>
	    * value. It also prints converted values from  XMLBean types to Java types
	    * (java.util.Date, java.util.Calendar) and org.apache.xmlbeans.GDate.
	    */
	   public void printInstance(XmlObject doc){
	       // Retrieve the <datetime> element and get an array of
	       // the <important-date> elements it contains.
		   Node types = doc.getDomNode();
	       NodeList nodes = types.getChildNodes();

	       // Loop through the <important-date> elements, printing the
	       // values for each.
	       for (int i=0;i<nodes.getLength();i++){

	           //For purpose of simplicity in output, only first occurrence is printed.
	           if (i == 0){
				   
	               System.out.println("toString: " + nodes.item(i));
	               System.out.println("\n\n");
	           }
	       }

	   }

	   /**
	    * This method creates an new <important-date> element and attaches to the existing XML Instance, and saves the
	    * new Instance to a file(args[1]).
	    */
	   public void createDocument(String file){
		   LibraryDocument doc;

	       XmlOptions xmlOptions = new XmlOptions();
	       xmlOptions.setSavePrettyPrint();
		   Map prefixes = new HashMap();
		   prefixes.put("http://syrus.com/AMFICOM/map/xml", "map");
		   prefixes.put("http://syrus.com/AMFICOM/general/xml", "gen");
		   xmlOptions.setSaveSuggestedPrefixes(prefixes);

		   doc = LibraryDocument.Factory.newInstance(xmlOptions);
		   MapLibrary lib = doc.addNewLibrary();
		   SiteNodeTypes types = lib.addNewSitenodetypes();
	       SiteNodeType node = types.addNewSitenodetype();
		   UID uid = node.addNewUid();
		   uid.setStringValue("test");
		   node.setDescription("desc");
		   node.setImage("img");
		   node.setName("namma");
		   node.setSort(SiteNodeTypeSort.BUILDING);
		   node.setTopological(true);

		   PhysicalLinkTypes ltypes = lib.addNewPhysicallinktypes();
	       PhysicalLinkType lnode = ltypes.addNewPhysicallinktype();
		   uid = lnode.addNewUid();
		   uid.setStringValue("test1");
		   lnode.setDescription("desc2");
		   lnode.setName("namma2");
		   lnode.setSort(PhysicalLinkTypeSort.INDOOR);
		   lnode.setDimensionX(BigInteger.valueOf(1));
		   lnode.setDimensionY(BigInteger.valueOf(1));
		   Characteristics chars = lnode.addNewCharacteristics();
		   Characteristic char1 = chars.addNewCharacteristic();
		   char1.setSort(CharacteristicTypeSort.VISUAL);
		   char1.setName(VisualCharacteristicType.STYLE);
		   char1.setValue("dashed");
		   Characteristic char2 = chars.addNewCharacteristic();
		   char2.setSort(CharacteristicTypeSort.VISUAL);
		   char2.setName(VisualCharacteristicType.COLOR);
		   char2.setValue(String.valueOf(0xFF004000));
		   Characteristic char3 = chars.addNewCharacteristic();
		   char3.setSort(CharacteristicTypeSort.VISUAL);
		   char3.setName(VisualCharacteristicType.THICKNESS);
		   char3.setValue(String.valueOf(1));

		   printInstance(doc);
	       // Validate the new XML
	       boolean isXmlValid = validateXml(doc);
	       if (isXmlValid) {
	           File f = new File(file);

	           try{
	               //Writing the XML Instance to a file.
	               doc.save(f,xmlOptions);
	           }
	           catch(IOException e){
	               e.printStackTrace();
	           }
	           System.out.println("\nXML Instance Document saved at : " + f.getPath());
	       }
	   }

	   /**
	    * <p>Validates the XML, printing error messages when the XML is invalid. Note
	    * that this method will properly validate any instance of a compiled schema
	    * type because all of these types extend XmlObject.</p>
	    *
	    * <p>Note that in actual practice, you'll probably want to use an assertion
	    * when validating if you want to ensure that your code doesn't pass along
	    * invalid XML. This sample prints the generated XML whether or not it's
	    * valid so that you can see the result in both cases.</p>
	    *
	    * @param xml The XML to validate.
	    * @return <code>true</code> if the XML is valid; otherwise, <code>false</code>
	    */
	   public boolean validateXml(XmlObject xml)
	   {
	       boolean isXmlValid = false;

	       // A collection instance to hold validation error messages.
	       ArrayList validationMessages = new ArrayList();

	       // Validate the XML, collecting messages.
	       isXmlValid = xml.validate(new XmlOptions().setErrorListener(validationMessages));

	       if (!isXmlValid)
	       {
	           System.out.println("Invalid XML: ");
	           for (int i = 0; i < validationMessages.size(); i++)
	           {
	               XmlError error = (XmlError) validationMessages.get(i);
	               System.out.println(error.getMessage());
	               System.out.println(error.getObjectLocation());
	           }
	       }
	       else
	           System.out.println("XML " + xml.documentProperties().getSourceName() + " is valid.\n");
	       return isXmlValid;
	   }
}
