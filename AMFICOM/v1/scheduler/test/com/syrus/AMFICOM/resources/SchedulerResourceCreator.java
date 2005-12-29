/*-
 * $Id: SchedulerResourceCreator.java,v 1.3 2005/12/29 08:54:53 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.resources;

import java.awt.Color;
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
import com.syrus.AMFICOM.extensions.resources.Rgb;
import com.syrus.AMFICOM.extensions.resources.Color.Name.Enum;

/**
 * @version $Revision: 1.3 $, $Date: 2005/12/29 08:54:53 $
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
		
		{
			final Resource resource = resources.addNewResource();
			final com.syrus.AMFICOM.extensions.resources.Color color2 = 
				(com.syrus.AMFICOM.extensions.resources.Color) resource.changeType(com.syrus.AMFICOM.extensions.resources.Color.type);
			color2.setId(UIStorage.EDGE_COLOR);
			final Rgb rgb = color2.addNewRgb();
			rgb.setRed(240);
			rgb.setGreen(240);
			rgb.setBlue(240);
		}
		
		final Map<String, Color> colors = new HashMap<String, Color>();
		colors.put(UIStorage.EDGE_COLOR, new Color(240, 240, 240));
		
		colors.put(UIStorage.COLOR_STOPPED, Color.MAGENTA.darker());

		colors.put(UIStorage.COLOR_STOPPED_SELECTED, Color.MAGENTA);
		
		colors.put(UIStorage.COLOR_ABORTED, Color.RED.darker());

		colors.put(UIStorage.COLOR_ABORTED_SELECTED, Color.RED);

		colors.put(UIStorage.COLOR_ALARM, Color.ORANGE.darker());

		colors.put(UIStorage.COLOR_ALARM_SELECTED, Color.ORANGE);

		colors.put(UIStorage.COLOR_COMPLETED, Color.GREEN.darker());

		colors.put(UIStorage.COLOR_COMPLETED_SELECTED, Color.GREEN);

		colors.put(UIStorage.COLOR_PROCCESSING, Color.CYAN.darker());

		colors.put(UIStorage.COLOR_PROCCESSING_SELECTED, Color.CYAN);

		colors.put(UIStorage.COLOR_SCHEDULED, Color.GRAY);

		colors.put(UIStorage.COLOR_SCHEDULED_SELECTED, Color.LIGHT_GRAY.brighter());

		colors.put(UIStorage.COLOR_UNRECOGNIZED, new Color(20, 20, 60));

		colors.put(UIStorage.COLOR_WARNING, Color.YELLOW.darker());

		colors.put(UIStorage.COLOR_WARNING_SELECTED, Color.YELLOW);

		final Map<Enum, Color> colorMap = ColorResourceHandler.getColorMap();
		for (final String key : colors.keySet()) {
			
			final Color color = colors.get(key);
			
			Enum name = null;
			for (final Enum colorEnum : colorMap.keySet()) {
				if (colorMap.get(colorEnum) == color) {
					name = colorEnum;
					break;
				}
			}
			final Resource resource = resources.addNewResource();
			final com.syrus.AMFICOM.extensions.resources.Color color2 = 
				(com.syrus.AMFICOM.extensions.resources.Color) resource.changeType(com.syrus.AMFICOM.extensions.resources.Color.type);
			color2.setId(key);
			if (name != null) {
				color2.setName(name);
			} else {
				final Rgb rgb = color2.addNewRgb();
				rgb.setRed(color.getRed());
				rgb.setGreen(color.getGreen());
				rgb.setBlue(color.getBlue());
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
