/*-
 * $$Id: StandAloneNetMapViewGenerator.java,v 1.3 2005/10/07 14:14:19 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.client.map.command.editor.ViewMapChooserCommand;
import com.syrus.AMFICOM.client.map.controllers.MapViewController;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.MapReportApplicationModelFactory;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.mapview.MapView;

/**
 * @version $Revision: 1.3 $, $Date: 2005/10/07 14:14:19 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class StandAloneNetMapViewGenerator {

	private static NetMapViewer netMapViewer;
	private static MapConnection mapConnection;

	public static BufferedImage getMapShot(MapView mapView, MapElement mapElement, Dimension dimension) throws MapConnectionException, MapDataException, ApplicationException {
		if(StandAloneNetMapViewGenerator.netMapViewer == null) {
			initialize();
		}

		StandAloneNetMapViewGenerator.netMapViewer.getLogicalNetLayer().setMapView(mapView);
		StandAloneNetMapViewGenerator.netMapViewer.centerAndScale(mapElement, dimension);
		StandAloneNetMapViewGenerator.netMapViewer.repaint(true);
		return StandAloneNetMapViewGenerator.netMapViewer.getMapShot();
	}

	private static void initialize() throws MapConnectionException, MapDataException, ApplicationException {
		mapConnection = MapConnection.create(MapPropertiesManager.getConnectionClassName());
		mapConnection.setPath(MapPropertiesManager.getDataBasePath());
		mapConnection.setView(MapPropertiesManager.getDataBaseView());
		mapConnection.setURL(MapPropertiesManager.getDataBaseURL());
			try {
				mapConnection.connect();
			} catch(MapConnectionException e) {
				int result = ViewMapChooserCommand.chooseMap(mapConnection);
				if(result == JOptionPane.OK_OPTION) {
//					this.mapConnection.connect();
				}
				else {
					throw e;
				}
			}

		final MapImageLoader loader = mapConnection.createImageLoader();
		final MapContext mapContext = mapConnection.createMapContext();
		final MapCoordinatesConverter converter = mapConnection.createCoordinatesConverter();

		final MapImageRenderer renderer = MapImageRendererFactory.create(MapPropertiesManager.getMapImageRendererClassName(),
				converter,
				mapContext,
				loader);

		ApplicationContext aContext = new ApplicationContext();
		aContext.setApplicationModel(new MapReportApplicationModelFactory().create());
		
		final LogicalNetLayer logicalNetLayer = new LogicalNetLayer(aContext, converter, mapContext);

		netMapViewer = NetMapViewer.create(MapPropertiesManager.getNetMapViewerClassName(), logicalNetLayer, mapContext, renderer);

		netMapViewer.init();

		logicalNetLayer.setMapViewController(MapViewController.createInstance(netMapViewer));

		netMapViewer.setScale(MapPropertiesManager.getZoom());
		netMapViewer.setCenter(MapPropertiesManager.getCenter());
	}
	
}
