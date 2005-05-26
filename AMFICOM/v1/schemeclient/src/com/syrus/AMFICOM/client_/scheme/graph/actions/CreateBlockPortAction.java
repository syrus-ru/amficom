/*
 * $Id: CreateBlockPortAction.java,v 1.3 2005/05/26 07:40:51 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph.actions;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;

import javax.swing.AbstractAction;

import com.jgraph.graph.*;
import com.syrus.AMFICOM.client_.scheme.graph.*;
import com.syrus.AMFICOM.client_.scheme.graph.objects.*;
import com.syrus.AMFICOM.scheme.AbstractSchemePort;
import com.syrus.AMFICOM.scheme.corba.AbstractSchemePortDirectionType;

/**
 * @author $Author: stas $
 * @version $Revision: 1.3 $, $Date: 2005/05/26 07:40:51 $
 * @module schemeclient_v1
 */

public class CreateBlockPortAction extends AbstractAction {
	protected UgoTabbedPane pane;

	public CreateBlockPortAction(UgoTabbedPane pane) {
		super(Constants.BLOCK_PORT);
		this.pane = pane;
	}

	public void actionPerformed(ActionEvent e) {
		SchemeGraph graph = this.pane.getGraph();
		Object[] cells = graph.getSelectionCells();
		if (cells.length != 1)
			return;
		Object cell = cells[0];
		if (!(cell instanceof PortCell || cell instanceof CablePortCell))
			return;

		int grid = graph.getGridSize();
		DefaultPort vport = null;
		Rectangle _bounds = graph.getCellBounds(cell);
		Rectangle bounds = null;
		AbstractSchemePort abstractSchemePort = null;

		if (cell instanceof PortCell) {
			PortCell port = (PortCell) cell;
			abstractSchemePort = port.getSchemePort();
			for (Enumeration enumeration = port.children(); enumeration .hasMoreElements();) {
				DefaultPort p = (DefaultPort) enumeration.nextElement();
				if (p.getUserObject().equals("Center")) {
					vport = p;
					break;
				}
			}
			if (vport == null)
				return;

			
			if (abstractSchemePort.getDirectionType().equals(AbstractSchemePortDirectionType._IN))
				bounds = new Rectangle(new Point(_bounds.x - 6 * grid, _bounds.y - 2),
						new Dimension(grid * 3, _bounds.height + 4));
			else
				bounds = new Rectangle(new Point(_bounds.x + _bounds.width + 3 * grid,
						_bounds.y - 2), new Dimension(grid * 3, _bounds.height + 4));
		} 
		else {
			CablePortCell port = (CablePortCell) cell;
			abstractSchemePort = port.getSchemeCablePort();
			for (Enumeration enumeration = port.children(); enumeration.hasMoreElements();) {
				DefaultPort p = (DefaultPort) enumeration.nextElement();
				if (p.getUserObject().equals("Center")) {
					vport = p;
					break;
				}
			}
			if (vport == null)
				return;

			if (abstractSchemePort.getDirectionType().equals(AbstractSchemePortDirectionType._IN))
				bounds = new Rectangle(new Point(_bounds.x - 6 * grid, _bounds.y - 2),
						new Dimension(grid * 3, _bounds.height + 4));
			else
				bounds = new Rectangle(new Point(_bounds.x + _bounds.width + 3 * grid,
						_bounds.y - 2), new Dimension(grid * 3, _bounds.height + 4));
		}

		Map viewMap = new HashMap();
		BlockPortCell blockport = BlockPortCell.createInstance(abstractSchemePort.getName(), bounds, viewMap, abstractSchemePort);
		graph.getGraphLayoutCache().insert(new Object[] { blockport }, viewMap, null, null, null);
		
		int u = GraphConstants.PERCENT;
		Point p = new Point((abstractSchemePort.getDirectionType().equals(AbstractSchemePortDirectionType._IN)) ? u : 0, u / 2);
		DefaultPort bpcPort = GraphActions.addPort(graph, "", blockport, p);
		
		DefaultPort dp = GraphActions.addPort(graph, "", (DefaultGraphCell)cell, p);

		Map edgemap = GraphConstants.createMap();

		ArrayList list = new ArrayList();
		list.add(new Point(_bounds.x, _bounds.y));
		list.add(new Point(bounds.x + bounds.width, _bounds.y));

		GraphConstants.setPoints(edgemap, list);
		GraphConstants.setLineEnd(edgemap, GraphConstants.ARROW_NONE);
		GraphConstants.setEndFill(edgemap, false);
		GraphConstants.setDisconnectable(edgemap, false);

		viewMap = new HashMap();
		DefaultEdge edge = new DefaultEdge("");

		viewMap.put(edge, edgemap);
		Object[] insert = new Object[] { edge };
		ConnectionSet cs = new ConnectionSet();
		cs.connect(edge, bpcPort, false);
		/**
		 * решить куда коннектить - на центральный порт или на вновь созданный
		 * cs.connect(edge, vport, true);
		 */
		cs.connect(edge, dp, true);

		graph.getModel().insert(insert, viewMap, cs, null, null);
	}
}
