/*
 * $Id: CreateTopLevelSchemeAction.java,v 1.3 2005/04/22 07:32:50 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph.actions;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.util.*;
import java.util.ArrayList;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.RISDSessionInfo;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.client_.scheme.graph.*;
import com.syrus.AMFICOM.client_.scheme.graph.Constants;
import com.syrus.AMFICOM.client_.scheme.graph.objects.*;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.resource.*;
import com.syrus.AMFICOM.resource.BitmapImageResource;
import com.syrus.AMFICOM.scheme.*;
import com.syrus.AMFICOM.scheme.corba.AbstractSchemePortDirectionType;
import com.syrus.util.Log;

/**
 * @author $Author: stas $
 * @version $Revision: 1.3 $, $Date: 2005/04/22 07:32:50 $
 * @module schemeclient_v1
 */

public class CreateTopLevelSchemeAction extends AbstractAction {
	UgoTabbedPane sourcePane;
	UgoTabbedPane targetPane;
	ApplicationContext aContext;

	public CreateTopLevelSchemeAction(UgoTabbedPane sourcePane, UgoTabbedPane targetPane, ApplicationContext aContext) {
		super(Constants.createTopLevelElementKey);
		this.sourcePane = sourcePane;
		this.targetPane = targetPane;
		this.aContext = aContext;
	}

	public void actionPerformed(ActionEvent e) {
		Scheme scheme = sourcePane.getCurrentPanel().getSchemeResource().getScheme();
		if (scheme == null) {
			Log.debugMessage("Can't create top level for null scheme", Log.SEVERE); //$NON-NLS-1$
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
		BitmapImageResource ir = scheme.getSymbol();
		if (ir != null) {
			icon = new ImageIcon(ir.getImage());
			if (icon.getIconWidth() > 20 || icon.getIconHeight() > 20)
				icon = new ImageIcon(icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
		}
		
		CreateUgo.create(targetPane.getGraph(), icon, scheme.getLabel(), blockports_in, blockports_out);
		CreateGroup action = new CreateGroup(targetPane);
		action.actionPerformed(null);
		
		SchemeImageResource ugo = scheme.getUgoCell();
		if (ugo == null) {
			Identifier userId = new Identifier(((RISDSessionInfo)this.aContext.getSessionInterface()).getAccessIdentifier().user_id);
			try {
				ugo = SchemeImageResource.createInstance(userId);
				scheme.setUgoCell(ugo);
			} catch (CreateObjectException e1) {
				Log.errorException(e1);
				return;
			}
		}
		ugo.setData((List)targetPane.getGraph().getArchiveableState());
	}
}
