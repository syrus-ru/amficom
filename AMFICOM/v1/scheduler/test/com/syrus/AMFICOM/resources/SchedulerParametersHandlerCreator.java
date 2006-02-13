/*-
 * $Id: SchedulerParametersHandlerCreator.java,v 1.1 2006/02/13 12:22:55 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.resources;

import java.io.FileWriter;
import java.io.IOException;

import junit.framework.TestCase;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;

import com.syrus.AMFICOM.Client.Schedule.UI.ReflectometryTestPanel;
import com.syrus.AMFICOM.extensions.ExtensionPoint;
import com.syrus.AMFICOM.extensions.RootDocument;
import com.syrus.AMFICOM.extensions.RootDocument.Root;
import com.syrus.AMFICOM.extensions.scheduler.ParameterHandler;
import com.syrus.AMFICOM.extensions.scheduler.ParametersHandler;
import com.syrus.AMFICOM.extensions.scheduler.SchedulerExtensions;
import com.syrus.AMFICOM.extensions.scheduler.SchedulerResource;
import com.syrus.AMFICOM.measurement.MeasurementType;

/**
 * @version $Revision: 1.1 $, $Date: 2006/02/13 12:22:55 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module scheduler
 */
public class SchedulerParametersHandlerCreator extends TestCase {

	public static void main(String[] args) {
		junit.textui.TestRunner.run(SchedulerParametersHandlerCreator.class);
	}
	
	public void testCreation() throws Exception {
		final String xmlFilePath = "resources/xml/scheduler.xml";
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
		final SchedulerExtensions schedulerExtensions = (SchedulerExtensions) extensionPoint.changeType(SchedulerExtensions.type);		
		schedulerExtensions.setId(com.syrus.AMFICOM.Client.Schedule.SchedulerHandler.class.getName());
				
		
		{			  
			final SchedulerResource resource = schedulerExtensions.addNewSchedulerResource();
			resource.setId(ParametersHandler.type.getName().getLocalPart());
			final ParametersHandler parametersHandler = (ParametersHandler) resource.changeType(ParametersHandler.type);			
			
			{			  
				ParameterHandler resource1 = parametersHandler.addNewParameterHandler();
				resource1.setId(MeasurementType.REFLECTOMETRY.getCodename());
				resource1.setHandlerClass(ReflectometryTestPanel.class.getName());
			}
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
