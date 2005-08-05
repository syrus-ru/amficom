/*
 * $Id: DefaultCableLink.java,v 1.8 2005/08/05 18:44:38 arseniy Exp $
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
 * @author $Author: arseniy $
 * @version $Revision: 1.8 $, $Date: 2005/08/05 18:44:38 $
 * @module schemeclient_v1
 */

public class DefaultCableLink extends DefaultEdge {
	private static final long serialVersionUID = 3618420414867519024L;

	private Identifier schemeCablelinkId;

	protected Point[] routed;
	protected transient DefaultPort _source, _target;
	protected transient DefaultPort source, target;
	private LinkRouting routing = new LinkRouting();

	public static DefaultCableLink createInstance(Object userObject,
			PortView firstPort, PortView port, Point p, Point p2, Map<Object, Map> viewMap,
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
		
		ArrayList<Point> list = new ArrayList<Point>();
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

	@Override
	public void setSource(Object port) {
		super.setSource(port);
		this.source = (DefaultPort)port;
	}

	@Override
	public void setTarget(Object port) {
		super.setTarget(port);
		this.target = (DefaultPort)port;
	}

	public Edge.Routing getRouting() {
		return this.routing;
	}

	public class LinkRouting implements Edge.Routing {
		private static final long serialVersionUID = 3618420414867519024L;

		public void route(EdgeView edge, java.util.List points) {
			DefaultCableLink cell = (DefaultCableLink) edge.getCell();
			SchemeGraph graph = (SchemeGraph) edge.getGraph();

			if (getSchemeCableLinkId() != null && !getSchemeCableLinkId().equals(cell.getSchemeCableLinkId()))
				setSchemeCableLinkId(cell.getSchemeCableLinkId());

			if (cell.source != cell._source) {
				DefaultCableLink.this.source = cell.source;
				DefaultCableLink.this._source = cell._source;
			}
			if (cell.target != cell._target) {
				DefaultCableLink.this.target = cell.target;
				DefaultCableLink.this._target = cell._target;
			}

			if (DefaultCableLink.this.source != null && !DefaultCableLink.this.source.equals(DefaultCableLink.this._source)) {
				if (DefaultCableLink.this._source != null) {
					if (DefaultCableLink.this._source.getParent() instanceof CablePortCell)
						SchemeActions.disconnectSchemeCableLink(graph, DefaultCableLink.this, (CablePortCell) DefaultCableLink.this._source.getParent(), true);
				}
				
				DefaultCableLink.this._source = DefaultCableLink.this.source;
				cell._source = cell.source;

				if (DefaultCableLink.this.source.getParent() instanceof CablePortCell)
					SchemeActions.connectSchemeCableLink(graph, DefaultCableLink.this,
							(CablePortCell) DefaultCableLink.this.source.getParent(), true);
			}
			if (DefaultCableLink.this.source == null && DefaultCableLink.this._source != null) {
				if (DefaultCableLink.this._source.getParent() instanceof CablePortCell)
					SchemeActions.disconnectSchemeCableLink(graph, DefaultCableLink.this, (CablePortCell) DefaultCableLink.this._source.getParent(), true);
				
				DefaultCableLink.this._source = DefaultCableLink.this.source;
				cell._source = cell.source;
			}

			if (DefaultCableLink.this.target != null && !DefaultCableLink.this.target.equals(DefaultCableLink.this._target)) {
				if (DefaultCableLink.this._target != null) {
					if (DefaultCableLink.this._target.getParent() instanceof CablePortCell)
						SchemeActions.disconnectSchemeCableLink(graph, DefaultCableLink.this, (CablePortCell) DefaultCableLink.this._target.getParent(), false);
				}
				
				DefaultCableLink.this._target = DefaultCableLink.this.target;
				cell._target = cell.target;

				if (DefaultCableLink.this.target.getParent() instanceof CablePortCell)
					SchemeActions.connectSchemeCableLink(graph, DefaultCableLink.this,
							(CablePortCell) DefaultCableLink.this.target.getParent(), false);
			}
			if (DefaultCableLink.this.target == null && DefaultCableLink.this._target != null) {
				if (DefaultCableLink.this._target.getParent() instanceof CablePortCell)
					SchemeActions.disconnectSchemeCableLink(graph, DefaultCableLink.this, (CablePortCell) DefaultCableLink.this._target.getParent(), false);
				
				DefaultCableLink.this._target = DefaultCableLink.this.target;
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
					DefaultCableLink.this.routed = new Point[4];
					DefaultCableLink.this.routed[0] = new Point(bounds.x + width, bounds.y + bounds.height);
					DefaultCableLink.this.routed[1] = new Point(bounds.x + width, bounds.y + bounds.height
							+ height);
					DefaultCableLink.this.routed[2] = new Point(bounds.x + 2 * width, bounds.y + bounds.height
							+ height);
					DefaultCableLink.this.routed[3] = new Point(bounds.x + 2 * width, bounds.y + bounds.height);
				} else {
					if (DefaultCableLink.this.routed == null) {
						int x2 = from.x + ((to.x - from.x) / 2);
						DefaultCableLink.this.routed = new Point[4];
						DefaultCableLink.this.routed[0] = graph.snap(new Point(x2, from.y));
						DefaultCableLink.this.routed[1] = graph.snap(new Point(x2, from.y));
						DefaultCableLink.this.routed[2] = graph.snap(new Point(x2, to.y));
						DefaultCableLink.this.routed[3] = graph.snap(new Point(x2, to.y));

					}
				}
				// Set/Add Points
				for (int i = 0; i < DefaultCableLink.this.routed.length; i++)
					if (points.size() > i + 2)
						points.set(i + 1, DefaultCableLink.this.routed[i]);
					else
						points.add(i + 1, DefaultCableLink.this.routed[i]);
				// Remove spare points
				while (points.size() > DefaultCableLink.this.routed.length + 2) {
					points.remove(points.size() - 2);
				}
			}
		}
	}

	public SchemeCableLink getSchemeCableLink() {
		try {
			return (SchemeCableLink) StorableObjectPool.getStorableObject(this.schemeCablelinkId, true);
		} catch (ApplicationException e) {
			Log.errorException(e);
			return null;
		}
	}

	public Identifier getSchemeCableLinkId() {
		return this.schemeCablelinkId;
	}

	public void setSchemeCableLinkId(Identifier id) {
		this.schemeCablelinkId = id;
	}
}

