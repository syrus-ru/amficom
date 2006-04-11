/*-
 * $$Id: CreateMapReportCommand.java,v 1.17 2006/04/11 14:24:14 stas Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command.map;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import javax.swing.JDesktopPane;
import javax.swing.JDialog;

import com.syrus.AMFICOM.client.map.MapException;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.client.map.command.MapDesktopCommand;
import com.syrus.AMFICOM.client.map.report.MapReportModel;
import com.syrus.AMFICOM.client.map.ui.MapFrame;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.report.CreateReportDialog;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.report.DestinationModules;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.17 $, $Date: 2006/04/11 14:24:14 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class CreateMapReportCommand extends AbstractCommand {
	private final ApplicationContext aContext;
	private final JDesktopPane desktopPane;

	public CreateMapReportCommand(JDesktopPane desktopPane, ApplicationContext aContext) {
		this.desktopPane = desktopPane;
		this.aContext = aContext;
	}

	@Override
	public void execute() {
		MapFrame mapFrame = MapDesktopCommand.findMapFrame(this.desktopPane);
		if(mapFrame == null) {
			Log.debugMessage("Report for map inaccessible - map is not opened", Level.WARNING);
			return;
		}

		MapView mapView = mapFrame.getMapView();
		if(mapView == null) {
			Log.debugMessage("Report for map inaccessible - mapView not found", Level.WARNING);
			return;
		}

		NetMapViewer netMapViewer = mapFrame.getMapViewer();

		java.util.Map<Object,Object> reportData = new HashMap<Object,Object>();
		Map<Object,Object> topologyImageReportData = new HashMap<Object,Object>();
		topologyImageReportData.put(MapReportModel.MAPVIEW_OBJECT,mapView);
		try {
			topologyImageReportData.put(
					MapReportModel.CENTER,
					netMapViewer.getMapContext().getCenter());			
			topologyImageReportData.put(
					MapReportModel.SCALE,
					new Double(netMapViewer.getMapContext().getScale()));
			topologyImageReportData.put(
					MapReportModel.MAPFRAME_SIZE,
					mapFrame.getSize());
			reportData.put(MapReportModel.TOPOLOGY_IMAGE, topologyImageReportData);
		} catch (MapException e1) {
			Log.errorMessage(e1);
		}
		
		final Set<MapElement> selectedElements = mapView.getMap().getSelectedElements();
		if(selectedElements.size() == 1) {
			MapElement mapElement = selectedElements.iterator().next();
			if(mapElement instanceof CablePath) {
				SchemeCableLink schemeCableLink = ((CablePath)mapElement).getSchemeCableLink();
				reportData.put(MapReportModel.CABLE_LAYOUT, schemeCableLink.getId());
			}
			reportData.put(MapReportModel.SELECTED_OBJECT_CHARS, mapElement.getId());
		}
		
		try {
			JDialog dialog = new CreateReportDialog(
					this.aContext,
					DestinationModules.MAP,
					reportData);
			dialog.setVisible(true);
		} catch(Exception e) {
			Log.errorMessage(e);
		}
	}
}
