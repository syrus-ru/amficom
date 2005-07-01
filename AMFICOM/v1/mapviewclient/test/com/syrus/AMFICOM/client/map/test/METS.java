/**
 * $Id: METS.java,v 1.1 2005/07/01 07:52:53 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.client.map.test;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JFrame;

import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.map.LogicalNetLayer;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.client.map.command.map.MapNewCommand;
import com.syrus.AMFICOM.client.map.command.map.MapViewNewCommand;
import com.syrus.AMFICOM.client.map.editor.MapEditor;
import com.syrus.AMFICOM.client.map.ui.MapFrame;
import com.syrus.AMFICOM.client.model.AbstractApplication;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Command;
import com.syrus.AMFICOM.client.model.MapMapEditorApplicationModelFactory;
import com.syrus.AMFICOM.client.model.OpenSessionCommand;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.Collector;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.mapview.MapView;
 
/**
 * @version $Revision: 1.1 $, $Date: 2005/07/01 07:52:53 $
 * @author $Author: krupenn $
 * @module mapviewclient
 */
public class METS {

	public static ApplicationContext aContext;

	public static JFrame mainFrame;

	public static Map map;
	public static MapView mapView;
	public static MapFrame mapFrame;
	public static NetMapViewer netMapViewer;
	public static LogicalNetLayer logicalNetLayer;

	public static boolean initPerformed = false;

	public static void tearDown() {
		mainFrame.dispose();
	}
	
	public static void clearMap() {
		ArrayList nodes = new ArrayList(map.getNodes());
		for(Iterator iter = nodes.iterator(); iter.hasNext();) {
			AbstractNode node = (AbstractNode )iter.next();
			map.removeNode(node);
		}
		ArrayList physicalLinks = new ArrayList(map.getPhysicalLinks());
		for(Iterator iter = physicalLinks.iterator(); iter.hasNext();) {
			PhysicalLink link = (PhysicalLink )iter.next();
			map.removePhysicalLink(link);
		}
		ArrayList nodeLinks = new ArrayList(map.getNodeLinks());
		for(Iterator iter = nodeLinks.iterator(); iter.hasNext();) {
			NodeLink nodeLink = (NodeLink )iter.next();
			map.removeNodeLink(nodeLink);
		}
		ArrayList collectors = new ArrayList(map.getCollectors());
		for(Iterator iter = collectors.iterator(); iter.hasNext();) {
			Collector collector = (Collector )iter.next();
			map.removeCollector(collector);
		}
	}
	
	public static void setUp() {
		if(initPerformed)
			return;

		try {
			TestMapEditor application = new TestMapEditor();
			Dispatcher dispatcher = new Dispatcher();
			new OpenSessionCommand(dispatcher).execute();
			System.out.println(LoginManager.getUserId());
			System.out.println(LoginManager.getDomainId());
			aContext = new ApplicationContext();
			aContext.setApplicationModel(new MapMapEditorApplicationModelFactory().create());
			aContext.setDispatcher(new Dispatcher());
			mapFrame = new MapFrame(aContext);
			MapNewCommand mnc = new MapNewCommand(aContext);
			mnc.execute();
			if(mnc.getResult() == Command.RESULT_OK) {
				map = mnc.getMap();
			}
			else
				return;
			MapViewNewCommand mvnc = new MapViewNewCommand(map, aContext);
			mvnc.execute();
			if(mvnc.getResult() == Command.RESULT_OK) {
				mapView = mvnc.getMapView();
				mapView.setCenter(mapFrame.getMapViewer().getLogicalNetLayer().getMapContext().getCenter());
				mapView.setScale(mapFrame.getMapViewer().getLogicalNetLayer().getMapContext().getScale());
			}
			else
				return;
			mapView.setMap(map);
			mapFrame.setMapView(mapView);
			
			mainFrame = new JFrame();
			mainFrame.getContentPane().setLayout(new BorderLayout());
//			JInternalFrame iframe = new JInternalFrame("Bueee", true, true, true, true);
			mapFrame.setVisible(true);
			mainFrame.getContentPane().add(mapFrame, BorderLayout.CENTER);

			Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
			mainFrame.setSize(dim.width, dim.height - 150);
			mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			mainFrame.setVisible(true);

			Dimension cpdim = mainFrame.getContentPane().getSize();
			mapFrame.setLocation(0, 0);
			mapFrame.setSize(cpdim.width * 4 / 5, cpdim.height);

			netMapViewer = mapFrame.getMapViewer();
			logicalNetLayer = netMapViewer.getLogicalNetLayer(); 

			while(!(METS.mainFrame.isVisible() && METS.mapFrame.isVisible())) {
				try {
					Thread.sleep(100);
				} catch(InterruptedException e) {
					//empty
				}
			}

			initPerformed = true;
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}

class TestMapEditor extends AbstractApplication {
	public TestMapEditor() {
        super(MapEditor.APPLICATION_NAME);
    }
}

