/*
 * $Id: DefaultLink.java,v 1.19 2005/10/30 15:20:56 bass Exp $
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
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.scheme.SchemeLink;
import com.syrus.util.Log;

/**
 * @author $Author: bass $
 * @version $Revision: 1.19 $, $Date: 2005/10/30 15:20:56 $
 * @module schemeclient
 */

public class DefaultLink extends DefaultEdge implements IdentifiableCell {
	private static final long serialVersionUID = 4050203024863737140L;

	private Identifier scheme_link_id;

	protected Point[] _routed;
	protected Point[] routed;
	protected Point _from;
	protected Point _to;
	protected transient DefaultPort _source, _target;
	protected transient DefaultPort source1, target1;
	private LinkRouting routing = new LinkRouting();

	public static DefaultLink createInstance(Object userObject,
			PortView firstPort, PortView port, Point p, Point p2, Map viewMap,
			ConnectionSet cs, Identifier linkId, boolean allowUnconnected) throws CreateObjectException {

		// we can connect link to PortCell or not connect at all
		PortCell sourcePortCell = null;
		PortCell targetPortCell = null;

		if (firstPort != null) {
			Object o = ((DefaultPort) firstPort.getCell()).getParent();
			if (o instanceof PortCell)
				sourcePortCell = (PortCell) o;
		}
		if (port != null) {
			Object o = ((DefaultPort) port.getCell()).getParent();
			if (o instanceof PortCell)
				targetPortCell = (PortCell) o;
		}
		
		if (!allowUnconnected && (sourcePortCell == null || targetPortCell == null)) {
			throw new CreateObjectException("Link must be connected to ports : " + linkId.getIdentifierString());
		}
		if (sourcePortCell != null) {
			SchemeLink cl1 = sourcePortCell.getSchemePort().getAbstractSchemeLink();
			if (cl1 != null && !cl1.getId().equals(linkId)) {
				throw new CreateObjectException("Other link (" + cl1.getId() + ") already connected to port; instead of " + linkId.getIdentifierString());
			}
		}
		if (targetPortCell != null) {
			SchemeLink cl2 = targetPortCell.getSchemePort().getAbstractSchemeLink();
			if (cl2 != null && !cl2.getId().equals(linkId)) {
				throw new CreateObjectException("Other link (" + cl2.getId() + ") already connected to port; instead of " + linkId.getIdentifierString());
			}
		}
		
		DefaultLink cell = new DefaultLink(userObject);
		cell.setSchemeLinkId(linkId);
		
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
		
		if (sourcePortCell != null)
			cs.connect(cell, firstPort.getCell(), true);
		if (targetPortCell != null)
			cs.connect(cell, port.getCell(), false);
		
		return cell;
	}

	
	private DefaultLink(Object userObject) {
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
						SchemeActions.disconnectSchemeLink(graph, DefaultLink.this, (PortCell) DefaultLink.this._source.getParent(), true);
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
					SchemeActions.disconnectSchemeLink(graph, DefaultLink.this, (PortCell) DefaultLink.this._source.getParent(), true);
				}
				
				DefaultLink.this._source = DefaultLink.this.source1;
				cell._source = cell.source1;
			}

			if (DefaultLink.this.target1 != null && !DefaultLink.this.target1.equals(DefaultLink.this._target))
			{
				if (DefaultLink.this._target != null) {
					if (DefaultLink.this._target.getParent() instanceof PortCell) {
						SchemeActions.disconnectSchemeLink(graph, DefaultLink.this, (PortCell) DefaultLink.this._target.getParent(), false);
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
					SchemeActions.disconnectSchemeLink(graph, DefaultLink.this, (PortCell) DefaultLink.this._target.getParent(), false);
				}
				
				DefaultLink.this._target = DefaultLink.this.target1;
				cell._target = cell.target1;
			}
			
			Point from = edge.getPoint(0);
			if (edge.getSource() != null) {
				from = edge.getSource().getLocation(null);
			}
			if (DefaultLink.this._from == null) {
				DefaultLink.this._from = new Point();
			}
			Point to = edge.getPoint(points.size() - 1);
			if (edge.getTarget() != null) {
				to = edge.getTarget().getLocation(null);
			}
			if (DefaultLink.this._to == null) {
				DefaultLink.this._to = new Point();
			}
			if (from != null && to != null) {
				// Handle self references
				if (edge.getSource() == edge.getTarget() && edge.getSource() != null) {
					Rectangle bounds = edge.getSource().getParentView().getBounds();
					int height = edge.getGraph().getGridSize();
					int width = (int) (bounds.getWidth() / 3);
					DefaultLink.this.routed = new Point[4];
					DefaultLink.this.routed[0] = new Point(bounds.x + width, bounds.y + bounds.height);
					DefaultLink.this.routed[1] = new Point(bounds.x + width, bounds.y + bounds.height
							+ height);
					DefaultLink.this.routed[2] = new Point(bounds.x + 2 * width, bounds.y + bounds.height
							+ height);
					DefaultLink.this.routed[3] = new Point(bounds.x + 2 * width, bounds.y + bounds.height);
				} else {
					if (DefaultLink.this.routed == null) {
						 DefaultLink.this.routed = createDefaultRouting(graph, from, to);
					} else {
						Point[] p1 = createDefaultRouting(graph, from, to);
						if (DefaultLink.this.routed.length != p1.length) {
							DefaultLink.this.routed = p1;
						}
						if (DefaultLink.this._routed != null
								&& DefaultLink.this._routed.length == DefaultLink.this.routed.length) {

							if ((DefaultLink.this._from != null && DefaultLink.this._to != null)
									&& (DefaultLink.this._from.x != from.x && DefaultLink.this._to.x != to.x)
									|| (DefaultLink.this._from.y != from.y && DefaultLink.this._to.y != to.y)) {
								
								int delta = from.x - DefaultLink.this._from.x;
								if (getSource() != null) {
									DefaultLink.this.routed[0].x += delta;
									if (DefaultLink.this.routed.length == 4) {
										DefaultLink.this.routed[1].x += delta;
									}
								}
								if (getTarget() != null) {
									delta = to.x - DefaultLink.this._to.x;
									if (DefaultLink.this.routed.length == 2) {
										DefaultLink.this.routed[1].x += delta;
									} else if (DefaultLink.this.routed.length == 4) {
										DefaultLink.this.routed[2].x += delta;
										DefaultLink.this.routed[3].x += delta;
									}
								}
//								if (getSource() != null && getTarget() != null) {
//									int deltay = ((from.y - DefaultLink.this._from.y) +
//											(to.y - DefaultLink.this._to.y)) / 2;
//									if (DefaultLink.this.routed.length == 4) {
//										DefaultLink.this.routed[1].y += deltay;
//										DefaultLink.this.routed[2].y += deltay;
//									}
//								}
								
							/*if (DefaultLink.this._from != null
									&& (DefaultLink.this._from.x != from.x || DefaultLink.this._from.y != from.y)) {
								// если сдвинулись одновременно начало и конец - перемещаем целиком
								if (edge.getSource() != null) {
									int delta = graph.snap((from.x - DefaultLink.this._from.x) / 2);
									DefaultLink.this.routed[0].x += delta;
									DefaultLink.this.routed[1].x += delta;
									if (DefaultLink.this.routed.length == 4) {
										delta = graph.snap(from.y - DefaultLink.this._from.y);
										DefaultLink.this.routed[1].y += delta;
										DefaultLink.this.routed[2].y += delta;
									}
								}
							} else if (DefaultLink.this._from != null
									&& (DefaultLink.this._from.x != from.x || DefaultLink.this._from.y != from.y)) {
								// если сдвинулись одновременно начало и конец - перемещаем целиком
								if (edge.getTarget() != null) {
									if (DefaultLink.this.routed.length == 4) {
										int delta = graph.snap(to.x - DefaultLink.this._to.x);
										DefaultLink.this.routed[2].x += delta;
										DefaultLink.this.routed[3].x += delta;
										delta = graph.snap(to.y - DefaultLink.this._to.y);
										DefaultLink.this.routed[1].y += delta;
										DefaultLink.this.routed[2].y += delta;
									}
								}*/
							} else {
								int grid = edge.getGraph().getGridSize();
								Point[] p = DefaultLink.this.routed;
								Point[] _p = DefaultLink.this._routed;
								
								// двигать можно только за точку, за линию - игнорим
								if (p.length == 2) {
									// только горизонтальные перемещения доступны
									if (p[0].x != _p[0].x && p[1].x == _p[1].x) { // двигаем точку 0
										if (p[0].x >= to.x) {
											p[0].x = to.x - grid;
										}
										p[1].x = p[0].x;
									} else if (p[1].x != _p[1].x && p[0].x == _p[0].x) { // двигаем точку 1
										if (p[1].x <= from.x) {
											p[1].x = from.x + grid;
										}
										p[0].x = p[1].x;
									} else { // сбрасываем все изменения по х
										for (int i = 0; i < p.length; i++) {
											p[i].x = _p[i].x;
										}
									}
								} else if (p.length == 4) {
									boolean changed = false;
									//	горизонтальные перемещения
									if (from.x != DefaultLink.this._from.x) { // двигаем начало
//										if (edge.getSource() != null || edge.getTarget() != null) {
											int delta = from.x - DefaultLink.this._from.x;
											p[0].x += delta;
											p[1].x = p[0].x;
											changed = true;
//										}
									}
									if (to.x != DefaultLink.this._to.x) { // двигаем конец
//										if (edge.getTarget() != null || edge.getSource() != null) {
											int delta = to.x - DefaultLink.this._to.x;
											p[3].x += delta;
											p[2].x = p[3].x;
											changed = true;
//										}
									}
									if (p[0].x != _p[0].x && p[1].x == _p[1].x) { // двигаем точку 0
										p[1].x = p[0].x;
										changed = true;
									} else if (p[1].x != _p[1].x && p[2].x == _p[2].x) { // двигаем точку 1
										if (p[1].x <= from.x) {
											p[1].x = from.x + grid;
										}
										p[0].x = p[1].x;
										changed = true;
									} 
									if (p[2].x != _p[2].x && p[1].x == _p[1].x) { // двигаем точку 2
										if (p[2].x >= to.x) {
											p[2].x = to.x - grid;
										}
										p[3].x = p[2].x;
										changed = true;
									} else if (p[3].x != _p[3].x && p[2].x == _p[2].x) { // двигаем точку 3
										p[2].x = p[3].x;
										changed = true;
									} 
									// сбрасываем все изменения по х
									if (!changed) {
										for (int i = 0; i < p.length; i++) {
											p[i].x = _p[i].x;
										}
									}
									
									//	вертикальные перемещения для точек 1, 2
									if (p[1].y != _p[1].y && p[2].y == _p[2].y) { // двигаем точку 1
										p[2].y = p[1].y;
									} else if (p[2].y != _p[2].y && p[1].y == _p[1].y) { // двигаем точку 1
										p[1].y = p[2].y;
									} else if (from.y != DefaultLink.this._from.y) { // двигаем начало
										p[0].y = from.y;
									} else if (to.x != DefaultLink.this._to.y) { // двигаем начало
										p[3].y = to.y;
									} else { // сбрасываем все изменения по y
										for (int i = 1; i < p.length - 1; i++) {
											p[i].y = _p[i].y;
										}
									}
								}
								
								// крайние точки обязаны находиться на одном y с концами
								if (p[0].y != from.y) {
									p[0].y = from.y;
								}
								if (p[p.length-1].y != to.y) {
									p[p.length-1].y = to.y;
								}
								if (p[0].x <= from.x) {
									p[0].x = from.x + grid;
								}
								if (p[p.length-1].x >= to.x) {
									p[p.length-1].x = to.x - grid;
								}
							}
						} else {
							DefaultLink.this._routed = new Point[DefaultLink.this.routed.length];
							for (int i = 0; i < DefaultLink.this._routed.length; i++) {
								DefaultLink.this._routed[i] = new Point();
							}
						}
						for (int i = 0; i < DefaultLink.this.routed.length; i++) {
							DefaultLink.this._routed[i].x = DefaultLink.this.routed[i].x;
							DefaultLink.this._routed[i].y = DefaultLink.this.routed[i].y;
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
			DefaultLink.this._from.x = from.x;
			DefaultLink.this._from.y = from.y;
			DefaultLink.this._to.x = to.x;
			DefaultLink.this._to.y = to.y;

//			GraphConstants.setPoints(edge.getAllAttributes(), points);
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

	public SchemeLink getSchemeLink() {
		try {
			return (SchemeLink) StorableObjectPool.getStorableObject(this.scheme_link_id, true);
		} catch (ApplicationException ex) {
			assert Log.errorMessage(ex);
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
