/*
 * $Id: CreateTopLevelSchemeAction.java,v 1.24 2005/10/30 14:49:19 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph.actions;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.General.Event.ObjectSelectedEvent;
import com.syrus.AMFICOM.client.UI.VisualManager;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client_.scheme.SchemeObjectsFactory;
import com.syrus.AMFICOM.client_.scheme.graph.Constants;
import com.syrus.AMFICOM.client_.scheme.graph.ElementsPanel;
import com.syrus.AMFICOM.client_.scheme.graph.LangModelGraph;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeGraph;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeResource;
import com.syrus.AMFICOM.client_.scheme.graph.UgoTabbedPane;
import com.syrus.AMFICOM.client_.scheme.graph.objects.BlockPortCell;
import com.syrus.AMFICOM.client_.scheme.ui.SchemeElementPropertiesManager;
import com.syrus.AMFICOM.client_.scheme.ui.SchemePropertiesManager;
import com.syrus.AMFICOM.client_.scheme.ui.SchemeProtoElementPropertiesManager;
import com.syrus.AMFICOM.configuration.EquipmentType;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.resource.BitmapImageResource;
import com.syrus.AMFICOM.resource.SchemeImageResource;
import com.syrus.AMFICOM.scheme.AbstractSchemePort;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.SchemeCellContainer;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemeProtoElement;
import com.syrus.AMFICOM.scheme.corba.IdlAbstractSchemePortPackage.IdlDirectionType;
import com.syrus.util.Log;

/**
 * @author $Author: bass $
 * @version $Revision: 1.24 $, $Date: 2005/10/30 14:49:19 $
 * @module schemeclient
 */

public class CreateTopLevelSchemeAction extends AbstractAction {
	private static final long serialVersionUID = -2327554205858907698L;
	UgoTabbedPane sourcePane;

	public CreateTopLevelSchemeAction(UgoTabbedPane sourcePane) {
		super(Constants.CREATE_UGO);
		this.sourcePane = sourcePane;
	}
	
	public void actionPerformed(ActionEvent e) {
		execute();
	}

	public void execute() {

		SchemeGraph graph = this.sourcePane.getGraph();
		long status = SchemeActions.getGraphState(graph);
		if ((status & SchemeActions.SCHEME_EMPTY) != 0) {
			JOptionPane.showMessageDialog(Environment.getActiveWindow(),
					LangModelGraph.getString("error_empty_scheme"),  //$NON-NLS-1$
					LangModelGraph.getString("error"),  //$NON-NLS-1$
					JOptionPane.OK_OPTION);
			return;
		} 
		if ((status & SchemeActions.SCHEME_HAS_UNGROUPED_DEVICE) != 0) {
			JOptionPane.showMessageDialog(Environment.getActiveWindow(),
					LangModelGraph.getString("error_ungrouped_device"),  //$NON-NLS-1$
					LangModelGraph.getString("error"), //$NON-NLS-1$
					JOptionPane.OK_OPTION);
			return;
		} 
		if ((status & SchemeActions.SCHEME_HAS_DEVICE_GROUP) == 0) {
			JOptionPane.showMessageDialog(Environment.getActiveWindow(),
					LangModelGraph.getString("error_component_not_found"),  //$NON-NLS-1$
					LangModelGraph.getString("error"), //$NON-NLS-1$
					JOptionPane.OK_OPTION);
			return;
		}
		
		Object[] cells = graph.getAll();
		ArrayList<BlockPortCell> blockports_in = new ArrayList<BlockPortCell>();
		ArrayList<BlockPortCell> blockports_out = new ArrayList<BlockPortCell>();
		BlockPortCell[] bpcs = GraphActions.findTopLevelPorts(graph, cells);

		for (int i = 0; i < bpcs.length; i++) {
			AbstractSchemePort port = bpcs[i].getAbstractSchemePort();
			if (port.getDirectionType() == IdlDirectionType._IN)
				blockports_in.add(bpcs[i]);
			else
				blockports_out.add(bpcs[i]);
		}
		if (blockports_in.size() == 0 && blockports_out.size() == 0) {
			JOptionPane.showMessageDialog(Environment.getActiveWindow(),
					LangModelGraph.getString("error_heirarchy_port_not_found"), //$NON-NLS-1$
					LangModelGraph.getString("error"), //$NON-NLS-1$
					JOptionPane.OK_OPTION);
			return;
		}

		
		SchemeResource res = ((ElementsPanel)this.sourcePane.getCurrentPanel()).getSchemeResource();
		
		if (res.getCellContainerType() == SchemeResource.SCHEME_PROTO_ELEMENT) {
			try {
				SchemeProtoElement proto = res.getSchemeProtoElement();
				if (proto == null) {
					res.setSchemeProtoElement(SchemeObjectsFactory.createSchemeProtoElement());
				}
				
//				DeviceGroup[] groups = GraphActions.findTopLevelGroups(graph, cells);
//				if (groups.length == 1) {
//					proto = groups[0].getProtoElement();
//				}
//				if (proto == null)
//					res.setSchemeProtoElement(SchemeObjectsFactory.createSchemeProtoElement());
//				else 
//					res.setSchemeProtoElement(proto);
			} catch (ApplicationException e1) {
				Log.errorMessage(e1);
				return;
			}
		}
		
		SchemeCellContainer cellContainer = null;
		try {
			cellContainer = res.getCellContainer();
		} catch (ApplicationException e1) {
			Log.errorMessage(e1);
		}
		if (cellContainer == null) {
			Log.debugMessage(this.getClass().getName() + ": can't create top level for 'null' SchemeCellContainer", Level.FINER);
			return;
		}
		
//		Rectangle oldrect = graph.getCellBounds(cells);
		ImageIcon icon = null;
		BitmapImageResource ir = null;
		String label;
		switch (res.getCellContainerType()) { 
		case SchemeResource.SCHEME:
			ir = ((Scheme)cellContainer).getSymbol();
			label = ((Scheme)cellContainer).getLabel();
			break;
		case SchemeResource.SCHEME_ELEMENT:
			ir = ((SchemeElement)cellContainer).getSymbol();
			label = ((SchemeElement)cellContainer).getLabel();
			break;
		case SchemeResource.SCHEME_PROTO_ELEMENT:
			ir = ((SchemeProtoElement)cellContainer).getSymbol();
			label = ((SchemeProtoElement)cellContainer).getLabel();
			break;
		default:
			Log.debugMessage(getClass().getSimpleName() + " | SchemeResource not initialized", Level.FINER);
		return;
		}
		if (ir != null) {
			icon = new ImageIcon(ir.getImage());
			if (icon.getIconWidth() > 20 || icon.getIconHeight() > 20)
				icon = new ImageIcon(icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
		}
		
		ApplicationContext internalContext =  new ApplicationContext();
		internalContext.setDispatcher(new Dispatcher());
		UgoTabbedPane pane = new UgoTabbedPane(internalContext);
		SchemeGraph invisibleGraph = pane.getGraph();
		if (res.getCellContainerType() == SchemeResource.SCHEME_PROTO_ELEMENT) {
			SchemeProtoElement pe = (SchemeProtoElement)cellContainer;
			try {
				if (pe.getProtoEquipment().getType().equals(EquipmentType.MUFF)) {
					CreateUgo.createMuffUgo(pe, invisibleGraph, icon, label, blockports_in, blockports_out);
				} else {
					CreateUgo.createProtoUgo((SchemeProtoElement)cellContainer, invisibleGraph, icon, label, blockports_in, blockports_out);
				}
			} catch (ApplicationException e) {
				Log.errorMessage(e);
			}
		} else if (res.getCellContainerType() == SchemeResource.SCHEME_ELEMENT) {
			SchemeElement se = (SchemeElement)cellContainer;
			try {
				if (se.getProtoEquipment().getType().equals(EquipmentType.MUFF)) {
					CreateUgo.createMuffUgo(se, invisibleGraph, icon, label, blockports_in, blockports_out);
				} else if (se.getProtoEquipment().getType().equals(EquipmentType.CABLE_PANEL)) {
					CreateUgo.createRackUgo(se, graph);
				} else {
					CreateUgo.createElementUgo(se, invisibleGraph, icon, label, blockports_in, blockports_out);
				}
				
			} catch (ApplicationException e) {
				Log.errorMessage(e);
			}
		} else if (res.getCellContainerType() == SchemeResource.SCHEME) {
			//FIXME когда создается УГО для схемы SchemeDevice никуда не добавляется ибо SE не создается, поэтому после выхода он пропадает
			CreateUgo.createSchemeUgo((Scheme)cellContainer, invisibleGraph, icon, label, blockports_in, blockports_out);
		}
		
		SchemeImageResource sir = cellContainer.getUgoCell();
		if (sir == null) {
			try {
				sir = SchemeObjectsFactory.createSchemeImageResource();
				cellContainer.setUgoCell(sir);
			} catch (ApplicationException ex) {
				Log.errorMessage(ex);
				return;
			}
		}
		
		VisualManager manager = null;
		long type = 0;
		ApplicationContext aContext = this.sourcePane.getContext();
		
		switch (res.getCellContainerType()) { 
		case SchemeResource.SCHEME:
			manager = SchemePropertiesManager.getInstance(aContext);
			type = ObjectSelectedEvent.SCHEME;
			break;
		case SchemeResource.SCHEME_ELEMENT:
			manager = SchemeElementPropertiesManager.getInstance(aContext);
			type = ObjectSelectedEvent.SCHEME_ELEMENT;
			break;
		case SchemeResource.SCHEME_PROTO_ELEMENT:
			manager = SchemeProtoElementPropertiesManager.getInstance(aContext);
			type = ObjectSelectedEvent.SCHEME_PROTOELEMENT;
			break;
		}
		
		sir.setData((List)invisibleGraph.getArchiveableState(invisibleGraph.getRoots()));
		aContext.getDispatcher().firePropertyChange(new ObjectSelectedEvent(this, cellContainer, manager, type));
	}
}
