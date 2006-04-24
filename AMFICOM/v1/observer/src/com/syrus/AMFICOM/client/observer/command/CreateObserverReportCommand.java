package com.syrus.AMFICOM.client.observer.command;

import static com.syrus.AMFICOM.resource.SchemeResourceKeys.FRAME_EDITOR_MAIN;

import java.awt.Component;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import javax.swing.JDesktopPane;

import com.syrus.AMFICOM.client.map.MapException;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.client.map.command.MapDesktopCommand;
import com.syrus.AMFICOM.client.map.report.MapReportModel;
import com.syrus.AMFICOM.client.map.ui.MapFrame;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.observer.ui.AlarmFrame;
import com.syrus.AMFICOM.client.observer.ui.ResultFrame;
import com.syrus.AMFICOM.client.report.CreateReportDialog;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeResource;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeTabbedPane;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.report.DestinationModules;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemeProtoElement;
import com.syrus.util.Log;

public class CreateObserverReportCommand extends AbstractCommand {
	ApplicationContext aContext;

	private final JDesktopPane desktopPane;
	private final SchemeTabbedPane pane;
	
	public CreateObserverReportCommand(ApplicationContext aContext, JDesktopPane desktopPane, SchemeTabbedPane pane) {
		this.aContext = aContext;
		this.desktopPane = desktopPane;
		this.pane = pane;
	}

	@Override
	public void execute() {
		java.util.Map<Object,Object> reportData = new HashMap<Object,Object>();
		
		if (this.pane.getCurrentPanel() != null) {
			try {
				SchemeResource res = this.pane.getCurrentPanel().getSchemeResource();
				if (res.getCellContainerType() == SchemeResource.SCHEME) {
					Scheme scheme = res.getScheme();
					reportData.put(FRAME_EDITOR_MAIN, scheme);	
				} else if (res.getCellContainerType() == SchemeResource.SCHEME_ELEMENT) {
					SchemeElement schemeElement = res.getSchemeElement();
					reportData.put(FRAME_EDITOR_MAIN, schemeElement);
				} else if (res.getCellContainerType() == SchemeResource.SCHEME_PROTO_ELEMENT) {
					SchemeProtoElement schemeProtoElement = res.getSchemeProtoElement();
					reportData.put(FRAME_EDITOR_MAIN, schemeProtoElement);
				}
			} catch (ApplicationException e1) {
				Log.errorMessage(e1);
			}
		} else {
			Log.debugMessage("Report for scheme inaccessible - scheme is not opened", Level.WARNING);
		}
		
		
		MapFrame mapFrame = MapDesktopCommand.findMapFrame(this.desktopPane);
		if(mapFrame != null) {
			MapView mapView = mapFrame.getMapView();
			if(mapView != null) {
				
				NetMapViewer netMapViewer = mapFrame.getMapViewer();
				Map<Object,Object> topologyImageReportData = new HashMap<Object,Object>();
				topologyImageReportData.put(MapReportModel.MAPVIEW_OBJECT,mapView);
				try {
					topologyImageReportData.put(MapReportModel.CENTER,
							netMapViewer.getMapContext().getCenter());			
					topologyImageReportData.put(MapReportModel.SCALE,
							new Double(netMapViewer.getMapContext().getScale()));
					topologyImageReportData.put(MapReportModel.MAPFRAME_SIZE,
							mapFrame.getSize());
					
					reportData.put(MapReportModel.TOPOLOGY_IMAGE, topologyImageReportData);
				} catch (MapException e1) {
					Log.errorMessage(e1);
				}
				
			} else {
				Log.debugMessage("Report for map inaccessible - mapView not found", Level.WARNING);
			}	
		} else {
			Log.debugMessage("Report for map inaccessible - map is not opened", Level.WARNING);
		}
		
		AlarmFrame alarmFrame = findAlarmFrame(this.desktopPane);
		if (alarmFrame != null) {
			reportData.put(alarmFrame.getReportTitle(), alarmFrame.getTableModel());
		} else {
			Log.debugMessage("Report for alarms inaccessible - alarmFrame is not opened", Level.WARNING);
		}
		
		ResultFrame resultFrame = findResultFrame(this.desktopPane);
		if (resultFrame != null) {
			if (resultFrame.getReflectogrammPanel() != null) {
				reportData.put(resultFrame.getName(), resultFrame.getReflectogrammPanel());
			} else {
				Log.debugMessage("Report for result inaccessible - reflectogrammPanel is not opened", Level.WARNING);
			}
		} else {
			Log.debugMessage("Report for result inaccessible - resultFrame is not opened", Level.WARNING);
		}
		
		new CreateReportDialog(
				this.aContext,
				DestinationModules.OBSERVE,
				reportData);

//		result.add(MapEditorResourceKeys.LABEL_MARKER_INFO);
	}
	
	private static AlarmFrame findAlarmFrame(JDesktopPane desktop) {
		for(int i = 0; i < desktop.getComponents().length; i++) {
			Component comp = desktop.getComponent(i);
			if(comp != null && comp instanceof AlarmFrame)
				return (AlarmFrame)comp;
		}
		return null;
	}
	
	private static ResultFrame findResultFrame(JDesktopPane desktop) {
		for(int i = 0; i < desktop.getComponents().length; i++) {
			Component comp = desktop.getComponent(i);
			if(comp != null && comp instanceof ResultFrame)
				return (ResultFrame)comp;
		}
		return null;
	}
}
