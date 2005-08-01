/*
 * $Id: DefaultLink.java,v 1.6 2005/08/01 07:52:28 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph.objects;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.jgraph.graph.ConnectionSet;
import com.jgraph.graph.DefaultEdge;
import com.jgraph.graph.DefaultPort;
import com.jgraph.graph.EdgeView;
import com.jgraph.graph.GraphConstants;
import com.jgraph.graph.PortView;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeGraph;
import com.syrus.AMFICOM.client_.scheme.graph.actions.SchemeActions;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.scheme.SchemeLink;
import com.syrus.util.Log;

/**
 * @author $Author: stas $
 * @version $Revision: 1.6 $, $Date: 2005/08/01 07:52:28 $
 * @module schemeclient_v1
 */

public class DefaultLink extends DefaultEdge {
	private static final long serialVersionUID = 4050203024863737140L;

	private Identifier scheme_link_id;

	protected Point[] routed;
	protected transient Object _source, _target;
	protected transient Object source, target;
	private LinkRouting routing = new LinkRouting();

	public static DefaultLink createInstance(Object userObject,
			PortView firstPort, PortView port, Point p, Point p2, Map viewMap,
			ConnectionSet cs) {

		// we can connect cable to PortCell or not connect at all
		PortCell sourceCablePortCell = null;
		PortCell targetCablePortCell = null;

		if (firstPort != null) {
			Object o = ((DefaultPort) firstPort.getCell()).getParent();
			if (o instanceof PortCell)
				sourceCablePortCell = (PortCell) o;
		}
		if (port != null) {
			Object o = ((DefaultPort) port.getCell()).getParent();
			if (o instanceof PortCell)
				targetCablePortCell = (PortCell) o;
		}
		DefaultLink cell = new DefaultLink(userObject);
		
		int u = GraphConstants.PERCENT;
		Map map = new HashMap();
		GraphConstants.setRouting(map, cell.getRouting());
		GraphConstants.setLabelPosition(map, new Point((int)(u * 0.4), (int)(u * 0.49)));
		
		ArrayList list = new ArrayList();
		list.add(p);
		list.add(p2);

		GraphConstants.setPoints(map, list);
		GraphConstants.setBendable(map, true);
		viewMap.put(cell, map);
		
		if (sourceCablePortCell != null)
			cs.connect(cell, firstPort.getCell(), true);
		if (targetCablePortCell != null)
			cs.connect(cell, port.getCell(), false);
		
		return cell;
	}

	
	private DefaultLink(Object userObject) {
		super(userObject);
	}

	public void setSource(Object port) {
		super.setSource(port);
		source = port;
	}

	public void setTarget(Object port) {
		super.setTarget(port);
		target = port;
	}

	public DefaultEdge.Routing getRouting() {
		return routing;
	}

	public class LinkRouting implements DefaultEdge.Routing {
		private static final long serialVersionUID = 01L;

		public void route(EdgeView edge, java.util.List points) {
			DefaultLink cell = (DefaultLink) edge.getCell();
			SchemeGraph graph = (SchemeGraph) edge.getGraph();

			if (getSchemeLinkId() != null && !getSchemeLinkId().equals(cell.getSchemeLinkId()))
				setSchemeLinkId(cell.getSchemeLinkId());

			if (cell.source != cell._source) {
				source = cell.source;
				_source = cell._source;
			}
			if (cell.target != cell._target) {
				target = cell.target;
				_target = cell._target;
			}

			if (source != null && !source.equals(_source)) {
				if (_source != null) {
					if (((DefaultPort) _source).getParent() instanceof PortCell)
						SchemeActions.disconnectSchemeLink(graph, DefaultLink.this, (PortCell) ((DefaultPort)_source).getParent(), true);
				}
				
				_source = source;
				cell._source = cell.source;

				if (((DefaultPort) source).getParent() instanceof PortCell)
					SchemeActions.connectSchemeLink(graph, DefaultLink.this,
							(PortCell) ((DefaultPort) source).getParent(), true);
			}
			if (source == null && _source != null) {
				if (((DefaultPort) _source).getParent() instanceof PortCell)
					SchemeActions.disconnectSchemeLink(graph, DefaultLink.this, (PortCell) ((DefaultPort)_source).getParent(), true);
				
				_source = source;
				cell._source = cell.source;
			}

			if (target != null && !target.equals(_target))
			{
				if (_target != null) {
					if (((DefaultPort) _target).getParent() instanceof PortCell)
						SchemeActions.disconnectSchemeLink(graph, DefaultLink.this, (PortCell) ((DefaultPort)_target).getParent(), false);
				}
				
				_target = target;
				cell._target = cell.target;

				if (((DefaultPort) target).getParent() instanceof PortCell)
					SchemeActions.connectSchemeLink(graph, DefaultLink.this,
							(PortCell) ((DefaultPort) target).getParent(), false);
			}
			if (target == null && _target != null) {
				if (((DefaultPort) _target).getParent() instanceof PortCell)
					SchemeActions.disconnectSchemeLink(graph, DefaultLink.this, (PortCell) ((DefaultPort)_target).getParent(), false);
				
				_target = target;
				cell._target = cell.target;
			}

			int n = points.size();
			Point from = edge.getPoint(0);
			if (edge.getSource() != null)
				from = edge.getSource().getLocation(null);
			Point to = edge.getPoint(n - 1);
			if (edge.getTarget() != null)
				to = edge.getTarget().getLocation(null);
			if (from != null && to != null) {
				// Handle self references
				if (edge.getSource() == edge.getTarget() && edge.getSource() != null) {
					Rectangle bounds = edge.getSource().getParentView().getBounds();
					int height = edge.getGraph().getGridSize();
					int width = (int) (bounds.getWidth() / 3);
					routed = new Point[4];
					routed[0] = graph.snap(new Point(bounds.x + width, bounds.y
							+ bounds.height));
					routed[1] = graph.snap(new Point(bounds.x + width, bounds.y
							+ bounds.height + height));
					routed[2] = graph.snap(new Point(bounds.x + 2 * width, bounds.y
							+ bounds.height + height));
					routed[3] = graph.snap(new Point(bounds.x + 2 * width, bounds.y
							+ bounds.height));
				} else {
					boolean bendable = GraphConstants.isBendable(edge.getAllAttributes());
					if (!bendable || routed == null) {
						int x2 = from.x + ((to.x - from.x) / 2);
						routed = new Point[4];
						routed[0] = graph.snap(new Point(x2, from.y));
						routed[1] = graph.snap(new Point(x2, from.y));
						routed[2] = graph.snap(new Point(x2, to.y));
						routed[3] = graph.snap(new Point(x2, to.y));
					}
				}
				// Set/Add Points
				for (int i = 0; i < routed.length; i++)
					if (points.size() > i + 2)
						points.set(i + 1, routed[i]);
					else
						points.add(i + 1, routed[i]);
				// Remove spare points
				while (points.size() > routed.length + 2) {
					points.remove(points.size() - 2);
				}
			}
		}
	}

	public SchemeLink getSchemeLink() {
		try {
			return (SchemeLink) StorableObjectPool.getStorableObject(scheme_link_id, true);
		} catch (ApplicationException ex) {
			Log.errorException(ex);
			return null;
		}
	}

	public Identifier getSchemeLinkId() {
		return scheme_link_id;
	}

	public void setSchemeLinkId(Identifier id) {
		assert id != null;
		scheme_link_id = id;
	}
}
