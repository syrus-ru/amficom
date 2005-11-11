/*-
* $Id: ResourcesTest.java,v 1.1 2005/11/11 11:14:30 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.resources;

import java.io.FileWriter;
import java.io.IOException;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;

import com.syrus.AMFICOM.extensions.ExtensionPoint;
import com.syrus.AMFICOM.extensions.RootDocument;
import com.syrus.AMFICOM.extensions.RootDocument.Root;
import com.syrus.AMFICOM.extensions.resources.Font;
import com.syrus.AMFICOM.extensions.resources.Handler;
import com.syrus.AMFICOM.extensions.resources.Image;
import com.syrus.AMFICOM.extensions.resources.Resource;
import com.syrus.AMFICOM.extensions.resources.Resources;
import com.syrus.AMFICOM.extensions.resources.Size;

import junit.framework.TestCase;


/**
 * @version $Revision: 1.1 $, $Date: 2005/11/11 11:14:30 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module extensions
 */
public class ResourcesTest extends TestCase {

	public void testCreation() throws Exception {
		final String xmlFilePath = "xml/resource.xml";
		this.buildDocument(true, xmlFilePath);
	}

    public XmlObject buildDocument(boolean enableOutput,
    		final String xmlFilePath) {
    	final XmlOptions opt = (new XmlOptions()).setSavePrettyPrint();

		// Build a new document
    	
    	final RootDocument doc = RootDocument.Factory.newInstance();    	
		if (enableOutput) {
			System.out.println("Empty document:\n" + doc.xmlText(opt) + "\nValid:" + doc.validate() + "\n");
		}
		Root extensions = doc.addNewRoot();		
		ExtensionPoint extensionPoint = extensions.addNewExtension();
		Resources resources = (Resources) extensionPoint.changeType(Resources.type);
		resources.setId(ResourceHandler.class.getName());
		
		{			
			final Resource resource = resources.addNewResource();
			final Handler handler = (Handler) resource.changeType(Handler.type);
			handler.setId("font");
			handler.setHandlerClass("com.syrus.AMFICOM.resources.FontResourceHandler");
	    }
		
		{			
			final Resource resource = resources.addNewResource();
			final Image image = (Image) resource.changeType(Image.type);
			image.setId("TEST_IMAGE");
			image.setFilename("test.gif");
	    }
		
		{
			final Resource resource = resources.addNewResource();
			final Font font = (Font) resource.changeType(Font.type);
			font.setId("AComboBox.font");
			font.setFontname("Arial");
			font.setStyle(Font.Style.BOLD_ITALIC);
			font.setSize(10);
		}
		
		{			
			final Resource resource = resources.addNewResource();
			final Image image = (Image) resource.changeType(Image.type);
			image.setId("TEST_IMAGE");
			image.setFilename("test2.gif");
			Size size = Size.Factory.newInstance();
			size.setWidth(16);
			size.setHeight(16);
			image.setSize(size);
	    }
		

		// Document contains two concrete resources and is valid
		if (enableOutput) {
			System.out.println("Final document:\n" + doc.xmlText(opt));
			System.out.println("Valid = " + doc.validate());
		}

		if (xmlFilePath != null) {
			try {
				final FileWriter fileWriter = new FileWriter(xmlFilePath);
				fileWriter.write(doc.xmlText(opt));
				fileWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return doc;
	}
}

