/*-
 * $Id: CreateSchemeReportCommand.java,v 1.15 2006/04/24 06:37:35 stas Exp $
 *
 * Copyright ? 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.General.Command.Scheme;

import static com.syrus.AMFICOM.resource.SchemeResourceKeys.FRAME_ADDITIONAL_PROPERIES;
import static com.syrus.AMFICOM.resource.SchemeResourceKeys.FRAME_CHARACTERISTICS;
import static com.syrus.AMFICOM.resource.SchemeResourceKeys.FRAME_EDITOR_MAIN;

import java.util.HashMap;
import java.util.logging.Level;

import javax.swing.JDialog;

import com.syrus.AMFICOM.client.model.AbstractCommand;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.report.CreateReportDialog;
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
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemeProtoElement;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeElementPackage.IdlSchemeElementKind;
import com.syrus.util.Log;

public class CreateSchemeReportCommand extends AbstractCommand {
	private final ApplicationContext aContext;
	private final SchemeTabbedPane pane;

	public CreateSchemeReportCommand(ApplicationContext aContext, SchemeTabbedPane pane) {
		this.aContext = aContext;
		this.pane = pane;
	}

	@Override
	public void execute() {
		if (this.pane.getCurrentPanel() == null) {
			Log.debugMessage("Could not create report - scheme is not opened", Level.WARNING);
			return;
		}
		
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
		
		SchemeGraph graph = this.pane.getGraph();
		Object object = graph.getSelectionCell();
		Identifiable selectedObject = null;
		
		if (object instanceof DeviceGroup) {
			DeviceGroup dev = (DeviceGroup)object;
			if (dev.getType() == DeviceGroup.PROTO_ELEMENT) {
				selectedObject = dev.getProtoElement();
			} else if (dev.getType() == DeviceGroup.SCHEME_ELEMENT) {
				SchemeElement el = dev.getSchemeElement();
				if (el.getKind() == IdlSchemeElementKind.SCHEME_CONTAINER) {
					selectedObject = dev.getScheme();
				} else {
					selectedObject = dev.getSchemeElement();
				}
			} 
		}	else if (object instanceof Rack) {
			Rack rack = (Rack)object;
			selectedObject = rack.getSchemeElement();
		} else if (object instanceof DefaultLink) {
			DefaultLink link = (DefaultLink)object;
			if (link.getSchemeLinkId() != null) {
				selectedObject = link.getSchemeLink();
			}
		} else if (object instanceof DefaultCableLink) {
			DefaultCableLink link = (DefaultCableLink)object;
			if (link.getSchemeCableLinkId() != null) {
				selectedObject = link.getSchemeCableLink();
			}
		} else if (object instanceof PortCell) {
			PortCell port = (PortCell)object;
			if (port.getSchemePortId() != null) {
				selectedObject = port.getSchemePort();
			}
		} else if (object instanceof CablePortCell) {
			CablePortCell port = (CablePortCell)object;
			if (port.getSchemeCablePortId() != null) {
				selectedObject = port.getSchemeCablePort();
			}
		} else if (object instanceof DeviceCell) {
			DeviceCell dev = (DeviceCell)object;
			if (dev.getSchemeDeviceId() != null) {
				selectedObject = dev.getSchemeDevice();
			}
		} else if (object instanceof TopLevelElement) {
			TopLevelElement top = (TopLevelElement)object;
			if (top.getSchemeId() != null) {
				selectedObject = top.getScheme();
			}
		}
		
		if (selectedObject != null) {
			reportData.put(FRAME_CHARACTERISTICS, selectedObject);
			reportData.put(FRAME_ADDITIONAL_PROPERIES, selectedObject);
		}
		
		new CreateReportDialog(
				this.aContext,
				DestinationModules.SCHEME,
				reportData);
	}
}
