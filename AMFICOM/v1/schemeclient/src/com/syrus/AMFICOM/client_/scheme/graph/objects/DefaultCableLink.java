/*
 * $Id: DefaultCableLink.java,v 1.1 2005/04/05 14:07:53 stas Exp $
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
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.scheme.*;
import com.syrus.util.Log;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/04/05 14:07:53 $
 * @module schemeclient_v1
 */

public class DefaultCableLink extends DefaultEdge {
	private Identifier schemeCablelinkId;

	protected Point[] routed;
	protected transient Object _source, _target;
	protected transient Object source, target;
	private LinkRouting routing = new LinkRouting();

	public static DefaultCableLink createInstance(Object userObject,
			PortView firstPort, PortView port, Point p, Point p2, Map viewMap,
			ConnectionSet cs, SchemeCableLink link) {

		// we can connect cable to CablePortCell or not connect at all
		CablePortCell sourceCablePortCell = null;
		CablePortCell targetCablePortCell = null;

		if (firstPort != null) {
			Object o = ((DefaultPort) firstPort.getCell()).getParent();
			if (o instanceof CablePortCell)
				sourceCablePortCell = (CablePortCell) o;
		}
		if (port != null) {
			Object o = ((DefaultPort) port.getCell()).getParent();
			if (o instanceof CablePortCell)
				targetCablePortCell = (CablePortCell) o;
		}
		
		DefaultCableLink cell = new DefaultCableLink(userObject);
		cell.setSchemeCableLinkId(link.getId());

		Map map = new HashMap();
		GraphConstants.setRouting(map, cell.getRouting());
		GraphConstants.setLineWidth(map, 2.49f);
		
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

	private DefaultCableLink(Object userObject) {
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

	public Edge.Routing getRouting() {
		return routing;
	}

	public class LinkRouting implements Edge.Routing {
		private static final long serialVersionUID = 01L;

		public void route(EdgeView edge, java.util.List points) {
			DefaultCableLink cell = (DefaultCableLink) edge.getCell();
			SchemeGraph graph = (SchemeGraph) edge.getGraph();

			if (!getSchemeCableLinkId().equals(cell.getSchemeCableLinkId()))
				setSchemeCableLinkId(cell.getSchemeCableLinkId());

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
				SchemeActions.disconnectSchemeCableLink(graph, DefaultCableLink.this, true);
				if (((DefaultPort) source).getParent() instanceof CablePortCell)
					SchemeActions.connectSchemeCableLink(graph, DefaultCableLink.this,
							(CablePortCell) ((DefaultPort) source).getParent(), true);
			}
			if (source == null && _source != null) {
				_source = source;
				cell._source = cell.source;
				SchemeActions.disconnectSchemeCableLink(graph, DefaultCableLink.this, true);
			}

			if (target != null && !target.equals(_target)) {
				_target = target;
				cell._target = cell.target;
				SchemeActions.disconnectSchemeCableLink(graph, DefaultCableLink.this, false);
				if (((DefaultPort) target).getParent() instanceof CablePortCell)
					SchemeActions.connectSchemeCableLink(graph, DefaultCableLink.this,
							(CablePortCell) ((DefaultPort) target).getParent(), false);
			}
			if (target == null && _target != null) {
				_target = target;
				cell._target = cell.target;
				SchemeActions.disconnectSchemeCableLink(graph, DefaultCableLink.this, false);
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
					routed[0] = new Point(bounds.x + width, bounds.y + bounds.height);
					routed[1] = new Point(bounds.x + width, bounds.y + bounds.height
							+ height);
					routed[2] = new Point(bounds.x + 2 * width, bounds.y + bounds.height
							+ height);
					routed[3] = new Point(bounds.x + 2 * width, bounds.y + bounds.height);
				} else {
					if (routed == null) {
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

	public SchemeCableLink getSchemeCableLink() {
		try {
			return (SchemeCableLink) SchemeStorableObjectPool.getStorableObject(schemeCablelinkId, true);
		} catch (ApplicationException e) {
			Log.errorException(e);
			return null;
		}
	}

	public Identifier getSchemeCableLinkId() {
		return schemeCablelinkId;
	}

	public void setSchemeCableLinkId(Identifier id) {
		schemeCablelinkId = id;
	}
}

