/*
 * $Id: DefaultCableLink.java,v 1.6 2005/08/01 07:52:28 stas Exp $
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
import com.jgraph.graph.Edge;
import com.jgraph.graph.EdgeView;
import com.jgraph.graph.GraphConstants;
import com.jgraph.graph.PortView;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeGraph;
import com.syrus.AMFICOM.client_.scheme.graph.actions.SchemeActions;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.util.Log;

/**
 * @author $Author: stas $
 * @version $Revision: 1.6 $, $Date: 2005/08/01 07:52:28 $
 * @module schemeclient_v1
 */

public class DefaultCableLink extends DefaultEdge {
	private static final long serialVersionUID = 3618420414867519024L;

	private Identifier schemeCablelinkId;

	protected Point[] routed;
	protected transient Object _source, _target;
	protected transient Object source, target;
	private LinkRouting routing = new LinkRouting();

	public static DefaultCableLink createInstance(Object userObject,
			PortView firstPort, PortView port, Point p, Point p2, Map viewMap,
			ConnectionSet cs) {

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

		int u = GraphConstants.PERCENT;
		Map map = new HashMap();
		GraphConstants.setRouting(map, cell.getRouting());
		GraphConstants.setLineWidth(map, 2.49f);
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
		private static final long serialVersionUID = 3618420414867519024L;

		public void route(EdgeView edge, java.util.List points) {
			DefaultCableLink cell = (DefaultCableLink) edge.getCell();
			SchemeGraph graph = (SchemeGraph) edge.getGraph();

			if (getSchemeCableLinkId() != null && !getSchemeCableLinkId().equals(cell.getSchemeCableLinkId()))
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
				if (_source != null) {
					if (((DefaultPort) _source).getParent() instanceof CablePortCell)
						SchemeActions.disconnectSchemeCableLink(graph, DefaultCableLink.this, (CablePortCell) ((DefaultPort)_source).getParent(), true);
				}
				
				_source = source;
				cell._source = cell.source;

				if (((DefaultPort) source).getParent() instanceof CablePortCell)
					SchemeActions.connectSchemeCableLink(graph, DefaultCableLink.this,
							(CablePortCell) ((DefaultPort) source).getParent(), true);
			}
			if (source == null && _source != null) {
				if (((DefaultPort) _source).getParent() instanceof CablePortCell)
					SchemeActions.disconnectSchemeCableLink(graph, DefaultCableLink.this, (CablePortCell) ((DefaultPort)_source).getParent(), true);
				
				_source = source;
				cell._source = cell.source;
			}

			if (target != null && !target.equals(_target)) {
				if (_target != null) {
					if (((DefaultPort) _target).getParent() instanceof CablePortCell)
						SchemeActions.disconnectSchemeCableLink(graph, DefaultCableLink.this, (CablePortCell) ((DefaultPort)_target).getParent(), false);
				}
				
				_target = target;
				cell._target = cell.target;

				if (((DefaultPort) target).getParent() instanceof CablePortCell)
					SchemeActions.connectSchemeCableLink(graph, DefaultCableLink.this,
							(CablePortCell) ((DefaultPort) target).getParent(), false);
			}
			if (target == null && _target != null) {
				if (((DefaultPort) _target).getParent() instanceof CablePortCell)
					SchemeActions.disconnectSchemeCableLink(graph, DefaultCableLink.this, (CablePortCell) ((DefaultPort)_target).getParent(), false);
				
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
			return (SchemeCableLink) StorableObjectPool.getStorableObject(schemeCablelinkId, true);
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

