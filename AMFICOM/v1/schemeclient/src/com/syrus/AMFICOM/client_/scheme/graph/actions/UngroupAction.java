/*
 * $Id: UngroupAction.java,v 1.2 2005/05/26 07:40:52 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph.actions;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.AbstractAction;

import com.jgraph.graph.Port;
import com.syrus.AMFICOM.client_.scheme.graph.*;
import com.syrus.AMFICOM.client_.scheme.graph.objects.DeviceGroup;

/**
 * @author $Author: stas $
 * @version $Revision: 1.2 $, $Date: 2005/05/26 07:40:52 $
 * @module schemeclient_v1
 */

public class UngroupAction extends AbstractAction {
	UgoTabbedPane pane;

	public UngroupAction(UgoTabbedPane pane) {
		super(Constants.UNGROUP);
		this.pane = pane;
	}

	public void actionPerformed(ActionEvent e) {
		SchemeGraph graph = pane.getGraph();
		Object[] cells = graph.getSelectionCells();
		if (cells != null) {
			ArrayList groups = new ArrayList();
			ArrayList children = new ArrayList();
			for (int i = 0; i < cells.length; i++) {
				if (graph.isGroup(cells[i])) {
					groups.add(cells[i]);
					for (int j = 0; j < graph.getModel().getChildCount(cells[i]); j++) {
						Object child = graph.getModel().getChild(cells[i], j);
						if (!(child instanceof Port))
							children.add(child);
					}
					if (cells[i] instanceof DeviceGroup) {
						DeviceGroup group = (DeviceGroup) cells[i];
						pane.getCurrentPanel().getSchemeResource().setSchemeElement(group.getSchemeElement());
					}
				}
			}
			graph.getModel().remove(groups.toArray());
			graph.setSelectionCells(children.toArray());
		}
	}
}
