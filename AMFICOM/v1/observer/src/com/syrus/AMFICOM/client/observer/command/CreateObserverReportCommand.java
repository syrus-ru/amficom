package com.syrus.AMFICOM.client.observer.command;

import static com.syrus.AMFICOM.resource.SchemeResourceKeys.FRAME_EDITOR_MAIN;

import java.util.HashMap;

import javax.swing.JDialog;

import com.syrus.AMFICOM.client.UI.AdditionalPropertiesFrame;
import com.syrus.AMFICOM.client.UI.CharacteristicPropertiesFrame;
import com.syrus.AMFICOM.client.map.report.MapReportModel;
import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.observer.ObserverMainFrame;
import com.syrus.AMFICOM.client.report.CreateReportDialog;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeGraph;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeResource;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeTabbedPane;
import com.syrus.AMFICOM.client_.scheme.graph.objects.CablePortCell;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DefaultCableLink;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DefaultLink;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DeviceCell;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DeviceGroup;
import com.syrus.AMFICOM.client_.scheme.graph.objects.PortCell;
import com.syrus.AMFICOM.client_.scheme.graph.objects.Rack;
import com.syrus.AMFICOM.client_.scheme.graph.objects.TopLevelElement;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.report.DestinationModules;
import com.syrus.AMFICOM.resource.ObserverResourceKeys;
import com.syrus.AMFICOM.resource.SchemeResourceKeys;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemeProtoElement;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeElementPackage.IdlSchemeElementKind;
import com.syrus.util.Log;

public class CreateObserverReportCommand extends AbstractCommand {
	ApplicationContext aContext;

	private final ObserverMainFrame mainFrame;
	private final SchemeTabbedPane pane;
	
	public CreateObserverReportCommand(ApplicationContext aContext, ObserverMainFrame mainFrame, SchemeTabbedPane pane) {
		this.aContext = aContext;
		this.mainFrame = mainFrame;
		this.pane = pane;
	}

	public void execute() {
		java.util.Map<Object,Object> reportData = new HashMap<Object,Object>();
		
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
		
		
		
		try {
			JDialog dialog = new CreateReportDialog(
					this.aContext,
					DestinationModules.SCHEME,
					reportData);
			dialog.setVisible(true);
		} catch(Exception e) {
			Log.errorMessage(e);
		}

//		result.add(MapEditorResourceKeys.LABEL_MARKER_INFO);
//		result.add(ObserverResourceKeys.FRAME_ALARM);		
//		result.add(MapReportModel.TOPOLOGY_IMAGE);		

	}
}
