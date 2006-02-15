/*-
 * $$Id: CreateMapReportCommand.java,v 1.15 2006/02/15 11:12:43 stas Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command.map;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
 * @version $Revision: 1.15 $, $Date: 2006/02/15 11:12:43 $
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
			return;
		}

		MapView mapView = mapFrame.getMapView();

		if(mapView == null) {
			return;
		}

		NetMapViewer netMapViewer = mapFrame.getMapViewer();

		final Set<MapElement> selectedElements = mapView.getMap().getSelectedElements();
		if(selectedElements.size() != 1) {
			// more than 1 object selected
			return;
		}
		MapElement mapElement = selectedElements.iterator().next();
		
		java.util.Map<Object,Object> reportData = new HashMap<Object,Object>();
		if(mapElement instanceof CablePath) {
			SchemeCableLink schemeCableLink = ((CablePath)mapElement).getSchemeCableLink();
			reportData.put(MapReportModel.CABLE_LAYOUT, schemeCableLink.getId());
		}
		Map<Object,Object> topologyImageReportData = new HashMap<Object,Object>();
		topologyImageReportData.put(MapReportModel.MAPVIEW_OBJECT,mapView);
		try {
			topologyImageReportData.put(
					MapReportModel.CENTER,
					mapElement.getLocation());			
			topologyImageReportData.put(
					MapReportModel.SCALE,
					new Double(netMapViewer.getMapContext().getScale()));
			topologyImageReportData.put(
					MapReportModel.MAPFRAME_SIZE,
					mapFrame.getSize());
		} catch (MapException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		reportData.put(MapReportModel.TOPOLOGY_IMAGE, topologyImageReportData);
		reportData.put(MapReportModel.SELECTED_OBJECT_CHARS, mapElement.getId());
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
