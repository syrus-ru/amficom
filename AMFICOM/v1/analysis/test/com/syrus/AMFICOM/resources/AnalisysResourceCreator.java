/*-
 * $Id: AnalisysResourceCreator.java,v 1.1 2005/12/13 10:45:55 bob Exp $
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

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys;
import com.syrus.AMFICOM.extensions.ExtensionPoint;
import com.syrus.AMFICOM.extensions.RootDocument;
import com.syrus.AMFICOM.extensions.RootDocument.Root;
import com.syrus.AMFICOM.extensions.resources.Handler;
import com.syrus.AMFICOM.extensions.resources.Image;
import com.syrus.AMFICOM.extensions.resources.Resource;
import com.syrus.AMFICOM.extensions.resources.Resources;
import com.syrus.AMFICOM.extensions.resources.Rgb;

/**
 * @version $Revision: 1.1 $, $Date: 2005/12/13 10:45:55 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module analysis
 */
public class AnalisysResourceCreator extends TestCase {

	public static void main(String[] args) {
		junit.textui.TestRunner.run(AnalisysResourceCreator.class);
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
		map.put(AnalysisResourceKeys.ICON_ANALYSIS_MINI, "images/main/analyse_mini.gif");
		map.put(AnalysisResourceKeys.ICON_SURVEY_MINI, "images/main/survey_mini.gif");
		map.put(AnalysisResourceKeys.ICON_EVALUATE_MINI, "images/main/evaluate_mini.gif");

		map.put(AnalysisResourceKeys.ICON_ANALYSIS_CENTER_A,
			"images/centerA.gif");
		map.put(AnalysisResourceKeys.ICON_ANALYSIS_CENTER_B,
			"images/centerB.gif");
		map.put(AnalysisResourceKeys.ICON_ANALYSIS_LOSS,
			"images/loss.gif");
		map.put(AnalysisResourceKeys.ICON_ANALYSIS_REFLECT,
			"images/reflect.gif");
		map.put(AnalysisResourceKeys.ICON_ANALYSIS_NOANALYSIS,
			"images/noanalyse.gif");
		map.put(AnalysisResourceKeys.ICON_ANALYSIS_PERFORM_ANALYSIS,
			"images/perform_analysis.gif");
		map.put(AnalysisResourceKeys.ICON_ANALYSIS_INITIAL_ANALYSIS,
			"images/cs_initial.gif");
		map.put(AnalysisResourceKeys.ICON_ANALYSIS_DEFAULT_ANALYSIS,
			"images/cs_default.gif");
		map.put(AnalysisResourceKeys.ICON_ANALYSIS_DOWNLOAD_TRACE,
			"images/download_trace.gif");
		map.put(AnalysisResourceKeys.ICON_ANALYSIS_DOWNLOAD_ADD,
			"images/download_add.gif");
		map.put(AnalysisResourceKeys.ICON_ANALYSIS_DOWNLOAD_REMOVE,
			"images/download_remove.gif");
		map.put(AnalysisResourceKeys.ICON_ANALYSIS_CHECK_MISMATCH,
			"images/check_mismatch.gif");
		map.put(AnalysisResourceKeys.ICON_ANALYSIS_MARKER,
			"images/marker.gif");
		map.put(AnalysisResourceKeys.ICON_ANALYSIS_DELETE_MARKER,
			"images/marker_delete.gif");
		map.put(AnalysisResourceKeys.ICON_ANALYSIS_THRESHOLD,
			"images/threshold.gif");
		map.put(AnalysisResourceKeys.ICON_ANALYSIS_ENLARGE_X,
			"images/enlargex.gif");
		map.put(AnalysisResourceKeys.ICON_ANALYSIS_ENLARGE_Y,
			"images/enlargey.gif");
		map.put(AnalysisResourceKeys.ICON_ANALYSIS_REDUCE_X,
			"images/reducex.gif");
		map.put(AnalysisResourceKeys.ICON_ANALYSIS_REDUCE_Y,
			"images/reducey.gif");
		map.put(AnalysisResourceKeys.ICON_ANALYSIS_FIT,
			"images/fit.gif");
		map.put(AnalysisResourceKeys.ICON_ANALYSIS_ZOOM_BOX,
			"images/zoom_box.gif");
		map.put(AnalysisResourceKeys.ICON_ANALYSIS_THRESHOLD_INITIAL,
			"images/th_initial.gif");
		map.put(AnalysisResourceKeys.ICON_ANALYSIS_THRESHOLD_CREATE_NEW,
			"images/th_createNew.gif");
		map.put(AnalysisResourceKeys.ICON_ANALYSIS_THRESHOLD_INCREASE,
			"images/increaseThresh.gif");
		map.put(AnalysisResourceKeys.ICON_ANALYSIS_THRESHOLD_DECREASE,
			"images/decreaseThresh.gif");
		map.put(AnalysisResourceKeys.ICON_ANALYSIS_EVENTS,
			"images/events.gif");
		map.put(AnalysisResourceKeys.ICON_ANALYSIS_MODELED,
			"images/modeled.gif");
		map.put(AnalysisResourceKeys.ICON_ANALYSIS_TRACE,
			"images/original_trace.gif");

		map.put(AnalysisResourceKeys.ICON_SHOW_PATH_ELEMENTS,
			"images/jdirectory.gif");

		map.put(AnalysisResourceKeys.ICON_ANALYSIS_GAIN,
			"images/gain.gif");
		map.put(AnalysisResourceKeys.ICON_ANALYSIS_SPLICE_GAIN,
			"images/splicegain.gif");
		map.put(AnalysisResourceKeys.ICON_ANALYSIS_BEND_LOSS,
			"images/bendloss.gif");
		map.put(AnalysisResourceKeys.ICON_ANALYSIS_SPLICE_LOSS,
			"images/spliceloss.gif");
		map.put(AnalysisResourceKeys.ICON_ANALYSIS_SINGULARITY,
			"images/singularity.gif");
		map.put(AnalysisResourceKeys.ICON_ANALYSIS_REFLECTION,
			"images/reflection.gif");
		map.put(AnalysisResourceKeys.ICON_ANALYSIS_DEADZONE,
			"images/deadzone.gif");
		map.put(AnalysisResourceKeys.ICON_ANALYSIS_END,
			"images/end.gif");
		map.put(AnalysisResourceKeys.ICON_ANALYSIS_BREAK,
			"images/break.gif");
		map.put(AnalysisResourceKeys.ICON_ANCHORED,
			"images/opt_start.gif");
		
		for (final String key : map.keySet()) {			
			final Resource resource = resources.addNewResource();
			final Image image = (Image) resource.changeType(Image.type);
			image.setId(key);
			image.setFilename(map.get(key));
	    }		
		
		final Map<String, Color> colors = new HashMap<String, Color>();
		
		colors.put(AnalysisResourceKeys.COLOR_PRIMARY_TRACE, Color.BLUE);
		colors.put(AnalysisResourceKeys.COLOR_EVENTS_NEW_SELECTED, Color.MAGENTA);
		colors.put(AnalysisResourceKeys.COLOR_EVENTS_NEW, Color.RED);

		Color alarm = new Color(255,120,120);
		Color alarmSelection = new Color(255,0,0);
		colors.put(AnalysisResourceKeys.COLOR_EVENTS_ALARM, alarm);
		colors.put(AnalysisResourceKeys.COLOR_EVENTS_ALARM_SELECTED, alarmSelection);

		Color loss = new Color(0,135,0);
		colors.put(AnalysisResourceKeys.COLOR_EVENTS_LOST_SELECTED, loss);
		colors.put(AnalysisResourceKeys.COLOR_EVENTS_LOST, loss);

		Color lossChanged = new Color(224,120,40);
		Color lossChangedSelection = new Color(160,96,0);

		colors.put(AnalysisResourceKeys.COLOR_EVENTS_LOSS_CHANGED, lossChanged);
		colors.put(AnalysisResourceKeys.COLOR_EVENTS_LOSS_CHANGED_SELECTED, lossChangedSelection);

		colors.put(AnalysisResourceKeys.COLOR_EVENTS_AMPLITUDE_CHANGED, lossChanged);
		colors.put(AnalysisResourceKeys.COLOR_EVENTS_AMPLITUDE_CHANGED_SELECTED, lossChangedSelection); 

		colors.put(AnalysisResourceKeys.COLOR_EVENTS, Color.BLACK);
		colors.put(AnalysisResourceKeys.COLOR_EVENTS_SELECTED, Color.BLACK);
		
		
		colors.put(AnalysisResourceKeys.COLOR_CONNECTOR, new Color (64, 180, 255));
		colors.put(AnalysisResourceKeys.COLOR_END, new Color(160,32,255));
		colors.put(AnalysisResourceKeys.COLOR_MARKER, Color.BLACK);
		
		colors.put(AnalysisResourceKeys.COLOR_DEADZONE, new Color(192, 32, 255));
		colors.put(AnalysisResourceKeys.COLOR_WELD, new Color(0, 220, 16));
		colors.put(AnalysisResourceKeys.COLOR_LINEZONE, new Color(0, 0, 255));
		colors.put(AnalysisResourceKeys.COLOR_NON_ID, new Color (200,9,0));
		colors.put(AnalysisResourceKeys.COLOR_NOISE, new Color(160, 160, 160));
		
		colors.put(AnalysisResourceKeys.COLOR_TRACE_PREFIX + 0, new Color(0, 128, 128));
		colors.put(Heap.PRIMARY_TRACE_KEY, Color.BLUE);
		colors.put(AnalysisResourceKeys.COLOR_TRACE_PREFIX + 1, new Color(128, 64, 0));
		colors.put(AnalysisResourceKeys.COLOR_TRACE_PREFIX + 2, new Color(128, 0, 128));
		colors.put(AnalysisResourceKeys.COLOR_TRACE_PREFIX + 3, new Color(0, 96, 0));
		colors.put(AnalysisResourceKeys.COLOR_TRACE_PREFIX + 4, new Color(0, 64, 128));
		colors.put(AnalysisResourceKeys.COLOR_TRACE_PREFIX + 5, new Color(128, 128, 0));
		
		colors.put(AnalysisResourceKeys.COLOR_WARNING_THRESHOLD, new Color(255, 220, 0));
		colors.put(AnalysisResourceKeys.COLOR_ALARM_THRESHOLD, new Color(255, 150, 60));
		
		colors.put(AnalysisResourceKeys.COLOR_MODELED, new Color (0, 192, 0));
		colors.put(AnalysisResourceKeys.COLOR_MIN_TRACE_LEVEL, new Color(255, 64, 64));
		colors.put(AnalysisResourceKeys.COLOR_SCALE, Color.LIGHT_GRAY);
		colors.put(AnalysisResourceKeys.COLOR_SCALE_DIGITS, Color.BLACK);
		colors.put(AnalysisResourceKeys.COLOR_SELECT, Color.GRAY); 
		

		final Map<String, Color> colorMap = ColorResourceHandler.getColorMap();
		for (final String key : colors.keySet()) {
			
			final Color color = colors.get(key);
			
			String name = null;
			for (final String colorMapKey : colorMap.keySet()) {
				if (colorMap.get(colorMapKey) == color) {
					name = colorMapKey;
					break;
				}
			}
			final Resource resource = resources.addNewResource();
			final com.syrus.AMFICOM.extensions.resources.Color color2 = 
				(com.syrus.AMFICOM.extensions.resources.Color) resource.changeType(com.syrus.AMFICOM.extensions.resources.Color.type);
			color2.setId(key);
			if (name != null) {
				color2.setName(com.syrus.AMFICOM.extensions.resources.Color.Name.Enum.forString(name));
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
