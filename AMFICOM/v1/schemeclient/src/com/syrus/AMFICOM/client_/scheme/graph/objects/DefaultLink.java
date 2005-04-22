/*
 * $Id: DefaultLink.java,v 1.2 2005/04/22 07:32:50 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.graph.objects;

import java.awt.*;
import java.util.*;

import com.jgraph.graph.*;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeGraph;
import com.syrus.AMFICOM.client_.scheme.graph.actions.SchemeActions;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.scheme.*;

/**
 * @author $Author: stas $
 * @version $Revision: 1.2 $, $Date: 2005/04/22 07:32:50 $
 * @module schemeclient_v1
 */

public class DefaultLink extends DefaultEdge {
	private Identifier scheme_link_id;

	protected Point[] routed;
	protected transient Object _source, _target;
	protected transient Object source, target;
	private LinkRouting routing = new LinkRouting();

	public static DefaultLink createInstance(Object userObject,
			PortView firstPort, PortView port, Point p, Point p2, Map viewMap,
			ConnectionSet cs) {

		// we can connect cable to CablePortCell or not connect at all
		PortCell sourceCablePortCell = null;
		PortCell targetCablePortCell = null;

		if (firstPort != null) {
			Object o = ((DefaultPort) firstPort.getCell()).getParent();
			if (o instanceof CablePortCell)
				sourceCablePortCell = (PortCell) o;
		}
		if (port != null) {
			Object o = ((DefaultPort) port.getCell()).getParent();
			if (o instanceof CablePortCell)
				targetCablePortCell = (PortCell) o;
		}
		DefaultLink cell = new DefaultLink(userObject);

		Map map = new HashMap();
		GraphConstants.setRouting(map, cell.getRouting());
		
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

			if (!getSchemeLinkId().equals(cell.getSchemeLinkId()))
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
				_source = source;
				cell._source = cell.source;

				SchemeActions.disconnectSchemeLink(graph, DefaultLink.this, true);
				if (((DefaultPort) source).getParent() instanceof PortCell)
					SchemeActions.connectSchemeLink(graph, DefaultLink.this,
							(PortCell) ((DefaultPort) source).getParent(), true);
			}
			if (source == null && _source != null) {
				_source = source;
				cell._source = cell.source;

				SchemeActions.disconnectSchemeLink(graph, DefaultLink.this, true);
			}

			if (target != null && !target.equals(_target))
			// (_target == null ||
			// !((PortCell)((DefaultPort)target).getParent()).getSchemePortId().equals(
			// ((PortCell)((DefaultPort)_target).getParent()).getSchemePortId())))
			{
				_target = target;
				cell._target = cell.target;

				SchemeActions.disconnectSchemeLink(graph, DefaultLink.this, false);
				if (((DefaultPort) target).getParent() instanceof PortCell)
					SchemeActions.connectSchemeLink(graph, DefaultLink.this,
							(PortCell) ((DefaultPort) target).getParent(), false);
			}
			if (target == null && _target != null) {
				_target = target;
				cell._target = cell.target;

				SchemeActions.disconnectSchemeLink(graph, DefaultLink.this, false);
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
			return (SchemeLink) SchemeStorableObjectPool.getStorableObject(scheme_link_id, true);
		} catch (Exception ex) {
			return null;
		}
	}

	public Identifier getSchemeLinkId() {
		return scheme_link_id;
	}

	public void setSchemeLinkId(Identifier id) {
		scheme_link_id = id;
	}
}
