/*
 * $Id: CreateTopLevelSchemeAction.java,v 1.6 2005/06/22 10:16:06 stas Exp $
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

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client_.scheme.graph.Constants;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeGraph;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeResource;
import com.syrus.AMFICOM.client_.scheme.graph.UgoTabbedPane;
import com.syrus.AMFICOM.client_.scheme.graph.objects.BlockPortCell;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DeviceGroup;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.resource.BitmapImageResource;
import com.syrus.AMFICOM.resource.SchemeImageResource;
import com.syrus.AMFICOM.scheme.AbstractSchemePort;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.SchemeCellContainer;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemeProtoElement;
import com.syrus.AMFICOM.scheme.corba.AbstractSchemePortDirectionType;
import com.syrus.util.Log;

/**
 * @author $Author: stas $
 * @version $Revision: 1.6 $, $Date: 2005/06/22 10:16:06 $
 * @module schemeclient_v1
 */

public class CreateTopLevelSchemeAction extends AbstractAction {
	UgoTabbedPane sourcePane;
	ApplicationContext aContext;

	public CreateTopLevelSchemeAction(UgoTabbedPane sourcePane, ApplicationContext aContext) {
		super(Constants.CREATE_UGO);
		this.sourcePane = sourcePane;
		this.aContext = aContext;
	}

	public void actionPerformed(ActionEvent e) {
		SchemeResource res = sourcePane.getCurrentPanel().getSchemeResource();
		SchemeCellContainer cell = res.getCellContainer();
		if (cell == null) {
			Log.debugMessage(this.getClass().getName() + ": can't create top level for 'null' SchemeCellContainer", Log.SEVERE);
			return;
		}
		
		SchemeGraph graph = sourcePane.getGraph();
		Object[] cells = graph.getAll();
		DeviceGroup[] groups = GraphActions.findTopLevelGroups(graph, cells);

		if (groups.length == 0) {
			JOptionPane.showMessageDialog(Environment.getActiveWindow(), 
					Constants.ERROR_COMPONENT_NOT_FOUND, Constants.ERROR, JOptionPane.OK_OPTION);
			return;
		}
		ArrayList blockports_in = new ArrayList();
		ArrayList blockports_out = new ArrayList();
		BlockPortCell[] bpcs = GraphActions.findTopLevelPorts(graph, cells);

		for (int i = 0; i < bpcs.length; i++) {
			AbstractSchemePort port = bpcs[i].getAbstractSchemePort();
			if (port.getDirectionType().equals(AbstractSchemePortDirectionType._IN))
				blockports_in.add(bpcs[i]);
			else
				blockports_out.add(bpcs[i]);
		}
		if (blockports_in.size() == 0 && blockports_out.size() == 0) {
			JOptionPane.showMessageDialog(Environment.getActiveWindow(),
					Constants.ERROR_HIERARCHY_PORT_NOT_FOUND, Constants.ERROR,
					JOptionPane.OK_OPTION);
			return;
		}

//		Rectangle oldrect = graph.getCellBounds(cells);
		ImageIcon icon = null;
		BitmapImageResource ir = null;
		String label;
		if (cell instanceof Scheme) {
			ir = ((Scheme)cell).getSymbol();
			label = ((Scheme)cell).getLabel();
		} else if (cell instanceof SchemeElement) {
			ir = ((SchemeElement)cell).getSymbol();
			label = ((SchemeElement)cell).getLabel();
		} else if (cell instanceof SchemeProtoElement) {
			ir = ((SchemeProtoElement)cell).getSymbol();
			label = ((SchemeProtoElement)cell).getLabel();
		} else {
			label = ""; //$NON-NLS-1$
		}
		if (ir != null) {
			icon = new ImageIcon(ir.getImage());
			if (icon.getIconWidth() > 20 || icon.getIconHeight() > 20)
				icon = new ImageIcon(icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
		}
		
		SchemeGraph invisibleGraph = new SchemeGraph(new ApplicationContext()); 
		CreateUgo.create(invisibleGraph, icon, label, blockports_in, blockports_out);
		SchemeImageResource sir = cell.getUgoCell();
		if (sir == null) {
			try {
				sir = SchemeImageResource.createInstance(LoginManager.getUserId());
				cell.setUgoCell(sir);
			} catch (ApplicationException ex) {
				Log.errorException(ex);
				return;
			}
		}
		sir.setData((List)invisibleGraph.getArchiveableState(invisibleGraph.getRoots()));
	}
}
