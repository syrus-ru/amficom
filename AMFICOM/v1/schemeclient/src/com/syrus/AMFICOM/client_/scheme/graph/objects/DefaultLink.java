/*
 * $Id: DefaultLink.java,v 1.11 2005/09/20 19:47:52 stas Exp $
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
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.scheme.SchemeLink;
import com.syrus.util.Log;

/**
 * @author $Author: stas $
 * @version $Revision: 1.11 $, $Date: 2005/09/20 19:47:52 $
 * @module schemeclient
 */

public class DefaultLink extends DefaultEdge implements IdentifiableCell {
	private static final long serialVersionUID = 4050203024863737140L;

	private Identifier scheme_link_id;

	protected Point[] routed;
	protected transient DefaultPort _source, _target;
	protected transient DefaultPort source1, target1;
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
		
		ArrayList<Point> list = new ArrayList<Point>(2);
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
		this.source1 = (DefaultPort)port;
	}

	public void setTarget(Object port) {
		super.setTarget(port);
		this.target1 = (DefaultPort)port;
	}

	public DefaultEdge.Routing getRouting() {
		return this.routing;
	}

	public class LinkRouting implements DefaultEdge.Routing {
		private static final long serialVersionUID = 01L;

		public void route(EdgeView edge, java.util.List points) {
			DefaultLink cell = (DefaultLink) edge.getCell();
			SchemeGraph graph = (SchemeGraph) edge.getGraph();

			if (getSchemeLinkId() != null && !getSchemeLinkId().equals(cell.getSchemeLinkId()))
				setSchemeLinkId(cell.getSchemeLinkId());

			if (cell.source1 == null) {
				cell.source1 = (DefaultPort)cell.source;
			}
			if (cell.target1 == null) {
				cell.target1 = (DefaultPort)cell.target;
			}
			
			if (cell.source1 != cell._source) {
				DefaultLink.this.source1 = cell.source1;
				DefaultLink.this._source = cell._source;
			}
			if (cell.target1 != cell._target) {
				DefaultLink.this.target1 = cell.target1;
				DefaultLink.this._target = cell._target;
			}

			if (DefaultLink.this.source1 != null && !DefaultLink.this.source1.equals(DefaultLink.this._source)) {
				if (DefaultLink.this._source != null) {
					if (DefaultLink.this._source.getParent() instanceof PortCell) {
						if (!SchemeActions.disconnectSchemeLink(graph, DefaultLink.this, (PortCell) DefaultLink.this._source.getParent(), true)) {
							return;
						}
					}
				}
				
				DefaultLink.this._source = DefaultLink.this.source1;
				cell._source = cell.source1;

				if (DefaultLink.this.source1.getParent() instanceof PortCell)
					SchemeActions.connectSchemeLink(graph, DefaultLink.this,
							(PortCell) DefaultLink.this.source1.getParent(), true);
			}
			if (DefaultLink.this.source1 == null && DefaultLink.this._source != null) {
				if (DefaultLink.this._source.getParent() instanceof PortCell) {
					if (!SchemeActions.disconnectSchemeLink(graph, DefaultLink.this, (PortCell) DefaultLink.this._source.getParent(), true)) {
						return;
					}
				}
				
				DefaultLink.this._source = DefaultLink.this.source1;
				cell._source = cell.source1;
			}

			if (DefaultLink.this.target1 != null && !DefaultLink.this.target1.equals(DefaultLink.this._target))
			{
				if (DefaultLink.this._target != null) {
					if (DefaultLink.this._target.getParent() instanceof PortCell) {
						if (!SchemeActions.disconnectSchemeLink(graph, DefaultLink.this, (PortCell) DefaultLink.this._target.getParent(), false)) {
							return;
						}
					}
				}
				
				DefaultLink.this._target = DefaultLink.this.target1;
				cell._target = cell.target1;

				if (DefaultLink.this.target1.getParent() instanceof PortCell) {
					SchemeActions.connectSchemeLink(graph, DefaultLink.this,
							(PortCell) DefaultLink.this.target1.getParent(), false);
				}
			}
			if (DefaultLink.this.target1 == null && DefaultLink.this._target != null) {
				if (DefaultLink.this._target.getParent() instanceof PortCell) {
					if (!SchemeActions.disconnectSchemeLink(graph, DefaultLink.this, (PortCell) DefaultLink.this._target.getParent(), false)) {
						return;
					}
				}
				
				DefaultLink.this._target = DefaultLink.this.target1;
				cell._target = cell.target1;
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
					DefaultLink.this.routed = new Point[4];
					DefaultLink.this.routed[0] = graph.snap(new Point(bounds.x + width, bounds.y
							+ bounds.height));
					DefaultLink.this.routed[1] = graph.snap(new Point(bounds.x + width, bounds.y
							+ bounds.height + height));
					DefaultLink.this.routed[2] = graph.snap(new Point(bounds.x + 2 * width, bounds.y
							+ bounds.height + height));
					DefaultLink.this.routed[3] = graph.snap(new Point(bounds.x + 2 * width, bounds.y
							+ bounds.height));
				} else {
					boolean bendable = GraphConstants.isBendable(edge.getAllAttributes());
					if (!bendable || DefaultLink.this.routed == null) {
						int x2 = from.x + ((to.x - from.x) / 2);
						int y2 = from.y + ((to.y - from.y) / 2);
						DefaultLink.this.routed = new Point[4];
						int grid = graph.getGridSize();
						if (from.x <= to.x) {
							DefaultLink.this.routed[0] = graph.snap(new Point(from.x + grid, from.y));
							DefaultLink.this.routed[1] = graph.snap(new Point(x2, from.y));
							DefaultLink.this.routed[2] = graph.snap(new Point(x2, to.y));
							DefaultLink.this.routed[3] = graph.snap(new Point(to.x - grid, to.y));
						} else {
							DefaultLink.this.routed[0] = graph.snap(new Point(from.x + grid, from.y));
							DefaultLink.this.routed[1] = graph.snap(new Point(from.x + grid, y2));
							DefaultLink.this.routed[2] = graph.snap(new Point(to.x - grid, y2));
							DefaultLink.this.routed[3] = graph.snap(new Point(to.x - grid, to.y));
						}
					}
				}
				// Set/Add Points
				for (int i = 0; i < DefaultLink.this.routed.length; i++)
					if (points.size() > i + 2)
						points.set(i + 1, DefaultLink.this.routed[i]);
					else
						points.add(i + 1, DefaultLink.this.routed[i]);
				// Remove spare points
				while (points.size() > DefaultLink.this.routed.length + 2) {
					points.remove(points.size() - 2);
				}
			}
		}
	}

	public SchemeLink getSchemeLink() {
		try {
			return (SchemeLink) StorableObjectPool.getStorableObject(this.scheme_link_id, true);
		} catch (ApplicationException ex) {
			Log.errorException(ex);
			return null;
		}
	}

	public Identifier getSchemeLinkId() {
		return this.scheme_link_id;
	}

	public void setSchemeLinkId(Identifier id) {
		assert id != null;
		this.scheme_link_id = id;
	}


	public Identifier getId() {
		return this.scheme_link_id;
	}


	public void setId(Identifier id) {
		assert id != null;
		this.scheme_link_id = id;
	}
}
