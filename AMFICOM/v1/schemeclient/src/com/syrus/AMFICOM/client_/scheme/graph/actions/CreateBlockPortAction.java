/*
 * $Id: CreateBlockPortAction.java,v 1.15 2005/10/31 12:30:28 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph.actions;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.swing.AbstractAction;

import com.jgraph.graph.ConnectionSet;
import com.jgraph.graph.DefaultEdge;
import com.jgraph.graph.DefaultGraphCell;
import com.jgraph.graph.DefaultPort;
import com.jgraph.graph.GraphConstants;
import com.syrus.AMFICOM.client_.scheme.graph.Constants;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeGraph;
import com.syrus.AMFICOM.client_.scheme.graph.UgoTabbedPane;
import com.syrus.AMFICOM.client_.scheme.graph.objects.BlockPortCell;
import com.syrus.AMFICOM.client_.scheme.graph.objects.BlockPortEdge;
import com.syrus.AMFICOM.client_.scheme.graph.objects.CablePortCell;
import com.syrus.AMFICOM.client_.scheme.graph.objects.PortCell;
import com.syrus.AMFICOM.scheme.AbstractSchemePort;
import com.syrus.AMFICOM.scheme.corba.IdlAbstractSchemePortPackage.IdlDirectionType;
import com.syrus.util.Log;

/**
 * @author $Author: bass $
 * @version $Revision: 1.15 $, $Date: 2005/10/31 12:30:28 $
 * @module schemeclient
 */

public class CreateBlockPortAction extends AbstractAction {
	private static final long serialVersionUID = 1840346018179019980L;
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
		
		create(graph, cell);
	}
	
	public static void create(SchemeGraph graph, Object cell) {
		if (!(cell instanceof PortCell || cell instanceof CablePortCell))
			return;
		
		int grid = graph.getGridSize();
//		DefaultPort vport = null;
		Rectangle _bounds = graph.getCellBounds(cell);
		Rectangle bounds = null;
		AbstractSchemePort abstractSchemePort = null;
		
		// search for connected BlockPortEdges
		for (Enumeration en = ((DefaultGraphCell)cell).children(); en.hasMoreElements();) {
			DefaultPort p = (DefaultPort) en.nextElement();
			for (Iterator<DefaultEdge> it = p.edges(); it.hasNext();) {
				DefaultEdge edge = it.next();
				if (edge instanceof BlockPortEdge) {
					Log.debugMessage("BlockPortEdge already connected to port " + cell, Level.FINEST);
					return;
				}
			}
		}
		
		if (cell instanceof PortCell) {
			PortCell port = (PortCell) cell;
			abstractSchemePort = port.getSchemePort();
			/*			for (Enumeration enumeration = port.children(); enumeration .hasMoreElements();) {
			 DefaultPort p = (DefaultPort) enumeration.nextElement();
			 if (p.getUserObject().equals("Center")) {
			 vport = p;
			 break;
			 }
			 }
			 if (vport == null)
			 return;*/
			
			
			if (abstractSchemePort.getDirectionType() == IdlDirectionType._IN)
				bounds = new Rectangle(new Point(_bounds.x - 6 * grid, _bounds.y - 2),
						new Dimension(grid * 3, _bounds.height + 4));
			else
				bounds = new Rectangle(new Point(_bounds.x + _bounds.width + 3 * grid,
						_bounds.y - 2), new Dimension(grid * 3, _bounds.height + 4));
		} 
		else {
			CablePortCell port = (CablePortCell) cell;
			abstractSchemePort = port.getSchemeCablePort();
			/*		for (Enumeration enumeration = port.children(); enumeration.hasMoreElements();) {
			 DefaultPort p = (DefaultPort) enumeration.nextElement();
			 if (p.getUserObject().equals("Center")) {
			 vport = p;
			 break;
			 }
			 }
			 if (vport == null)
			 return;
			 */
			if (abstractSchemePort.getDirectionType() == IdlDirectionType._IN)
				bounds = new Rectangle(new Point(_bounds.x - 6 * grid, _bounds.y - 2),
						new Dimension(grid * 3, _bounds.height + 4));
			else
				bounds = new Rectangle(new Point(_bounds.x + _bounds.width + 3 * grid,
						_bounds.y - 2), new Dimension(grid * 3, _bounds.height + 4));
		}
		
		Map<Object, Map> viewMap = new HashMap<Object, Map>();
		BlockPortCell blockport = BlockPortCell.createInstance(abstractSchemePort.getName(), bounds, viewMap, abstractSchemePort);
		graph.getGraphLayoutCache().insert(new Object[] { blockport }, viewMap, null, null, null);
		
		int u = GraphConstants.PERCENT;
		Point p = new Point((abstractSchemePort.getDirectionType() == IdlDirectionType._IN) ? u : 0, u / 2);
		DefaultPort bpcPort = GraphActions.addPort(graph, "", blockport, p);
		p = new Point((abstractSchemePort.getDirectionType() == IdlDirectionType._OUT) ? u : 0, u / 2);		
		DefaultPort dp = GraphActions.addPort(graph, "", (DefaultGraphCell)cell, p);
		
		Map edgemap = GraphConstants.createMap();
		
		List<Point> list = new ArrayList<Point>(2);
		list.add(new Point(_bounds.x, _bounds.y));
		list.add(new Point(bounds.x + bounds.width, _bounds.y));
		
		GraphConstants.setPoints(edgemap, list);
		GraphConstants.setLineEnd(edgemap, GraphConstants.ARROW_NONE);
		GraphConstants.setEndFill(edgemap, false);
		GraphConstants.setDisconnectable(edgemap, false);
		
		viewMap = new HashMap<Object, Map>();
		BlockPortEdge edge = new BlockPortEdge("");
		
		viewMap.put(edge, edgemap);
		Object[] insert = new Object[] { edge };
		ConnectionSet cs = new ConnectionSet();
		cs.connect(edge, bpcPort, false);
		/**
		 * решить куда коннектить - на центральный порт или на вновь созданный
		 * 
		 */
//		cs.connect(edge, vport, true);
		cs.connect(edge, dp, true);
		
		graph.getModel().insert(insert, viewMap, cs, null, null);
		
	}
}
