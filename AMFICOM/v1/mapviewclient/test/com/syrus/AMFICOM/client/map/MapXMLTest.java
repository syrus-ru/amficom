/**
 * $Id: MapXMLTest.java,v 1.9 2006/02/20 09:28:13 bass Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.client.map;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.syrus.AMFICOM.client.map.controllers.AbstractLinkController;
import com.syrus.AMFICOM.general.xml.XmlCharacteristic;
import com.syrus.AMFICOM.general.xml.XmlCharacteristicSeq;
import com.syrus.AMFICOM.general.xml.XmlIdentifier;
import com.syrus.AMFICOM.map.xml.MapLibraryDocument;
import com.syrus.AMFICOM.map.xml.XmlMapLibrary;
import com.syrus.AMFICOM.map.xml.XmlPhysicalLinkType;
import com.syrus.AMFICOM.map.xml.XmlPhysicalLinkTypeSeq;
import com.syrus.AMFICOM.map.xml.XmlPhysicalLinkTypeSort;
import com.syrus.AMFICOM.map.xml.XmlSiteNodeType;
import com.syrus.AMFICOM.map.xml.XmlSiteNodeTypeSeq;
import com.syrus.AMFICOM.map.xml.XmlSiteNodeTypeSort;
import com.syrus.util.Log;

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
	           doc = MapLibraryDocument.Factory.parse(xmlfile);
	       }
	       catch(XmlException e){
	           Log.errorMessage(e);
	       }
	       catch(IOException e){
	           Log.errorMessage(e);
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
				   
	               System.out.println("toString: " + nodes.item(i)); //$NON-NLS-1$
	               System.out.println("\n\n"); //$NON-NLS-1$
	           }
	       }

	   }

	   /**
	    * This method creates an new <important-date> element and attaches to the existing XML Instance, and saves the
	    * new Instance to a file(args[1]).
	    */
	   public void createDocument(String file){
		   MapLibraryDocument doc;

	       XmlOptions xmlOptions = new XmlOptions();
	       xmlOptions.setSavePrettyPrint();
		   Map prefixes = new HashMap();
		   prefixes.put("http://syrus.com/AMFICOM/map/xml", "map"); //$NON-NLS-1$ //$NON-NLS-2$
		   prefixes.put("http://syrus.com/AMFICOM/general/xml", "gen"); //$NON-NLS-1$ //$NON-NLS-2$
		   xmlOptions.setSaveSuggestedPrefixes(prefixes);

		   doc = MapLibraryDocument.Factory.newInstance(xmlOptions);
		   XmlMapLibrary lib = doc.addNewMapLibrary();
		   XmlSiteNodeTypeSeq types = lib.addNewSiteNodeTypes();
		   XmlSiteNodeType node = types.addNewSiteNodeType();
		   XmlIdentifier uid = node.addNewId();
		   uid.setStringValue("test"); //$NON-NLS-1$
		   node.setDescription("desc"); //$NON-NLS-1$
		   node.setImage("img"); //$NON-NLS-1$
		   node.setName("namma"); //$NON-NLS-1$
		   node.setSort(XmlSiteNodeTypeSort.BUILDING);
		   node.setTopological(true);

		   XmlPhysicalLinkTypeSeq ltypes = lib.addNewPhysicalLinkTypes();
		   XmlPhysicalLinkType lnode = ltypes.addNewPhysicalLinkType();
		   uid = lnode.addNewId();
		   uid.setStringValue("test1"); //$NON-NLS-1$
		   lnode.setDescription("desc2"); //$NON-NLS-1$
		   lnode.setName("namma2"); //$NON-NLS-1$
		   lnode.setSort(XmlPhysicalLinkTypeSort.INDOOR);
		   lnode.setDimensionX(1);
		   lnode.setDimensionY(1);
		   XmlCharacteristicSeq chars = lnode.addNewCharacteristics();
		   XmlCharacteristic char1 = chars.addNewCharacteristic();
		   // Broken
//		   char1.setTypecodename(AbstractLinkController.ATTRIBUTE_STYLE);
		   char1.setValue("dashed"); //$NON-NLS-1$
		   XmlCharacteristic char2 = chars.addNewCharacteristic();
//		   char2.setTypecodename(AbstractLinkController.ATTRIBUTE_COLOR);
		   char2.setValue(String.valueOf(0xFF004000));
		   XmlCharacteristic char3 = chars.addNewCharacteristic();
//		   char3.setTypecodename(AbstractLinkController.ATTRIBUTE_THICKNESS);
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
	               Log.errorMessage(e);
	           }
	           System.out.println("\nXML Instance Document saved at : " + f.getPath()); //$NON-NLS-1$
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
	           System.out.println("Invalid XML: "); //$NON-NLS-1$
	           for (int i = 0; i < validationMessages.size(); i++)
	           {
	               XmlError error = (XmlError) validationMessages.get(i);
	               System.out.println(error.getMessage());
	               System.out.println(error.getObjectLocation());
	           }
	       }
	       else
	           System.out.println("XML " + xml.documentProperties().getSourceName() + " is valid.\n"); //$NON-NLS-1$ //$NON-NLS-2$
	       return isXmlValid;
	   }
}
