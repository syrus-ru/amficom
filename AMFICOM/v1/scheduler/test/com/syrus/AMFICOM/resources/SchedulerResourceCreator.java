/*-
 * $Id: SchedulerResourceCreator.java,v 1.1 2005/12/20 09:01:25 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.resources;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;

import com.syrus.AMFICOM.Client.Scheduler.General.UIStorage;
import com.syrus.AMFICOM.extensions.ExtensionPoint;
import com.syrus.AMFICOM.extensions.RootDocument;
import com.syrus.AMFICOM.extensions.RootDocument.Root;
import com.syrus.AMFICOM.extensions.resources.Handler;
import com.syrus.AMFICOM.extensions.resources.Image;
import com.syrus.AMFICOM.extensions.resources.Resource;
import com.syrus.AMFICOM.extensions.resources.Resources;

/**
 * @version $Revision: 1.1 $, $Date: 2005/12/20 09:01:25 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module scheduler
 */
public class SchedulerResourceCreator extends TestCase {

	public static void main(String[] args) {
		junit.textui.TestRunner.run(SchedulerResourceCreator.class);
	}
	
	public void testCreation() throws Exception {
		final String xmlFilePath = "resources/xml/resources.xml";
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
			Resource resource = resources.addNewResource();
			Handler handler = (Handler) resource.changeType(Handler.type);
			handler.setId(Image.type.getName().getLocalPart());
			handler.setHandlerClass(ImageResourceHandler.class.getName());
		}
		
		{
			Resource resource = resources.addNewResource();
			Handler handler = (Handler) resource.changeType(Handler.type);
			handler.setId(com.syrus.AMFICOM.extensions.resources.Color.type.getName().getLocalPart());
			handler.setHandlerClass(ColorResourceHandler.class.getName());
		}
		
		final Map<String, String> map = new HashMap<String, String>();
		
		map.put(UIStorage.ICON_SCHEDULER_MINI, "images/main/scheduling_mini.gif");
		map.put(UIStorage.ICON_DELETE, "com/syrus/AMFICOM/resources/scheduler/delete.gif");
		map.put(UIStorage.ICON_RESUME, "com/syrus/AMFICOM/resources/scheduler/resume.gif");
		map.put(UIStorage.ICON_PAUSE, "com/syrus/AMFICOM/resources/scheduler/pause.gif");

		for (final String key : map.keySet()) {			
			final Resource resource = resources.addNewResource();
			final Image image = (Image) resource.changeType(Image.type);
			image.setId(key);
			image.setFilename(map.get(key));
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
