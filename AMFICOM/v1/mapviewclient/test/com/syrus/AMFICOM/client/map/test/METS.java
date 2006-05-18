/**
 * $Id: METS.java,v 1.15.2.1 2006/05/18 17:50:00 bass Exp $
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
import java.util.Iterator;
import java.util.SortedSet;

import javax.swing.JFrame;

import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.map.LogicalNetLayer;
import com.syrus.AMFICOM.client.map.MapPropertiesManager;
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
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.Collector;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.NodeLink;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.map.SiteNode;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.mapview.MeasurementPath;
import com.syrus.AMFICOM.scheme.CableChannelingItem;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemeSampleData;
 
/**
 * @version $Revision: 1.15.2.1 $, $Date: 2006/05/18 17:50:00 $
 * @author $Author: bass $
 * @module mapviewclient
 */
public class METS {

	public static Identifier domainId;

	public static Identifier userId;

	public static ApplicationContext aContext;

	public static JFrame mainFrame;

	public static Map map;
	public static MapView mapView;
	public static MapFrame mapFrame;
	public static NetMapViewer netMapViewer;
	public static LogicalNetLayer logicalNetLayer;

	public static boolean initPerformed = false;

	public static void tearDown() {
		netMapViewer.getRenderer().cancel();		
		mainFrame.dispose();
	}

	public static void clearSchemeBinding() throws ApplicationException {
		for(Iterator iter = mapView.getSchemes().iterator(); iter.hasNext();) {
			Scheme scheme = (Scheme )iter.next();
			clearSchemeBinding(scheme);
		}
	}
	
	private static void clearSchemeBinding(Scheme scheme) throws ApplicationException {
		for(Iterator iter = scheme.getSchemeElements().iterator(); iter.hasNext();) {
			SchemeElement element = (SchemeElement )iter.next();
			element.setSiteNode(null);
		}
		for(Iterator iter = scheme.getSchemeCableLinks().iterator(); iter.hasNext();) {
			SchemeCableLink element = (SchemeCableLink )iter.next();
			final SortedSet<CableChannelingItem> cableChannelingItems = element.getPathMembers();
			if (!cableChannelingItems.isEmpty()) {
				cableChannelingItems.first().setParentPathOwner(null, true);
			}
		}
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
	
	public static void clearMapView() {
		ArrayList cables = new ArrayList(mapView.getCablePaths());
		for(Iterator iter = cables.iterator(); iter.hasNext();) {
			CablePath cablePath = (CablePath )iter.next();
			mapView.removeCablePath(cablePath);
		}
		ArrayList measurementPaths = new ArrayList(mapView.getMeasurementPaths());
		for(Iterator iter = measurementPaths.iterator(); iter.hasNext();) {
			MeasurementPath measurementPath = (MeasurementPath)iter.next();
			mapView.removeMeasurementPath(measurementPath);
		}
	}
	
	public static void setUp() throws Exception {
		if(initPerformed)
			return;

		new TestMapEditor();
		Dispatcher dispatcher = new Dispatcher();
		new OpenSessionCommand(dispatcher).execute();
		aContext = new ApplicationContext();
		aContext.setApplicationModel(new MapMapEditorApplicationModelFactory().create());
		aContext.setDispatcher(new Dispatcher());
		mapFrame = new MapFrame(aContext);

		METS.userId = LoginManager.getUserId();
		METS.domainId = LoginManager.getDomainId();
		System.out.println(METS.userId);
		System.out.println(METS.domainId);
		
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

		SchemeSampleData.populate(METS.userId, METS.domainId);

		mapView.addScheme(SchemeSampleData.scheme1);
		mapView.addScheme(SchemeSampleData.scheme2);
		
		mapFrame.setVisible(true);

		mainFrame = new JFrame();
		mainFrame.getContentPane().setLayout(new BorderLayout());
		mainFrame.getContentPane().add(mapFrame, BorderLayout.CENTER);

		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		mainFrame.setSize(dim.width, dim.height - 200);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setVisible(true);

//		Dimension cpdim = mainFrame.getContentPane().getSize();
//		mapFrame.setLocation(0, 0);
//		mapFrame.setSize(cpdim.width * 4 / 5, cpdim.height);

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
	}

	public static CableChannelingItem generateCCI(
			SchemeCableLink schemeCableLink,
			SiteNode startNode,
			SiteNode endNode) throws Exception {
		return CableChannelingItem.createInstance(
				METS.userId, 
				startNode,
				endNode,
				schemeCableLink);
	}

	public static CableChannelingItem generateCCI(
			SchemeCableLink schemeCableLink,
			SiteNode startNode,
			SiteNode endNode,
			PhysicalLink link) throws Exception
	{
		return CableChannelingItem.createInstance(
				METS.userId, 
				MapPropertiesManager.getSpareLength(),
				MapPropertiesManager.getSpareLength(),
				0,//default
				0,//default
				link,
				null,
				startNode,
				endNode,
				schemeCableLink);
	}
}

class TestMapEditor extends AbstractApplication {
	public TestMapEditor() {
        super(MapEditor.APPLICATION_NAME);
    }

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		
	}
}

