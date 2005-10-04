/*
 * $Id: DefaultCableLink.java,v 1.13 2005/10/04 16:25:54 stas Exp $
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
 * @version $Revision: 1.13 $, $Date: 2005/10/04 16:25:54 $
 * @module schemeclient
 */

public class DefaultCableLink extends DefaultEdge implements IdentifiableCell {
	private static final long serialVersionUID = 3618420414867519024L;

	private Identifier schemeCablelinkId;

	protected Point[] routed;
	protected transient DefaultPort _source, _target;
	protected transient DefaultPort source1, target1;
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
		this.source1 = (DefaultPort)port;
	}

	@Override
	public void setTarget(Object port) {
		super.setTarget(port);
		this.target1 = (DefaultPort)port;
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

			if (cell.source1 == null) {
				cell.source1 = (DefaultPort)cell.source;
			}
			if (cell.target1 == null) {
				cell.target1 = (DefaultPort)cell.target;
			}
			
			if (cell.source1 != cell._source) {
				DefaultCableLink.this.source1 = cell.source1;
				DefaultCableLink.this._source = cell._source;
			}
			if (cell.target1 != cell._target) {
				DefaultCableLink.this.target1 = cell.target1;
				DefaultCableLink.this._target = cell._target;
			}

			if (DefaultCableLink.this.source1 != null && !DefaultCableLink.this.source1.equals(DefaultCableLink.this._source)) {
				if (DefaultCableLink.this._source != null) {
					if (DefaultCableLink.this._source.getParent() instanceof CablePortCell)
						SchemeActions.disconnectSchemeCableLink(graph, DefaultCableLink.this, (CablePortCell) DefaultCableLink.this._source.getParent(), true);
				}
				
				DefaultCableLink.this._source = DefaultCableLink.this.source1;
				cell._source = cell.source1;

				if (DefaultCableLink.this.source1.getParent() instanceof CablePortCell)
					SchemeActions.connectSchemeCableLink(graph, DefaultCableLink.this,
							(CablePortCell) DefaultCableLink.this.source1.getParent(), true);
			}
			if (DefaultCableLink.this.source1 == null && DefaultCableLink.this._source != null) {
				if (DefaultCableLink.this._source.getParent() instanceof CablePortCell)
					SchemeActions.disconnectSchemeCableLink(graph, DefaultCableLink.this, (CablePortCell) DefaultCableLink.this._source.getParent(), true);
				
				DefaultCableLink.this._source = DefaultCableLink.this.source1;
				cell._source = cell.source1;
			}

			if (DefaultCableLink.this.target1 != null && !DefaultCableLink.this.target1.equals(DefaultCableLink.this._target)) {
				if (DefaultCableLink.this._target != null) {
					if (DefaultCableLink.this._target.getParent() instanceof CablePortCell)
						SchemeActions.disconnectSchemeCableLink(graph, DefaultCableLink.this, (CablePortCell) DefaultCableLink.this._target.getParent(), false);
				}
				
				DefaultCableLink.this._target = DefaultCableLink.this.target1;
				cell._target = cell.target1;

				if (DefaultCableLink.this.target1.getParent() instanceof CablePortCell)
					SchemeActions.connectSchemeCableLink(graph, DefaultCableLink.this,
							(CablePortCell) DefaultCableLink.this.target1.getParent(), false);
			}
			if (DefaultCableLink.this.target1 == null && DefaultCableLink.this._target != null) {
				if (DefaultCableLink.this._target.getParent() instanceof CablePortCell)
					SchemeActions.disconnectSchemeCableLink(graph, DefaultCableLink.this, (CablePortCell) DefaultCableLink.this._target.getParent(), false);
				
				DefaultCableLink.this._target = DefaultCableLink.this.target1;
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
					DefaultCableLink.this.routed = new Point[4];
					DefaultCableLink.this.routed[0] = new Point(bounds.x + width, bounds.y + bounds.height);
					DefaultCableLink.this.routed[1] = new Point(bounds.x + width, bounds.y + bounds.height
							+ height);
					DefaultCableLink.this.routed[2] = new Point(bounds.x + 2 * width, bounds.y + bounds.height
							+ height);
					DefaultCableLink.this.routed[3] = new Point(bounds.x + 2 * width, bounds.y + bounds.height);
				} else {
					if (DefaultCableLink.this.routed == null) {
						 DefaultCableLink.this.routed = createDefaultRouting(graph, from, to);
					} else {
						int grid = edge.getGraph().getGridSize();
						Point[] p1 = createDefaultRouting(graph, from, to);
						if (DefaultCableLink.this.routed.length != p1.length) {
							DefaultCableLink.this.routed = p1;
						}
						Point[] p = DefaultCableLink.this.routed;
						// крайние точки
						if (p[0].y != from.y) {
							p[0].y = from.y;
						}
						if (p[0].x <= from.x) {
							p[0].x = from.x + grid;
						}
						if (p[p.length-1].y != to.y) {
							p[p.length-1].y = to.y;
						}
						if (p[p.length-1].x >= to.x) {
							p[p.length-1].x = to.x - grid;
						}
						// средние точки
						if (p.length == 2) {
							if (p[1].x != p[0].x) {
								p[1].x = p[0].x;
							}
						} else if (p.length == 4) {
							if (p[1].x != p[0].x) {
								p[1].x = p[0].x;
							}
							if (p[2].x != p[3].x) {
								p[2].x = p[3].x;
							}
							if (p[2].y != p[1].y) {
								p[2].y = p[1].y;
							}
						}
//						
//						Point[] tmp = new Point[4];
//						createDefaultRouting(graph, tmp, from, to);
//						if (false) {
//							DefaultCableLink.this.routed = tmp;
//						}
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
	
	Point[] createDefaultRouting(SchemeGraph graph, Point from, Point to) {
		int x2 = from.x + ((to.x - from.x) / 2);
		int y2 = from.y + ((to.y - from.y) / 2);
		int grid = graph.getGridSize();
		Point[] p;
		if (from.x + graph.getGridSize() * 3 <= to.x) {
			p = new Point[2];
//			p[0] = graph.snap(new Point(from.x + grid, from.y));
			p[0] = graph.snap(new Point(x2, from.y));
			p[1] = graph.snap(new Point(x2, to.y));
//			p[3] = graph.snap(new Point(to.x - grid, to.y));
		} else {
			p = new Point[4];
			p[0] = graph.snap(new Point(from.x + grid, from.y));
			p[1] = graph.snap(new Point(from.x + grid, y2));
			p[2] = graph.snap(new Point(to.x - grid, y2));
			p[3] = graph.snap(new Point(to.x - grid, to.y));
		}
		return p;
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

	public Identifier getId() {
		return this.schemeCablelinkId;
	}

	public void setId(Identifier id) {
		this.schemeCablelinkId = id;
	}
}

